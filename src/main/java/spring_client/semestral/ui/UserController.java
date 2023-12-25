package spring_client.semestral.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import spring_client.semestral.data_format.UserDto;
import spring_client.semestral.service.UserService;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;
    public static final String userRegister = "/users/new";
    public static final String allUsers = "/users/userList";
    public static final String welcomeNewUser = "/users/welcomeNewUser";

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
        UserDto user = new UserDto();
        user.setRealName(form.getRealName());
        user.setUsername(form.getUsername());
        user.setIntroduction(form.getIntroduction());
        user.setUserStatus(form.getUserStatus());
        user.setUserStatusByValue();
        userService.create(user);
        log.info("User saved successfully");
        model.addAttribute("user", user);
        return welcomeNewUser;
    }

    @GetMapping(allUsers)
    public String userList(Model model){
        var users = userService.readAll();
        model.addAttribute("users", users);
        return allUsers;
    }
}
