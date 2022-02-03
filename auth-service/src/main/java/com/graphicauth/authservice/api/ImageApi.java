package com.graphicauth.authservice.api;

import com.graphicauth.authservice.dto.ImageResponse;
import com.graphicauth.authservice.service.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;

@RestController
@RequestMapping("api/image")
@RequiredArgsConstructor
public class ImageApi {
    private final IImageService imageService;

    @PostMapping("/upload")
    Long upload(@RequestParam MultipartFile multipartFile) throws IOException {
        return imageService.save(multipartFile);
    }

    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    Resource downloadImage(@PathVariable Long imageId) {
        byte[] image = imageService.getImageById(imageId);
        if(image == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else{
            return new ByteArrayResource(image);
        }
    }

    @GetMapping(value = "/user/{userName}")
    ResponseEntity<ImageResponse> downloadImageByUserName(@PathVariable String userName) {
        ImageResponse image = imageService.getImageByUserName(userName);
        if(image == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok(image);
        }
    }
}
