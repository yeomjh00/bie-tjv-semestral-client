package spring_client.semestral.data_format;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class PostDto {
    private Long id;
    private String authorUsername;
    private Long userId;
    private String title;
    private String content;
    private Long numberOfLikes;
}
