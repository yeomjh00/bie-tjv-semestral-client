package spring_client.semestral.api_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import spring_client.semestral.data_format.UserDto;

import java.util.*;

@Component
public class UserClient {
    private RestClient userRestClient;
    private RestClient currentUserRestClient;
    private String baseUrl;

    public UserClient(@Value("${api.url}") String baseUrl) {
        this.baseUrl = baseUrl + "/users";
        userRestClient = RestClient.create(this.baseUrl);
    }

    public void setCurrentUser(Long userId) {
        currentUserRestClient = RestClient
                .builder()
                .baseUrl(baseUrl + "/{id}")
                .defaultUriVariables(java.util.Map.of("id", userId))
                .build();
    }

    public List<UserDto> readAll() { // get /users
        return Optional.of(userRestClient.get().retrieve().toEntity(UserDto[].class))
                .map(ResponseEntity::getBody)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }


    public Optional<UserDto> create(UserDto data) { // post /users
        ResponseEntity<UserDto> response = userRestClient.post()
                .body(data)
                .retrieve()
                .toEntity(UserDto.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return Optional.of(Objects.requireNonNull(response.getBody()));
        }
        return Optional.empty();
    }

    public Optional<UserDto> readCurrentUserInfo() { // get /users/{id}
        ResponseEntity<UserDto> response = currentUserRestClient.get()
                .retrieve()
                .toEntity(UserDto.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return Optional.ofNullable(response.getBody());
        }
        return Optional.empty();
    }

    public boolean userExists(Long userId) {
        setCurrentUser(userId);
        return currentUserRestClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange((request, response) -> response.getStatusCode().equals(HttpStatus.OK));
    }

    public void updateCurrentUser(UserDto data) { // put /users/{id}
        currentUserRestClient
                .put()
                .body(data)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteCurrentUser() { // delete /users/{id}
        currentUserRestClient
                .delete()
                .retrieve()
                .toBodilessEntity();
    }
}
