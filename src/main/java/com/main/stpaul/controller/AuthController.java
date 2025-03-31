package com.main.stpaul.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.Exceptions.DuplicateEntityException;
import com.main.stpaul.Exceptions.EntityNotFoundException;
import com.main.stpaul.Exceptions.UnauthorizeException;
import com.main.stpaul.dto.ResponseDTO.LoginResponse;
import com.main.stpaul.dto.ResponseDTO.OtpResponse;
import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.dto.request.LoginRequest;
import com.main.stpaul.dto.request.RegisterRequest;
import com.main.stpaul.dto.request.ResetPassword;
import com.main.stpaul.dto.request.VarifyOpt;
import com.main.stpaul.entities.Session;
import com.main.stpaul.entities.User;
import com.main.stpaul.jwtSecurity.CustomerUserDetail;
import com.main.stpaul.jwtSecurity.JwtProvider;
import com.main.stpaul.mapper.UserMapper;
import com.main.stpaul.services.impl.OtpSerivceImpl;
import com.main.stpaul.services.impl.SessionServiceImpl;
import com.main.stpaul.services.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:5173/", "http://localhost:5174/" })
public class AuthController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private CustomerUserDetail CustomerUserDetail;

    @Autowired
    private OtpSerivceImpl otpSerivceImpl;

    @Autowired
    private SessionServiceImpl sessionServiceImpl;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody RegisterRequest request) throws Exception {
        User user = this.userServiceImpl.getUserByEmail(request.getEmail());
        if (user != null) {
            throw new DuplicateEntityException("user already present !");
        }
        try {
            user = userMapper.toUser(request);
            user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
            this.userServiceImpl.addUser(user);

            SuccessResponse response = new SuccessResponse(HttpStatus.CREATED, 201, "Register Successfully !");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest request) throws Exception {
        User user = new User();
        user = this.userServiceImpl.getUserByEmail(request.getEmail());
        if (user == null) {
            throw new EntityNotFoundException("User Not Found ");
        }
        boolean isMatchPassword = new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword());

        if (!isMatchPassword) {
            throw new UnauthorizeException("email or password invalid");
        }

        try {
            Authentication authentication = authentication(request.getEmail(), request.getPassword());
            Session session = JwtProvider.generateJwtToken(authentication);
            user.setLoginDate(LocalDateTime.now());
            user = this.userServiceImpl.updateUser(user);

            session.setUser(user);
            sessionServiceImpl.addSession(session);

            LoginResponse response = LoginResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .statusCode(200)
                    .token(session.getToken())
                    .message("login seccessful !")
                    .user(userMapper.toUserResponse(user))
                    .build();

            return ResponseEntity.of(Optional.of(response));

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Authentication authentication(String email, String password) {
        UserDetails userDetails = this.CustomerUserDetail.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UsernameNotFoundException("Invalid credentials ");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@RequestHeader("Authorization") String jwt) throws Exception {
        try {
            Session session = this.sessionServiceImpl.getSessionByToken(jwt.substring(7));
            if (session == null) {
                throw new EntityNotFoundException("session not found !");
            }
            session.setActive(false);
            session.setLogoutAt(LocalDateTime.now());
            session.setDelete(true);
            session.setDeleteDate(LocalDateTime.now());
            this.sessionServiceImpl.addSession(session);

            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "logout Successful ");
            return ResponseEntity.of(Optional.of(response));

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/send-otp/{email}")
    public ResponseEntity<?> sendOpt(@PathVariable("email") String email) throws Exception {
        User user = this.userServiceImpl.getUserByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("user not found !");
        }
        try {
            OtpResponse response = OtpResponse.builder()
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("opt sended successfully")
                    .opt(otpSerivceImpl.sendOtp(email)).build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> varifyOtp(@RequestBody VarifyOpt request) throws Exception {
        User user = this.userServiceImpl.getUserByEmail(request.getEmail());
        if (user == null) {
            throw new EntityNotFoundException("user not found !");
        }
        try {
            if (!otpSerivceImpl.varifyOtp(request.getEmail(), request.getOpt())) {
                throw new BadRequestException("Invalid otp !");
            }
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "otp sended Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassword request) throws Exception {
        User user = this.userServiceImpl.getUserByEmail(request.getEmail());
        if (user == null) {
            throw new EntityNotFoundException("user not found !");
        }
        try {
            if (!otpSerivceImpl.resetPassword(request.getEmail(), request.getOpt(), request.getPassword())) {
                throw new BadRequestException("Invalid otp !");
            }
            SuccessResponse response = new SuccessResponse(HttpStatus.OK, 200, "Password Reset Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
