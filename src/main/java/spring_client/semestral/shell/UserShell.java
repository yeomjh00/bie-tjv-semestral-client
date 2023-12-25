package spring_client.semestral.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import spring_client.semestral.data_format.UserDto;
import spring_client.semestral.service.UserService;

import java.util.Collection;

@ShellComponent
public class UserShell {
    private UserService userService;

    public UserShell(UserService userService) {
        this.userService = userService;
    }

    @ShellMethod
    public Collection<UserDto> listAllUsers() {
        return userService.readAll();
    }

    @ShellMethod
    public String createNewUser(String username, @ShellOption(defaultValue = "") String realName) {
        var u = new UserDto();
        u.setUsername(username);
        u.setRealName(realName);
        try {
            userService.create(u);
        } catch (ResourceAccessException e) {
            return "cannot connect to the server";
        } catch (HttpClientErrorException.Conflict e) {
            return "ERROR: username already registered";
        }
        return "created";
    }

    @ShellMethod
    public void setCurrentUser(Long userId) {
        userService.setCurrentUser(userId);
    }

    @ShellMethod
    public void updateCurrentUser(UserDto updatedUserInfo) {
        userService.updateCurrentUser(updatedUserInfo);
    }

    @ShellMethodAvailability({"update-current-user"})
    public Availability isCurrentUser() {
        if (userService.isCurrentUser())
            return Availability.available();
        else
            return Availability.unavailable("current user is not set");
    }
}
