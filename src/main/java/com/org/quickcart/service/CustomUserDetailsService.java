package com.org.quickcart.service;

import com.org.quickcart.entity.User;
import com.org.quickcart.exception.CustomException;
import com.org.quickcart.exception.ErrorCode;
import com.org.quickcart.repository.UserRepository;
import com.org.quickcart.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isEmpty()){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return new CustomUserDetails(user.get());
    }
}
