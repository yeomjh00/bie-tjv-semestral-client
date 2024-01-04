package spring_client.semestral.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring_client.semestral.data_format.UserDto;
import spring_client.semestral.service.PostService;
import spring_client.semestral.service.UserResourceService;
import spring_client.semestral.service.UserService;

@Slf4j
@Controller
public class UserPageController {
    private final UserService userService;

    private final UserResourceService userResourceService;
    private final PostService postService;

    public static final String userPage = "/users/{id}";

    public static final String userPageEdit = "/users/{id}/edit";

    public static final String userWithdrawal = "/users/{id}/withdrawal";

    public static final String userPosts = "/users/{id}/myposts";

    public static final String likedByUser = "/users/{id}/likedposts";

    // @TODO: move to media controller
    public static final String userMusicLists = "/users/{id}/musiclists";

    @Autowired
    public UserPageController(UserService userService,
                              PostService postService,
                              UserResourceService userResourceService){
        this.userService = userService;
        this.userResourceService = userResourceService;
        this.postService = postService;
    }

    @GetMapping(userPage)
    public String userPage(Model model, @PathVariable Long id){
        log.info("trial: entering user edit page");
        UserDto user = userService.readById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);

        return "users/personalPage";
    }

    @GetMapping(userPageEdit)
    public String userPageEdit(Model model, @PathVariable Long id){

        if(userService.UserExists(id)){
            log.info("user id: {} entered edit page", id);
        } else{
            log.info("user id: {} not found", id);
            return "redirect:/";
        }
        UserDto user = userService.readById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Information may be changed by other users"));
        model.addAttribute("user", user);

        return "users/edit";
    }


    @PostMapping(userPageEdit)
    public String userPageEditPost(@PathVariable Long id, UserDto form){
        log.info("trial: user information modified");
        UserDto user = userService.readById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("user info for editing");
        UserDto editedUser = UserDto.builder()
                .id(id)
                .username(form.getUsername())
                .realName(user.getRealName())
                .introduction(form.getIntroduction())
                .userStatus(form.getChangedUserStatus())
                .build();

        log.info("Valid for modifying user information!");
        user.setUsername(form.getUsername());
        userResourceService.updateUserInfoByUserId(id, editedUser);
        return "redirect:/users/"+id;
    }

    @GetMapping(userWithdrawal)
    public String userPageDelete(Model model, @PathVariable Long id){
        UserDto user = userService.readById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);
        log.info("user id: {} entered withdrawal page", id);
        return "users/withdrawal";
    }

    @PostMapping(userWithdrawal)
    public String userPageDeletePost(@PathVariable Long id, @RequestParam String confirm){
        UserDto user = userService.readById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        log.info("Confirmation Statement: {}", confirm);
        log.info("Actual Statement: {}", "withdraw "+ user.getUsername());
        if (confirm.equals("withdraw "+ user.getUsername())) {
            userResourceService.deleteUserInfoByUserId(id);
            return "redirect:/";
        }
        else
            return "redirect:/users/"+id+"/withdrawal";
    }

}
