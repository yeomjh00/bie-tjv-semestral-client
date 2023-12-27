package spring_client.semestral.data_format;


import lombok.*;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class UserDto {
    private Long id;
    private String username;
    private String realName;
    private String userStatus;
    private String introduction;

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                '}';
    }

    public static UserDto notFound(){
        return UserDto.builder()
                .id(-1L)
                .username("Not found")
                .realName("Not found")
                .userStatus("Not found")
                .introduction("User Not Found! Check ID.")
                .build();
    }

    public String getChangedUserStatus(){
        return this.userStatus = this.userStatus == null ? "trial" : "membership";
    }
}
