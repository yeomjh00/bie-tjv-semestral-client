package spring_client.semestral.data_format;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class MusicDto {
    private Long id;
    private String uri;
    private String title;
    private String artist;
}
