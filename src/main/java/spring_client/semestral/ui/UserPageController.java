package spring_client.semestral.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring_client.semestral.data_format.UserDto;
import spring_client.semestral.service.PostService;
import spring_client.semestral.service.UserResourceService;
import spring_client.semestral.service.UserService;

import java.util.Optional;

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

    //TODO: 1. Post -> Success/Failure  2. Check -> Reserve -> update
    @PostMapping(userPageEdit)
    public String userPageEditPost(@PathVariable Long id, UserDto form){
        UserDto user = userService.readById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserDto editedUser = UserDto.builder()
                .username(form.getUsername())
                .introduction(form.getIntroduction())
                .realName(user.getRealName())
                .build();

        if(userService.CheckValidityAndDuplicate(editedUser)){
            log.info("Valid for modifying user information!");
//            user.setUsername(form.getUsername());
//            if (null == form.getUserStatus()){
//                user.setUserStatusTrial();
//            } else{
//                user.setUserStatusMembership();
//            }
//            userResourceService.updateUserInfoByUserId(id, user);

            return "redirect:/users/"+id;
        }
        log.info("Invalid for modifying user information!");
        return "redirect:/users/"+id+"/edit";
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
