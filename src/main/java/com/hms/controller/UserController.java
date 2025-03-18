package com.hms.controller;

import com.hms.entity.AppUser;
import com.hms.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private AppUserRepository appUserRepository;

    public UserController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> createUser(@RequestBody AppUser user){
        Optional<AppUser> opUsername = appUserRepository.findByUsername(user.getUserName());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("User already taken", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        Optional<AppUser> opEmail = appUserRepository.findByEmail(user.getEmail());
        if(opEmail.isPresent()){
            return new ResponseEntity<>("Email already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        AppUser savedUser = appUserRepository.save(user);
        return new ResponseEntity<>(savedUser,HttpStatus.CREATED);
    }

    @GetMapping("/massage")
    public String getMassage(){
        return "hello";
    }
}
