package com.graphicauth.authservice.service;

import com.graphicauth.authservice.dto.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {
    Long save(MultipartFile imageFile) throws IOException;

    byte[] getImageById(Long id);

    ImageResponse getImageByUserName(String username);
}
