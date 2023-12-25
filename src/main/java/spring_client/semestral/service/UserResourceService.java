package spring_client.semestral.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring_client.semestral.api_client.UserResourceClient;

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
        //TODO
    }
//
//    public void deleteUserInfoByUserId(Long id) {
//    }
}
