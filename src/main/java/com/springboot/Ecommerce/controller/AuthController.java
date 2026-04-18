package com.springboot.Ecommerce.controller;

import com.springboot.Ecommerce.model.AppRole;
import com.springboot.Ecommerce.model.Role;
import com.springboot.Ecommerce.model.User;
import com.springboot.Ecommerce.repository.RoleRepository;
import com.springboot.Ecommerce.repository.UserRepository;
import com.springboot.Ecommerce.security.request.LoginRequest;
import com.springboot.Ecommerce.security.request.SignupRequest;
import com.springboot.Ecommerce.security.response.MessageResponse;
import com.springboot.Ecommerce.security.response.UserInfoResponse;
import com.springboot.Ecommerce.security.jwt.JwtUtils;
import com.springboot.Ecommerce.security.sevices.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        }catch (Exception e){
            Map<String,Object> map = new HashMap<>();
            map.put("message","bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getId(),userDetails.getUsername(),roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(userInfoResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken"));
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: this email is already registered"));
        }
        User user = new User(signupRequest.getUsername(),signupRequest.getEmail(),passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> usrRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if(usrRoles == null){
            Role role = roleRepository.findByRoleName(AppRole.ROLE_USER);
            System.out.println(role);
            roles.add(role);
        }else{
            usrRoles.forEach(role -> {
                switch (role){
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN);
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER);
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER);
                        roles.add(userRole);
                }
            });
        }
        System.out.println(roles);
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/username")
    public String getCurrentUsername(Authentication authentication){
        if(authentication != null){
            return authentication.getName();
        }else{
            return "Null";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList());
        UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getId(),userDetails.getUsername(),roles);
        return new ResponseEntity<>(userInfoResponse,HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(){
        ResponseCookie cookie = jwtUtils.generateCleanCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body("Signed Out successfully");
    }
}
