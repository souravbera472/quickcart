package com.org.quickcart.service;

import com.org.quickcart.exception.EmailAlreadyExistException;
import com.org.quickcart.exception.InvalidUserException;
import com.org.quickcart.exception.UserNotFoundException;
import com.org.quickcart.model.Role;
import com.org.quickcart.model.User;
import com.org.quickcart.repository.UserRepository;
import com.org.quickcart.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
            throw new EmailAlreadyExistException(user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findById(String id){
        Optional<User> tempUser = userRepository.findById(id);
        if(tempUser.isEmpty()){
            throw new UserNotFoundException("User", "ID", id);
        }
        return tempUser.get();
    }

    public User findUserByEmail(String email){
        if(userRepository.findByEmail(email).isEmpty()){
            throw new UserNotFoundException("User", "Email", email);
        }
        return userRepository.findByEmail(email).get();
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User updateUser(String id, Map<String, String> map){

        Optional<User> tempUser = userRepository.findById(id);
        if(tempUser.isEmpty()){
            throw new UserNotFoundException("User", "ID", id);
        }

        if(map.containsKey("firstName")){
            tempUser.get().setFirstName(map.get("firstName"));
        }

        if(map.containsKey("lastName")){
            tempUser.get().setLastName(map.get("lastName"));
        }

        if(map.containsKey("email")){
            if(userRepository.findByEmail(map.get("email")).isPresent()){
                throw new EmailAlreadyExistException(map.get("email"));
            }
            tempUser.get().setEmail(map.get("email"));
        }

        if(map.containsKey("password")){
            tempUser.get().setPassword(passwordEncoder.encode(map.get("password")));
        }

        if(map.containsKey("role")){
            tempUser.get().setRole(map.get("role").equalsIgnoreCase("ADMIN") ? Role.ADMIN : Role.USER);
        }

        return userRepository.save(tempUser.get());
    }

    public String deleteUser(String id){
        if(userRepository.findById(id).isEmpty()){
            throw new UserNotFoundException("User", "ID", id);
        }
        userRepository.deleteById(id);
        return "User successfully deleted";
    }

    public String login(User user){
         Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword()
        ));
         if(!authenticate.isAuthenticated()){
             throw new InvalidUserException();
         }
        return jwtUtil.generateToken(user);
    }
}
