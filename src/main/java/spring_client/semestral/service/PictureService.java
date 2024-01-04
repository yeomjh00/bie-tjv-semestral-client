package spring_client.semestral.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import spring_client.semestral.api_client.PictureClient;
import spring_client.semestral.data_format.PictureDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PictureService {
    private PictureClient pictureClient;

    @Autowired
    public PictureService(PictureClient pictureClient) {
        this.pictureClient = pictureClient;
    }

    public String getPictureUris(List<PictureDto> pictureDtos) {
        if (pictureDtos == null || pictureDtos.isEmpty()) {
            return null;
        }
        ArrayList<String> uris = new ArrayList<>();
        for (PictureDto dto : pictureDtos) {
            String uri = dto.getUri() + ";" + dto.getHeight() + ";" + dto.getWidth();
            uris.add(uri);
        }
        return uris.stream().reduce((a, b) -> a + "\n" + b).get();
    }
}
