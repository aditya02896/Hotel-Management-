package com.hms.controller;
import com.hms.entity.AppUser;
import com.hms.payload.LoginDto;
import com.hms.repository.AppUserRepository;
import com.hms.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private AppUserRepository appUserRepository;
    private final UserService userService;

    public UserController(AppUserRepository appUserRepository, UserService userService) {
        this.appUserRepository = appUserRepository;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody AppUser user) {
        Optional<AppUser> opUsername = appUserRepository.findByUsername(user.getUserName());
        if (opUsername.isPresent()) {
            return new ResponseEntity<>("User already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<AppUser> opEmail = appUserRepository.findByEmail(user.getEmail());
        if (opEmail.isPresent()) {
            return new ResponseEntity<>("Email already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String encryptedPasword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5));
        user.setPassword(encryptedPasword);
        AppUser savedUser = appUserRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/massage")
    public String getMassage() {
        return "hello";
    }

    //http://localhost:8080/login

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto) {
        boolean token = userService.verifyLogin(dto);
        if (token){
            return new ResponseEntity<>("User loggedin", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Invalid username/password",HttpStatus.FORBIDDEN);
        }
    }

    }

