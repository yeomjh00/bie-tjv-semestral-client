package spring_client.semestral.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring_client.semestral.data_format.PostDto;
import spring_client.semestral.data_format.UserDto;
import spring_client.semestral.service.PostService;
import spring_client.semestral.service.UserResourceService;
import spring_client.semestral.service.UserService;

@Slf4j
@Controller
public class PostFuncController {
    private final PostService postService;
    private final UserService userService;
    private final UserResourceService userResourceService;

    public static final String newPost = "/users/{id}/newpost";
    public static final String editSpecificPost = "/users/{id}/myposts/{postId}/edit";

    public static final String deleteSpecificPost = "/users/{id}/myposts/{postId}/delete";
    public static final String likePost = "/posts/{postId}/like";
    public static final String unLikePost = "/posts/{postId}/unlike";


    @Autowired
    public PostFuncController(PostService postService, UserService userService, UserResourceService userResourceService){
        this.postService = postService;
        this.userService = userService;
        this.userResourceService = userResourceService;
    }

    @GetMapping(newPost)
    public String newPost(Model model, @PathVariable Long id){
        log.info("Creating new post");
        UserDto user = userService.readById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        model.addAttribute("user", user);
        return "posts/newpost";
    }

    //@TODO: Add Media
    @PostMapping(newPost)
    public String createPost(Model model, @PathVariable Long id, PostDto form) {
        UserDto user = userService.readById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        PostDto post = PostDto.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .authorUsername(user.getUsername())
                .userId(user.getId())
                .build();
        postService.createPost(post);
        log.info("Creating new post");
        return "redirect:/users/" + id;
    }

    @GetMapping(editSpecificPost)
    public String editSpecificPost(Model model, @PathVariable Long id, @PathVariable Long postId){
        log.info("Editing specific post");
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        UserDto user = userService.readById(id)
                .orElseThrow(() -> new IllegalArgumentException("Please Access By correct id"));
        model.addAttribute("post", post);
        model.addAttribute("user", user);
        return "posts/edit";
    }

    //TODO
    @PostMapping(editSpecificPost)
    public String editSpecificPostPost(Model model, @PathVariable Long id, @PathVariable Long postId, PostDto form){
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (UserResourceService.checkPostOwnership(id, postId)){
            PostDto updatedPost = postService.updatePostFromDto(postId, form);
            return "redirect:/posts/" + postId;
        }
        return "redirect:/users/" + id + "/myposts";
    }


    @GetMapping(deleteSpecificPost)
    public String deleteSpecificPost(Model model, @PathVariable Long id, @PathVariable Long postId){
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (UserResourceService.checkPostOwnership(id, postId)){
            postService.deletePost(postId);
            return "redirect:/users/" + id + "/myposts";
        }
        return "redirect:/users/" + id + "/myposts";
    }

    @PostMapping(likePost)
    public String likePost(Model model, @PathVariable Long postId, @RequestParam Long userId){
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        userResourceService.likePost(postId, userId);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping(likePost)
    public String unLikePost(Model model, @PathVariable Long postId, @RequestParam Long userId){
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        userResourceService.unlikePost(userId, postId);
        return "redirect:/posts/" + postId;
    }
}
