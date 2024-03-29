package spring_client.semestral.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring_client.semestral.data_format.PostDto;
import spring_client.semestral.data_format.UserDto;
import spring_client.semestral.service.PostService;
import spring_client.semestral.service.UserResourceService;
import spring_client.semestral.service.UserService;

@Slf4j
@Controller
public class PostViewController {

    private final PostService postService;
    private final UserService userService;

    private final UserResourceService userResourceService;

    public static final String allPosts = "/posts";

    public static final String specificPost = "/posts/{postId}";

    public static final String allMyPosts = "/users/{id}/myposts";
    public static final String mySpecificPost = "/users/{id}/myposts/{postId}";
    public static final String likedPosts = "/users/{id}/likedposts";

    @Autowired
    public PostViewController(UserService userService,
                              PostService postService,
                              UserResourceService userResourceService){
        this.userService = userService;
        this.postService = postService;
        this.userResourceService = userResourceService;
    }

    @GetMapping(allPosts)
    public String viewAllPosts(Model model){
        model.addAttribute("posts", postService.readAllPosts());
        log.info("Viewing all posts");
        return "posts/postList";
    }

    @GetMapping(specificPost)
    public String viewSpecificPost(Model model, @PathVariable Long postId){
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        model.addAttribute("post", post);
        log.info("Viewing post with id: {}, likes: {}, pics: {}, music id: {}",
                post.getId(), post.getNumberOfLikes(), post.getPictureDtos(), post.getMusicDto());
        return "posts/viewPost";
    }

    @GetMapping(mySpecificPost)
    public String viewMySpecificPost(Model model, @PathVariable Long id, @PathVariable Long postId){
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        UserDto user = userService.readById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        log.info("Viewing post with id: {}, likes: {}, pics: {}, music id: {}",
                post.getId(), post.getNumberOfLikes(), post.getPictureDtos(), post.getMusicDto());
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        log.info("Viewing specific post");
        return "posts/viewMyPost";
    }

    @GetMapping(allMyPosts)
    public String viewAllMyPosts(Model model, @PathVariable Long id){
        log.info("1");
        UserDto user = userService.readById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        log.info("2");
        //TODO: Change into My Posts
        var myposts = userResourceService.readSomeonePosts(id);
        log.info("3");
        model.addAttribute("user", user);
        model.addAttribute("posts", myposts);
        log.info("Viewing all my posts");
        return "/users/myposts";
    }

    @GetMapping(likedPosts)
    public String viewLikedPosts(Model model, @PathVariable Long id){
        UserDto user = userService.readById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        model.addAttribute("user", user);
        //TODO: Change into Liked Posts
        model.addAttribute("posts", userResourceService.readLikedPosts(id));
        log.info("Viewing all my posts");
        return "posts/postList";
    }
}
