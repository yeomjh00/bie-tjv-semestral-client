package spring_client.semestral.data_format;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class MusicListDto {
    private Long id;
    private String listName;
    private String ownerUsername;
    private Long ownerId;
    private String description;
    private List<MusicDto> track;
}