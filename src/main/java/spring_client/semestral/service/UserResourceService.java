package spring_client.semestral.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring_client.semestral.api_client.UserResourceClient;
import spring_client.semestral.data_format.PostDto;
import spring_client.semestral.data_format.UserDto;

import java.util.List;

@Slf4j
@Service
public class UserResourceService {
    private final UserResourceClient userResourceClient;

    @Autowired
    public UserResourceService(UserResourceClient userResourceClient){
        this.userResourceClient = userResourceClient;
    }

    //TODO
    public static boolean checkPostOwnership(Long id, Long postId) {
        return true;
    }

    public void deleteUserInfoByUserId(Long id) {
        userResourceClient.deleteUserInfoByUserId(id);
    }

    public List<PostDto> readLikedPosts(Long id) {
        return userResourceClient.readLikedPosts(id);
    }

    public List<PostDto> readSomeonePosts(Long id) {
        return userResourceClient.readSomeonePosts(id);
    }

    public void likePost(Long id, Long postId) {
        userResourceClient.likePost(id, postId);
    }

    public void unlikePost(Long id, Long postId) {
        userResourceClient.unlikePost(id, postId);
    }

    public void updateUserInfoByUserId(Long id, UserDto user) {
        userResourceClient.updateUserInfoByUserId(id, user);
    }
}
