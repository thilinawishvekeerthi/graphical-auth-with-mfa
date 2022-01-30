package com.graphicauth.authservice.service;

import com.graphicauth.authservice.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {
    Long save(MultipartFile imageFile) throws IOException;

    byte[] getImageById(Long Id);

    byte[] getImageByUserName(String username);
}
