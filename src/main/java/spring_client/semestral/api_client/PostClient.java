package spring_client.semestral.api_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import spring_client.semestral.data_format.PostDto;
import spring_client.semestral.data_format.UserDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PostClient {

    private RestClient postRestClient;

    private RestClient currentPostRestClient;

    private String baseUrl;

    public PostClient(@Value("${api.url}") String baseUrl) {
        this.baseUrl = baseUrl + "/posts";
        postRestClient = RestClient.create(this.baseUrl);
    }

    public void setCurrentPost(Long postId) {
        currentPostRestClient = RestClient
                .builder()
                .baseUrl(baseUrl + "/{id}")
                .defaultUriVariables(java.util.Map.of("id", postId))
                .build();
    }

    public List<PostDto> readAllPosts() { // get /posts
        return Optional.of(postRestClient.get().retrieve().toEntity(PostDto[].class))
                .map(ResponseEntity::getBody)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public PostDto readPostById(Long postId) { // get /posts/{id}
        setCurrentPost(postId);
        return currentPostRestClient.get().retrieve().toEntity(PostDto.class).getBody();
    }

    public void create(PostDto data) { // post /posts
        postRestClient.post()
                .body(data)
                .retrieve()
                .toBodilessEntity();
    }
}
