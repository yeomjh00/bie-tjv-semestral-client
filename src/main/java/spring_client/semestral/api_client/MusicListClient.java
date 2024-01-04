package spring_client.semestral.api_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import spring_client.semestral.data_format.MusicListDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class MusicListClient {
    private String baseUrl;
    private RestClient musicListClient;

    public MusicListClient(@Value("${api.url}") String baseUrl) {
        this.baseUrl = baseUrl + "/musiclists";
        musicListClient = RestClient.builder().baseUrl(this.baseUrl).build();
    }


    public List<MusicListDto> readAllMusicListsByUserId(Long userId) {
        return Optional.of(musicListClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/owned")
                                .queryParam("user_id", userId)
                                .build())
                        .retrieve()
                        .toEntity(MusicListDto[].class))
                .map(ResponseEntity::getBody)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public void deleteMusicListById(Long musicListId) {
        musicListClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(musicListId))
                .retrieve()
                .toBodilessEntity();
    }

    public Optional<MusicListDto> readMusicListById(Long musicListId) {
        ResponseEntity<MusicListDto> response = musicListClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(musicListId))
                .retrieve()
                .toEntity(MusicListDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }
        return Optional.empty();
    }

    public void createEmptyList(MusicListDto musicListDto) {
        musicListClient.post()
                .body(musicListDto)
                .retrieve()
                .toBodilessEntity();
    }

    public void updateMusicList(MusicListDto updatedList, Long musicListId) {
        musicListClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(musicListId))
                .body(updatedList)
                .retrieve()
                .toBodilessEntity();
    }

    public Optional<Long> countMusicListsByUserId(Long userId) {
        return Optional.ofNullable(musicListClient.get()
                .uri(uriBuilder -> uriBuilder
                                .path("/count-list")
                                .queryParam("user_id", userId)
                                .build())
                .retrieve()
                .body(Long.class));
    }

    public Optional<Long> countMusicByListId(Long musicListId) {
        return Optional.ofNullable(musicListClient.get()
                .uri(uriBuilder -> uriBuilder
                                .path("/count-music")
                                .queryParam("list_id", musicListId)
                                .build())
                .retrieve()
                .body(Long.class));
    }
}
