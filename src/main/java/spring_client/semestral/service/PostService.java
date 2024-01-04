package spring_client.semestral.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring_client.semestral.api_client.MusicClient;
import spring_client.semestral.api_client.PostClient;
import spring_client.semestral.data_format.MusicDto;
import spring_client.semestral.data_format.PictureDto;
import spring_client.semestral.data_format.PostDto;
import spring_client.semestral.data_format.UserDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PostService {
    private final PostClient postClient;

    private final MusicClient musicClient;

    @Autowired
    public PostService(PostClient postClient,
                       MusicClient musicClient){
        this.postClient = postClient;
        this.musicClient = musicClient;
    }

    public List<PostDto> readAllPosts() {
        return postClient.readAllPosts();
    }

    public Optional<PostDto> readPostById(Long postId) {
        return Optional.ofNullable(postClient.readPostById(postId));
    }

    public void createPost(PostDto form, UserDto user, String urls, Long songId) throws IllegalArgumentException {
        MusicDto song = null;
        if(songId != null)
            song = musicClient.readMusicById(songId);

        PostDto postDto = PostDto.builder()
                .authorUsername(user.getUsername())
                .userId(user.getId())
                .title(form.getTitle())
                .content(form.getContent())
                .musicDto(song)
                .build();
        SetPictureDtosFromUrls(postDto, urls);
        postClient.create(postDto);
    }

    public PostDto updatePostFromDto(PostDto post, PostDto form, String urls, Long songId) throws IllegalArgumentException{
        MusicDto musicDto = songId == null ? null : musicClient.readMusicById(songId);

        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setMusicDto(musicDto);
        SetPictureDtosFromUrls(post, urls);

        return postClient.updatePost(post);
    }

    public void deletePost(Long postId) {
        postClient.deletePost(postId);
    }

    private void SetPictureDtosFromUrls(PostDto form, String urls) throws IllegalArgumentException {

        if (urls == null || urls.isEmpty() || urls.isBlank()) {
            form.setPictureDtos(Collections.EMPTY_LIST);
            log.info("No pictures");
            return;
        }

        String[] pictures = urls.split("\n");
        ArrayList<PictureDto> candidatePictures = new ArrayList<>();

        for (String picture : pictures) {
            PictureDto pictureDto = new PictureDto();

            String[] pictureInfo = picture.split(";");
            if (pictureInfo.length != 3) {
                String errorStatement = "Invalid picture format respect to ;";
                log.error("{}: {}", errorStatement, picture);
                throw new IllegalArgumentException(errorStatement);
            }
            if(pictureInfo[0].trim().isEmpty() || pictureInfo[0].trim().length() > 511 )
                throw new IllegalArgumentException("Picture uri cannot be empty and exceed 511 characters");

            pictureDto.setUri(pictureInfo[0].trim());

            try {
                pictureDto.setHeight(Integer.parseInt(pictureInfo[1].trim()));
                pictureDto.setWidth(Integer.parseInt(pictureInfo[2].trim()));
            } catch (NumberFormatException e) {
                String errorStatement = "Size of picture must be Integer";
                log.error("{}: {}",errorStatement, picture);
                throw new NumberFormatException(errorStatement);
            }
            candidatePictures.add(pictureDto);
        }
        form.setPictureDtos(List.of(candidatePictures.toArray(PictureDto[]::new)));
    }
}
