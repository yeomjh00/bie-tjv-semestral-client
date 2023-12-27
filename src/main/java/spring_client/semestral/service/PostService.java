package spring_client.semestral.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring_client.semestral.api_client.PostClient;
import spring_client.semestral.data_format.PostDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PostService {
    private final PostClient postClient;

    @Autowired
    public PostService(PostClient postClient){
        this.postClient = postClient;
    }

    public List<PostDto> readAllPosts() {
        return postClient.readAllPosts();
    }

    public Optional<PostDto> readPostById(Long postId) {
        return Optional.ofNullable(postClient.readPostById(postId));
    }

    public void createPost(PostDto post) {
        postClient.create(post);
    }

    public PostDto updatePostFromDto(Long postId, PostDto form) {
        PostDto post = PostDto.builder()
                .id(postId)
                .title(form.getTitle())
                .content(form.getContent())
                .authorUsername(form.getAuthorUsername())
                .build();
        return postClient.updatePost(post);
    }

    public void deletePost(Long postId) {
        postClient.deletePost(postId);
    }
}
