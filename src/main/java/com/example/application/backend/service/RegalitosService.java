package com.example.application.backend.service;

import com.example.application.backend.Role;
import com.example.application.backend.entity.Friend;
import com.example.application.backend.entity.User;
import com.example.application.backend.repository.FriendRepository;
import com.example.application.backend.repository.UserRepository;
//import jakarta.transaction.Transactional;
import com.example.config.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.example.config.email.TokenGenerator;
import com.example.config.UserDetailsServiceImpl;
import com.example.config.SecurityConfig;


import java.util.ArrayList;
import java.util.List;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.example.config.UserDetailsServiceImpl.getAuthorities;

@Service
public class RegalitosService implements UserDetailsService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private User currentUser;


    @Autowired
    public RegalitosService(UserRepository userRepository,
                            FriendRepository friendRepository,
                            EmailService emailService,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.emailService = emailService;
    }

    ////////////////////////////////////////SERVICE REGISTER///////////////////////////////////////////////////////

    @Transactional
    public void registerUser(String firstname, String lastname, String username, String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setHashedPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setEnabled(false);
        String token = TokenGenerator.generateToken();
        user.setConfirmationToken(token);


        userRepository.save(user);

        emailService.sendConfirmationEmail(user.getEmail(), token);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    ///////////////////////////////////////SERVICE LOGIN/////////////////////////////////
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }else{
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getHashedPassword(),
                    getAuthorities(user));
                    //.password(user.getHashedPassword())
//                    .roles("USER")
//                    .build();
        }

        //List<GrantedAuthority> authorities = new ArrayList<>();
        //authorities.add(new SimpleGrantedAuthority("USER"));

    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    public boolean authenticateb(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if(passwordEncoder.matches(password, userDetails.getPassword())) {
            currentUser=userRepository.findByUsername(username);
            return true;
        }
        else
            return false;
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getHashedPassword())) {
            return user;
        }
        return null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}









