package spring_client.semestral.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring_client.semestral.api_client.UserClient;
import spring_client.semestral.data_format.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserClient userClient;
    private Long currentUserId;
    private UserDto currentUser;

    @Autowired
    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public List<UserDto> readAll() {
        return userClient.readAll();
    }

    public Optional<UserDto> readById(Long id) {
        userClient.setCurrentUser(id);
        return userClient.readCurrentUserInfo();
    }

    public UserDto create(UserDto data) {
        Optional<UserDto> user = userClient.create(data);
        return user.orElseGet(UserDto::userNotFound);
    }

    public boolean setCurrentUser(Long id) {
        userClient.setCurrentUser(id);
        Optional<UserDto> currentUserInfo = userClient.readCurrentUserInfo();
        this.currentUserId = id;
        currentUserInfo.ifPresent(userDto -> this.currentUser = userDto);
        return currentUserInfo.isPresent();
    }

    public boolean isCurrentUser() {
        return currentUserId != null && currentUser != null;
    }

    public boolean UserExists(Long id) { return userClient.userExists(id); }

    public void updateCurrentUser(UserDto updatedUserInfo) {
        if (isCurrentUser()) {
            userClient.updateCurrentUser(updatedUserInfo);
        }
    }

    public boolean CheckValidityAndDuplicate(UserDto request) {
        if (request.getUsername().equals(currentUser.getUsername())) {
            return true;
        }
        return !userClient.userExists(request.getId());
    }
}
