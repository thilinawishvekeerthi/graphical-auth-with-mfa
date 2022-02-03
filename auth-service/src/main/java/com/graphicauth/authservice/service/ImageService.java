package com.graphicauth.authservice.service;

import com.graphicauth.authservice.dto.ImageResponse;
import com.graphicauth.authservice.entity.Image;
import com.graphicauth.authservice.entity.User;
import com.graphicauth.authservice.repo.ImageRepo;
import com.graphicauth.authservice.repo.UserRepo;
import dev.samstevens.totp.util.Utils;
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
        image.setMemType(imageFile.getContentType());
        image = imageRepo.save(image);
        return image.getId();
    }

    @Override
    public byte[] getImageById(Long id) {
        Optional<Image> optionalImage = imageRepo.findById(id);
        return optionalImage.isPresent() ? optionalImage.get().getContent() : null;
    }

    public Image getImage(Long id) {
        Optional<Image> optionalImage = imageRepo.findById(id);
        return optionalImage.isPresent() ? optionalImage.get() : null;
    }
    @Override
    public ImageResponse getImageByUserName(String username) {
        User user = this.userRepo.findByUserName(username);
        Image image = getImage(user.getImageRef());
        if(isImageRefExists(user)) return new ImageResponse(Utils.getDataUriForImage(image.getContent(), image.getMemType())) ;
        return null;
    }

    private boolean isImageRefExists(User user) {
        return user != null && user.getImageRef() != null;
    }
}
