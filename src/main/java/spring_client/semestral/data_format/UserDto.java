package spring_client.semestral.data_format;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class UserDto {
    private Long id;
    private String username;
    private String realName;
    private String userStatus;
    private String introduction;

    public static UserDto userNotFound(){
        return UserDto.builder()
                .id(-1L)
                .username("User Not Found")
                .realName("User Not Found")
                .userStatus("User Not Found")
                .introduction("User Not Found")
                .build();
    }

    public String getChangedUserStatus() {
        if(null == userStatus) return "trial";
        return userStatus.equals("membership") ? "membership" : "trial";
    }
}
