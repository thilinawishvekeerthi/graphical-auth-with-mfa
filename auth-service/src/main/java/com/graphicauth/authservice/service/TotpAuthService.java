package com.graphicauth.authservice.service;

import com.graphicauth.authservice.entity.User;
import com.graphicauth.authservice.repo.UserRepo;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@RequiredArgsConstructor
public class TotpAuthService implements ITotpAuthService{

    private final UserRepo userRepo;

    public String generateSecret() {
        SecretGenerator generator = new DefaultSecretGenerator();
        return generator.generate();
    }

    @Override
    public String getUriForImage(String secret) throws QrGenerationException{
        QrData data = new QrData.Builder()
                .label("graphic-auth-two-factor")
                .secret(secret)
                .issuer("graphic-auth")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);

        String mimeType = generator.getImageMimeType();

        return getDataUriForImage(imageData, mimeType);
    }

    @Override
    public boolean verifyCode(String code, String secret) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }

    @Override
    public boolean verifyCodeByUser(String code, String userName) {
        User user = userRepo.findByUserName(userName);
        return verify(code, user);
    }

    private boolean verify(String code, User user) {
        if(user != null && user.getTotpSecret() != null) return verifyCode(code, user.getTotpSecret());
        else return false;
    }
}
