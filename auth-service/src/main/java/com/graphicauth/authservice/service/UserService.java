package com.graphicauth.authservice.service;


import com.graphicauth.authservice.dto.*;
import com.graphicauth.authservice.entity.Role;
import com.graphicauth.authservice.entity.User;
import com.graphicauth.authservice.repo.RoleRepo;
import com.graphicauth.authservice.repo.UserRepo;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements  IUserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo  roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ITotpAuthService totpAuthService;

    @Override
    public User saveUser(User user) {
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        return  userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public void addUserRole(String userName, String roleName) {
        User user = getUser(userName);
        Role role = roleRepo.findByName(roleName);
        user.addRole(role);
        userRepo.save(user);
    }

    @Override
    public User getUser(String userName) {
        return userRepo.findByUserName(userName);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream().map(user -> new UserDto(user.getId(),user.getUserName(), user.getEmail(), user.isActive(), user.isMfa(), user.getRoles())).collect(Collectors.toList());
    }

    @Override
    public SignUpResponse signUpUser(SignUpRequest request) throws QrGenerationException, SQLException {
        SignUpResponse signUpResponse = null;
        User user = getUser(request.getUserName());
        if(user == null){
            user =new User();
            BeanUtils.copyProperties(request, user);
            Role role = roleRepo.findByName("APP_USER");
            user.addRole(role);
            user.setPassWord(passwordEncoder.encode(request.getPassWord()));
            user.setPassPoints(AESCipherService.encrypt(request.getPassWord()));
            user.setTotpSecret(totpAuthService.generateSecret());
            user = userRepo.save(user);
            signUpResponse = new SignUpResponse(user.isMfa(), totpAuthService.getUriForImage(user.getTotpSecret()));
        }else{
            throw new BadCredentialsException("User Already exists");
        }
        return signUpResponse;
    }

    @Override
    public ConfigDto getUserConfig(String userName) {
        User user = getUser(userName);
        if(user == null) throw  new UsernameNotFoundException("User Not Found");
        return new ConfigDto(user.getUserName(), user.getCanvasX(), user.getCanvasY());
    }

    @Override
    public Boolean resetUser(ResetUserRequest resetUserRequest) {
        User user = getUser(resetUserRequest.getUserName());
        if(user != null && user.getVerifyToken().equals(resetUserRequest.getVerifyToken())){
            user.setPassWord(passwordEncoder.encode(resetUserRequest.getPassPoints()));
            user.setPassPoints(AESCipherService.encrypt(resetUserRequest.getPassPoints()));
            user.setNumberOfPassPoints(resetUserRequest.getNumberOfPassPoints());
            user.setTolerance(resetUserRequest.getTolerance());
            user.setImageRef(resetUserRequest.getImageRef());
            user.setCanvasX(resetUserRequest.getCanvasX());
            user.setCanvasY(resetUserRequest.getCanvasY());
            userRepo.save(user);
            return true;
        }else{
            throw new BadCredentialsException("Invalid Credentials");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username);
        if(user == null) throw  new UsernameNotFoundException("User Not Found");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(), authorities);
    }
}
