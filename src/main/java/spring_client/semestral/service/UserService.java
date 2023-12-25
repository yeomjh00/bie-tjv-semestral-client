package spring_client.semestral.service;

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

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public List<UserDto> readAll() {
        return userClient.readAll();
    }

    public Optional<UserDto> readById(Long id) {
        userClient.setCurrentUser(id);
        return Optional.ofNullable(userClient.readCurrentUserInfo());
    }

    public void create(UserDto data) {
        userClient.create(data);
    }

    public boolean setCurrentUser(Long id) {
        userClient.setCurrentUser(id);
        UserDto currentUserInfo = userClient.readCurrentUserInfo();
        this.currentUserId = id;
        this.currentUser = currentUserInfo;
        return currentUserInfo != null;
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

    public UserDto readOne() {
        return userClient.readCurrentUserInfo();
    }

    public boolean CheckValidityAndDuplicate(UserDto editedUser) {
        if (editedUser.getUsername().equals(currentUser.getUsername())) {
            return true;
        }
        return !userClient.userExists(editedUser.getId());
    }
}
