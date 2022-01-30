package com.graphicauth.authservice.service;

import com.graphicauth.authservice.entity.Image;
import com.graphicauth.authservice.entity.User;
import com.graphicauth.authservice.repo.ImageRepo;
import com.graphicauth.authservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepo imageRepo;
    private final UserRepo userRepo;

    @Override
    public Long save(MultipartFile imageFile) throws IOException {
        Image image = new Image();
        image.setName(imageFile.getName());
        image.setContent(imageFile.getBytes());
        image = imageRepo.save(image);
        return image.getId();
    }

    @Override
    public byte[] getImageById(Long Id) {
        Optional<Image> optionalImage = imageRepo.findById(Id);
        return optionalImage.isPresent() ? optionalImage.get().getContent() : null;
    }

    @Override
    public byte[] getImageByUserName(String username) {
        User user = this.userRepo.findByUserName(username);
        if(user != null && user.getImageRef() != null) return getImageById(user.getImageRef());
        return null;
    }
}
