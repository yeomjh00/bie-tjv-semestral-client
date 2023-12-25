package spring_client.semestral.api_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserResourceClient {
    private final RestClient userResourceClient;
    private String baseUrl;

    @Autowired
    public UserResourceClient(@Value("${api.url}") String baseUrl, RestClient userResourceClient) {
        this.baseUrl = baseUrl + "/users";
        this.userResourceClient = userResourceClient;
    }

    public boolean setPostOwner(Long id, Long postId) {
        return userResourceClient
                .get()
                .uri(baseUrl + "/{id}/posts/{postId}", id, postId)
                .retrieve()
                .toBodilessEntity()
                .getStatusCode()
                .is2xxSuccessful();
    }
}
