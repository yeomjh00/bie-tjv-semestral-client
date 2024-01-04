package spring_client.semestral.data_format;

import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class PostDto {
    private Long id;
    private String authorUsername;
    private Long userId;
    private String title;
    private String content;
    private Long numberOfLikes;
    private List<PictureDto> pictureDtos;
    private MusicDto musicDto;

    public static PostDto postNotFound(){
        return PostDto.builder()
                .id(-1L)
                .authorUsername("Post Not Found")
                .userId(-1L)
                .title("Post Not Found")
                .content("Post Not Found")
                .numberOfLikes(-1L)
                .build();
    }
}
