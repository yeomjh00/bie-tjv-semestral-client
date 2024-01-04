package spring_client.semestral.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring_client.semestral.data_format.PictureDto;
import spring_client.semestral.data_format.PostDto;
import spring_client.semestral.data_format.UserDto;
import spring_client.semestral.service.PictureService;
import spring_client.semestral.service.PostService;
import spring_client.semestral.service.UserResourceService;
import spring_client.semestral.service.UserService;

import java.util.List;

@Slf4j
@Controller
public class PostFuncController {
    private final PostService postService;
    private final PictureService pictureService;
    private final UserService userService;
    private final UserResourceService userResourceService;

    public static final String newPost = "/users/{id}/newpost";
    public static final String editSpecificPost = "/users/{id}/myposts/{postId}/edit";

    public static final String deleteSpecificPost = "/users/{id}/myposts/{postId}/delete";
    public static final String likePost = "/posts/{postId}/like";
    public static final String unLikePost = "/posts/{postId}/unlike";


    @Autowired
    public PostFuncController( UserService userService,
                               PostService postService,
                               UserResourceService userResourceService,
                               PictureService pictureService){
        this.postService = postService;
        this.userService = userService;
        this.userResourceService = userResourceService;
        this.pictureService = pictureService;
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

    @PostMapping(newPost)
    public String createPost(Model model,
                             @PathVariable Long id,
                             PostDto form,
                             @RequestParam(value = "unhandledPictures", required = false) String urls,
                             @RequestParam(required = false) Long songId) {
        UserDto user = userService.readById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        try{
            postService.createPost(form, user, urls, songId);
        } catch (IllegalArgumentException e) {
            return "redirect:/users/" + id;
        }
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
        String pictureUris = pictureService.getPictureUris(post.getPictureDtos());
        model.addAttribute("pictureUris", pictureUris);
        log.info("photos: {}", pictureUris);
        return "posts/edit";
    }

    @PostMapping(editSpecificPost)
    public String editSpecificPostPost(Model model,
                                       @PathVariable Long id,
                                       @PathVariable Long postId,
                                       @RequestParam String unhandledPictures,
                                       @RequestParam Long songId,
                                       PostDto form){
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (UserResourceService.checkPostOwnership(id, postId)){
            PostDto updatedPost = postService.updatePostFromDto(post, form, unhandledPictures, songId);
            return "redirect:/users/"+ id + "/myposts/" + postId;
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
    public String likePost(@PathVariable Long postId, @RequestParam Long userId){
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        log.info("user {} liked post {}", userId, postId);
        userResourceService.likePost(userId, postId);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping(likePost)
    public String unLikePost(@PathVariable Long postId, @RequestParam Long userId){
        PostDto post = postService.readPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        log.info("user {} unliked post {}", userId, postId);
        userResourceService.unlikePost(userId, postId);
        return "redirect:/posts/" + postId;
    }
}
