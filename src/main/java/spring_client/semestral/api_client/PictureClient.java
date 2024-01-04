package spring_client.semestral.api_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PictureClient {
    private String basUrl;
    private RestClient pictureClient;

    public PictureClient(@Value("${api.url}") String baseUrl) {
        this.basUrl = baseUrl;
        pictureClient = RestClient.builder().baseUrl(baseUrl).build();
    }

}
