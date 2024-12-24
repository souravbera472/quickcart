package com.org.quickcart.service;

import com.org.quickcart.exception.*;
import com.org.quickcart.logger.QLogger;
import com.org.quickcart.model.Role;
import com.org.quickcart.entity.User;
import com.org.quickcart.repository.UserRepository;
import com.org.quickcart.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public User addUser(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            QLogger.warn("User already exist with given credential.");
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() == null ? Role.USER : user.getRole());
        QLogger.info("User added successfully.");
        return userRepository.save(user);
    }

    public User findById(String id){
        Optional<User> tempUser = userRepository.findById(id);
        if(tempUser.isEmpty()){
            QLogger.warn("User not found.");
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return tempUser.get();
    }

    public User findUserByEmail(String email){
        if(userRepository.findByEmail(email).isEmpty()){
            QLogger.warn("User not found.");
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return userRepository.findByEmail(email).get();
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User updateUser(String id, Map<String, String> map){

        Optional<User> tempUser = userRepository.findById(id);
        if(tempUser.isEmpty()){
            QLogger.warn("User not found.");
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if(map.containsKey("firstName")){
            tempUser.get().setFirstName(map.get("firstName"));
        }

        if(map.containsKey("lastName")){
            tempUser.get().setLastName(map.get("lastName"));
        }

        if(map.containsKey("email")){
            if(userRepository.findByEmail(map.get("email")).isPresent()){
                QLogger.warn("User already exist with given credential.");
                throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
            }
            tempUser.get().setEmail(map.get("email"));
        }

        if(map.containsKey("password")){
            tempUser.get().setPassword(passwordEncoder.encode(map.get("password")));
        }

        if(map.containsKey("role")){
            tempUser.get().setRole(map.get("role").equalsIgnoreCase("ADMIN") ? Role.ADMIN : Role.USER);
        }

        QLogger.info("User detail updated successfully.");
        return userRepository.save(tempUser.get());
    }

    public String deleteUser(String id){
        if(userRepository.findById(id).isEmpty()){
            QLogger.info("User not found.");
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
        QLogger.info("User deleted successfully.");
        return "User successfully deleted";
    }

    public String login(User user) {
        try {
            String password = user.getPassword();
            user = userRepository.findByEmail(user.getEmail()).get();
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), password)
            );
            return jwtUtil.generateToken(user, String.valueOf(user.getRole()));
        } catch (BadCredentialsException | AccountExpiredException | DisabledException e) {
            QLogger.warn("Invalid user credentials or account issues: " + e.getMessage());
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        } catch (AuthenticationException e) {
            QLogger.error("User is not authorized: " + e.getMessage());
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        } catch (Exception e) {
            QLogger.error("Unexpected error during login: " + e.getMessage());
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }
    }

}
