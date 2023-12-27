package spring_client.semestral.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import spring_client.semestral.data_format.UserDto;
import spring_client.semestral.service.UserService;

import java.util.List;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;
    public static final String userRegister = "/users/new";
    public static final String allUsers = "/users/userList";
    public static final String welcomeNewUser = "/users/welcomeNewUser";

    public static final String userPrivate = "/users/{id}";

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping(userRegister)
    public String createForm(Model model){
        return userRegister;
    }

    @PostMapping(userRegister)
    public String createUser(UserDto form, Model model){
        UserDto user = UserDto.builder()
                        .username(form.getUsername())
                        .realName(form.getRealName())
                        .introduction(form.getIntroduction())
                        .userStatus(form.getChangedUserStatus())
                        .build();

        log.info("New user with username: {}, realname: {}, introduction: {}, status: {}",
                user.getUsername(), user.getRealName(), user.getIntroduction(), user.getChangedUserStatus());
        UserDto response = userService.create(user);
        model.addAttribute("user", response);
        return welcomeNewUser;
    }

    @GetMapping(allUsers)
    public String userList(Model model){
        List<UserDto> users = userService.readAll();
        model.addAttribute("users", users);
        return allUsers;
    }
}
