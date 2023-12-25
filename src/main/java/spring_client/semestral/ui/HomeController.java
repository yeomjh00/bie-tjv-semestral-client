package spring_client.semestral.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring_client.semestral.data_format.UserDto;
import spring_client.semestral.service.UserService;

@Slf4j
@Controller
public class HomeController {
    private final UserService userService;

    private static final String homePage = "/";
    private static final String userPage = "/users/{id}";

    @Autowired
    public HomeController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(homePage)
    public String home(){
        return "home";
    }

    @PostMapping(homePage)
    public String userPage(@RequestParam("postedId") Long postedId){
        log.info("given id: {}", postedId);
        UserDto user = userService.readById(postedId)
                        .orElseThrow(() -> new IllegalArgumentException("User not Found"));
        return "redirect:users/"+user.getId();
    }
}
