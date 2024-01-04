package spring_client.semestral.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring_client.semestral.api_client.MusicClient;
import spring_client.semestral.api_client.MusicListClient;
import spring_client.semestral.api_client.UserClient;
import spring_client.semestral.data_format.MusicDto;
import spring_client.semestral.data_format.MusicListDto;
import spring_client.semestral.data_format.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MusicAndListService {
    private MusicClient musicClient;
    private MusicListClient musicListClient;
    private UserClient userClient;

    @Autowired
    public MusicAndListService(MusicClient musicClient,
                               MusicListClient musicListClient,
                               UserClient userClient) {
        this.musicClient = musicClient;
        this.musicListClient = musicListClient;
        this.userClient = userClient;
    }

    public void createMusic(MusicDto musicDto) {
        if(musicDto.getUri().length() > 1023) return;

        MusicDto createdMusic = MusicDto.builder()
                        .uri(musicDto.getUri())
                        .title(musicDto.getTitle())
                        .artist(musicDto.getArtist())
                        .build();
        musicClient.createMusic(musicDto);
    }

    public Optional<MusicDto> readMusicById(Long id) {
        return Optional.ofNullable(musicClient.readMusicById(id));
    }

    public List<MusicDto> readAllMusics() {
        return musicClient.readAllMusics();
    }

    public List<MusicListDto> readAllMusicListsByUserId(Long userId) {
        return musicListClient.readAllMusicListsByUserId(userId);
    }

    public void deleteMusicListById(Long musicListId) {
        musicListClient.deleteMusicListById(musicListId);
    }

    public Long checkAvailableNumberOfLists(Long userId) {
        Long numberOfListsByUserId = musicListClient
                .countMusicListsByUserId(userId).orElseGet(() -> 0L);

        userClient.setCurrentUser(userId);
        Optional<UserDto> user = userClient.readCurrentUserInfo();

        if (user.isPresent()) {
            Long maxNumberOfList = "membership".equals(user.get().getUserStatus()) ? Long.MAX_VALUE : 3L;
            return maxNumberOfList - numberOfListsByUserId;
        }
        return 0L;
    }

    public Long checkAvailableNumberOfMusics(Long userId, Long musicListId) {
        Long numberOfMusicsByMusicListId = musicListClient
                .countMusicByListId(musicListId).orElseGet(() -> 0L);

        userClient.setCurrentUser(userId);
        Optional<UserDto> user = userClient.readCurrentUserInfo();

        if (user.isPresent()) {
            Long maxNumberOfMusics = "membership".equals(user.get().getUserStatus())  ? Long.MAX_VALUE : 30L;
            return maxNumberOfMusics - numberOfMusicsByMusicListId;
        }
        return 0L;
    }

    public void createEmptyList(MusicListDto musicListDto) {
        musicListClient.createEmptyList(musicListDto);
    }

    public Optional<MusicListDto> readMusicListById(Long musicListId) {
        return musicListClient.readMusicListById(musicListId);
    }

    public void addMusicToList(MusicListDto updatedList,
                                         List<Long> musicIds,
                                         Long musicListId) {
        List<MusicDto> addedList = new ArrayList<>();
        for (Long musicId : musicIds) {
            MusicDto musicDto = musicClient.readMusicById(musicId);
            addedList.add(musicDto);
        }
        addedList.addAll(updatedList.getTrack());
        updatedList.setTrack(addedList);

        musicListClient.updateMusicList(updatedList, musicListId);
    }

    public void removeMusicAndUpdateList(MusicListDto updatedList,
                                List<Long> musicIds,
                                Long musicListId) {
        ArrayList<MusicDto> removeList = new ArrayList<>(updatedList.getTrack());
        for (Long id : musicIds) {
            Optional<MusicDto> musicToRemove = removeList.stream()
                    .filter(musicDto -> musicDto.getId().equals(id))
                    .findFirst();

            musicToRemove.ifPresent(removeList::remove);
        }
        updatedList.setTrack(removeList);
        musicListClient.updateMusicList(updatedList, musicListId);
    }
}
