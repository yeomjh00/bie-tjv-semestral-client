package spring_client.semestral.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring_client.semestral.data_format.MusicDto;
import spring_client.semestral.data_format.MusicListDto;
import spring_client.semestral.service.MusicAndListService;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/users/{userId}/musiclists")
public class MusicListController {

    private final MusicAndListService musicAndListService;

    @Autowired
    public MusicListController(MusicAndListService musicAndListService){
        this.musicAndListService = musicAndListService;
    }

    @GetMapping
    public String viewMyMusicLists(Model model, @PathVariable Long userId){
        List<MusicListDto> musicListDto = musicAndListService.readAllMusicListsByUserId(userId);
        model.addAttribute("musiclists", musicListDto);
        model.addAttribute("userId", userId);
        log.info("Viewing all musiclists size of {}", musicListDto.size());
        return "musiclists/musicList";
    }

    @GetMapping("/new")
    public String getListForm(Model model, @PathVariable Long userId){
        model.addAttribute("userId", userId);
        return "musiclists/new";
    }

    @PostMapping("/new")
    public String createEmptyList(Model model, @PathVariable Long userId, MusicListDto generatedList){
        Long availableNumberOfLists = musicAndListService.checkAvailableNumberOfLists(userId);
        log.info("Trial: possible number to create lists: {}", availableNumberOfLists);
        if(availableNumberOfLists > 0){
            log.info("Creating new list");
            MusicListDto musicListDto = MusicListDto.builder()
                            .listName(generatedList.getListName())
                            .ownerId(userId)
                            .description(generatedList.getDescription())
                                    .build();
            musicAndListService.createEmptyList(musicListDto);
        }
        return "redirect:/users/{userId}/musiclists";
    }

    @GetMapping("/{musicListId}")
    public String viewSpecificMusicList(Model model, @PathVariable Long userId, @PathVariable Long musicListId){
        Optional<MusicListDto> musicListDto = musicAndListService.readMusicListById(musicListId);
        if(musicListDto.isEmpty()){
            log.info("MusicList not found");
            return "redirect:/users/{userId}/musiclists";
        }
        model.addAttribute("musiclist", musicListDto.get());
        model.addAttribute("userId", userId);
        log.info("Viewing specific musiclist");
        return "musiclists/singleMusicList";
    }

    @GetMapping("/{musicListId}/edit") // edit info and drop musics
    public String editSpecificMusicList(Model model, @PathVariable Long userId,@PathVariable Long musicListId){
        Optional<MusicListDto> musicListDto = musicAndListService.readMusicListById(musicListId);
        if(musicListDto.isEmpty()){
            log.info("MusicList not found");
            return "redirect:/users/{userId}/musiclists";
        }
        model.addAttribute("musiclist", musicListDto.get());
        model.addAttribute("userId", userId);
        log.info("Viewing specific musiclist");
        return "musiclists/removeMusicAndEdit";
    }

    @PostMapping("/{musicListId}/edit") // edit info and drop musics
    public String updateMusicListAndMusics(Model model,
                                           @PathVariable Long userId,
                                           @PathVariable Long musicListId,
                                           @RequestParam("listName") String listName,
                                           @RequestParam("description") String description,
                                           @RequestParam(value = "musicIds", required = false) List<Long> musicIds){
        Optional<MusicListDto> musicListDto = musicAndListService.readMusicListById(musicListId);
        List<Long> ids = Optional.ofNullable(musicIds).orElse(Collections.emptyList());
        if(musicListDto.isEmpty()){
            log.info("MusicList not found");
            return "redirect:/users/{userId}/musiclists";
        }

        MusicListDto mld = musicListDto.get();
        mld.setListName(listName);
        mld.setDescription(description);
        musicAndListService.removeMusicAndUpdateList(mld, ids, musicListId);

        Optional<MusicListDto> updatedMusicList = musicAndListService.readMusicListById(musicListId);
        if(updatedMusicList.isEmpty()){
            log.info("Updated MusicList not found");
            return "redirect:/users/{userId}/musiclists";
        }
        model.addAttribute("musiclist", updatedMusicList);
        model.addAttribute("userId", userId);
        log.info("Confirm update of specific musiclist");
        return "redirect:/users/{userId}/musiclists/{musicListId}";
    }

    @GetMapping("/{musicListId}/delete")
    public String deleteSpecificMusicList(Model model, @PathVariable Long userId, @PathVariable Long musicListId){
        Optional<MusicListDto> musicListDto = musicAndListService.readMusicListById(musicListId);
        if(musicListDto.isEmpty()){
            log.info("MusicList not found");
            return "redirect:/users/{userId}/musiclists";
        }
        musicAndListService.deleteMusicListById(musicListId);
        return "redirect:/users/{userId}/musiclists";
    }

    @GetMapping("/{musicListId}/add") // From all musics, add selected musics to a list
    public String addMusicPage(Model model,
                               @PathVariable Long userId,
                               @PathVariable Long musicListId){
        List<MusicDto> allMusics = musicAndListService.readAllMusics();
        model.addAttribute("musics", allMusics);
        model.addAttribute("userId", userId);
        model.addAttribute("musicListId", musicListId);
        return "musiclists/add2MusicList";
    }

    @PostMapping("/{musicListId}/add") // From all musics, add selected musics to a list
    public String addMusicToList(Model model,
                                 @RequestParam List<Long> musicIds,
                                 @PathVariable Long musicListId,
                                 @PathVariable Long userId){
        Optional<MusicListDto> musicListDto = musicAndListService.readMusicListById(musicListId);
        if(musicListDto.isEmpty()){
            log.info("Music list not found");
            return "redirect:/users/{userId}/musiclists";
        }

        Long currentAvailableMusics = musicAndListService.checkAvailableNumberOfMusics(userId, musicListId);
        log.info("current number of music: {}, tried to add {} musics",
                currentAvailableMusics, musicListDto.get().getTrack().size());
        if (musicAndListService.checkAvailableNumberOfMusics(userId, musicListId) <= musicIds.size()){
            log.info("Too many musics, {}", musicAndListService.checkAvailableNumberOfMusics(userId, musicListId));
            return "redirect:/users/{userId}/musiclists";
        }
        musicAndListService.addMusicToList(musicListDto.get(), musicIds, musicListId);
        Optional<MusicListDto> updatedMusicListDto = musicAndListService.readMusicListById(musicListId);
        if(updatedMusicListDto.isEmpty()){
            log.info("Music list not found");
            return "redirect:/users/{userId}/musiclists";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("musiclist", updatedMusicListDto.get());
        return "redirect:/users/{userId}/musiclists/{musicListId}";
    }


}
