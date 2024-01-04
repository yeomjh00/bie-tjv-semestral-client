package spring_client.semestral.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring_client.semestral.data_format.MusicDto;
import spring_client.semestral.data_format.MusicListDto;
import spring_client.semestral.service.MusicAndListService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/users/{userId}/musics")
public class MusicController {

    private final MusicAndListService musicAndListService;

    @Autowired
    public MusicController(MusicAndListService musicAndListService){
        this.musicAndListService = musicAndListService;
    }

    @GetMapping
    public String viewAllMusic(Model model, @PathVariable Long userId){
        List<MusicDto> musicDto = musicAndListService.readAllMusics();
        model.addAttribute("musics", musicDto);
        model.addAttribute("userId", userId);
        log.info("Viewing all musics size of {}", musicDto.size());
        return "musics/musics";
    }

    @PostMapping
    public String uploadMusic(Model model, @PathVariable Long userId){
        List<MusicDto> musicDto = musicAndListService.readAllMusics();
        model.addAttribute("musics", musicDto);
        model.addAttribute("userId", userId);
        log.info("Viewing all musics size of {}", musicDto.size());
        return "musics/musics";
    }

    @GetMapping("/new")
    public String newMusic(Model model, @PathVariable Long userId){
        model.addAttribute("userId", userId);
        return "musics/new";
    }

    @PostMapping("/new")
    public String createMusic(Model model, @PathVariable Long userId, MusicDto generatedMusic){
        log.info("Trial for creating new music");
        if(generatedMusic.getTitle() == null || generatedMusic.getTitle().isEmpty()){
            log.info("Title is null");
            return "redirect:/users/{userId}/musics";
        }
        musicAndListService.createMusic(generatedMusic);
       return "redirect:/users/{userId}/musics";
    }

    @GetMapping("/{musicId}")
    public String viewSpecificMusic(Model model, @PathVariable Long userId, @PathVariable Long musicId){
        Optional<MusicDto> musicDto = musicAndListService.readMusicById(musicId);
        if(musicDto.isEmpty()){
            log.info("Music not found");
            return "redirect:/users/{userId}/musics";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("music", musicDto.get());
        return "musics/singleMusic";
    }
}
