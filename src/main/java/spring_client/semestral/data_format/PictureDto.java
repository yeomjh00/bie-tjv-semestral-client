package spring_client.semestral.data_format;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PictureDto {
    private Long id;
    private String uri;
    private Integer height;
    private Integer width;
    private Long postId;
}
