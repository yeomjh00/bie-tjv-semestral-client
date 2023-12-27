package spring_client.semestral.api_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import spring_client.semestral.data_format.PostDto;
import spring_client.semestral.data_format.UserDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class UserResourceClient {
    private RestClient userPostClient;

    private String baseUrl;

    @Autowired
    public UserResourceClient(@Value("${api.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.userPostClient = RestClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void setCurrentUser(Long userId) {

    }

    public List<PostDto> readSomeonePosts(Long id) {
        return Optional.of(userPostClient.get().uri(
                uriBuilder -> uriBuilder
                        .path("/posts/owned")
                        .queryParam("user_id", id)
                        .build())
                .retrieve()
                .toEntity(PostDto[].class))
                .map(ResponseEntity::getBody)
                .map(Arrays::asList).orElse(Collections.emptyList());
    }

    public List<PostDto> readLikedPosts(Long id) {
        return Optional.of(userPostClient.get().uri(
                                uriBuilder -> uriBuilder
                                        .path("/posts/liked")
                                        .queryParam("user_id", id)
                                        .build())
                        .retrieve()
                        .toEntity(PostDto[].class))
                .map(ResponseEntity::getBody)
                .map(Arrays::asList).orElse(Collections.emptyList());
    }

    public boolean setPostOwner(Long id, Long postId) {
        return userPostClient
                .get()
                .uri(baseUrl + "/{id}/posts/{postId}", id, postId)
                .retrieve()
                .toBodilessEntity()
                .getStatusCode()
                .is2xxSuccessful();
    }

    public void updateUserInfoByUserId(Long userId, UserDto userDto) {
        userPostClient
                .put()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/{id}")
                        .build(userId))
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDto)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteUserInfoByUserId(Long userId) {
        userPostClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/{id}")
                        .build(userId))
                .retrieve()
                .toBodilessEntity();
    }

    public void likePost(Long userId, Long postId) {
        userPostClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/posts/{postId}/like")
                        .queryParam("user_id", userId)
                        .build(postId))
                .retrieve()
                .toBodilessEntity();
    }

    public void unlikePost(Long userId, Long postId) {
        userPostClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/posts/{postId}/like")
                        .queryParam("user_id", userId)
                        .build(postId))
                .retrieve()
                .toBodilessEntity();
    }
}
