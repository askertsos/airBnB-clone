package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.Photo;
import com.rbbnbb.TediTry1.domain.Role;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.UserDTO;
import com.rbbnbb.TediTry1.repository.PhotoRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoService photoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public void updateUser(User user, UserDTO userDTO){
        if (Objects.nonNull(userDTO.getUsername())) user.setUsername(userDTO.getUsername());
        if (Objects.nonNull(userDTO.getPassword())) user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (Objects.nonNull(userDTO.getFirst_name())) user.setFirst_name(userDTO.getFirst_name());
        if (Objects.nonNull(userDTO.getLast_name())) user.setLast_name(userDTO.getLast_name());
        if (Objects.nonNull(userDTO.getEmail())) user.setEmail(userDTO.getEmail());
        if (Objects.nonNull(userDTO.getPhoneNumber())) user.setPhoneNumber(userDTO.getPhoneNumber());

        String photoName = userDTO.getProfilePicture();

        if (Objects.nonNull(photoName) && !photoName.isEmpty()) {
//            photoName = photoName.substring(photoName.lastIndexOf("\\") + 1);
            String filePath = user.getPhotoDirectory() + "/" + photoName;

            Optional<Photo> optionalPhoto = photoRepository.findByFilePath(filePath);

//            if (optionalPhoto.isEmpty()){
////                Photo photo = photoService.saveImage();
//            }

//
//
////            Photo photo = new Photo(filePath);
////            photoRepository.save(photo);
////
////            user.setProfilePicture(photo);
//
//            try {
//                File temp = new File(filePath);
//                File profilePhoto = new File(temp.getAbsolutePath());
//                System.out.println("full is " + profilePhoto.getPath());
//                if (profilePhoto.createNewFile()) {
//                    System.out.println("createNewFile successful");
//                    Photo previous = user.getProfilePicture();
//                    if (Objects.nonNull(previous)) photoRepository.deleteById(previous.getId());
//                    user.setProfilePicture(null);
//                    Photo profilePicture = new Photo(filePath);
//                    photoRepository.save(profilePicture);
//                    user.setProfilePicture(profilePicture);
//                    userRepository.save(user);
//                }
//            } catch (Exception e) {
//                //Do nothing
//            }
        }

        //Roles cannot be changed

        userRepository.save(user);

    }

    public User assertUserHasAuthority(User user, String authority){
        Collection<?> grantedAuthorities = user.getAuthorities();
        for (var grantedAuthority: grantedAuthorities) {
            Role role = (Role) grantedAuthority;
            if (role.getAuthority().equals(authority)) return user;
        }
        return null;
    }

    //Takes in the user id, and returns the user if they exist and have the specified authority, null otherwise
    public User assertUserHasAuthority(Long id, String authority){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();
        return assertUserHasAuthority(user,authority);
    }

    //Takes in the jwt token, and returns the user if they exist and have the specified authority, null otherwise
    public User assertUserHasAuthority(String jwt, String authority){
        Optional<User> optionalUser = getUserByJwt(jwt);
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();
        return assertUserHasAuthority(user,authority);
    }

    public Optional<User> getUserByJwt(String jwt){
        if (Objects.isNull(jwt)) return Optional.empty();
        String pureJwt = jwt;
        pureJwt = pureJwt.replaceFirst("Bearer ", "");
        Jwt decodedJWT = jwtDecoder.decode(pureJwt);
        String username = decodedJWT.getSubject();
        return userRepository.findByUsername(username);
    }



}
