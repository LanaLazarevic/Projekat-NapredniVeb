package com.example.raf.napredni.veb.projekat.services;

import com.example.raf.napredni.veb.projekat.model.User;
import com.example.raf.napredni.veb.projekat.repositories.UserRepository;
import com.example.raf.napredni.veb.projekat.utils.MyUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        user.getPermissions().size();
        return new MyUserDetails(user);
    }
}
