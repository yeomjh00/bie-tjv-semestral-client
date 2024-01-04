package spring_client.semestral.api_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import spring_client.semestral.data_format.MusicDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class MusicClient {
    private String baseUrl;
    private RestClient musicClient;

    public MusicClient(@Value("${api.url}") String baseUrl) {
        this.baseUrl = baseUrl + "/musics";
        musicClient = RestClient.builder().baseUrl(this.baseUrl).build();
    }

    public List<MusicDto> readAllMusics() {
        return Optional.of(musicClient.get()
                .retrieve().toEntity(MusicDto[].class))
                .map(ResponseEntity::getBody)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public MusicDto readMusicById(Long id) {
        ResponseEntity<MusicDto> response = musicClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}")
                        .build(id))
                .retrieve()
                .toEntity(MusicDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return null;
    }

    public void createMusic(MusicDto musicDto) {
        musicClient.post()
                .body(musicDto)
                .retrieve()
                .toBodilessEntity();
    }
}
