package io.digitallly2024.webservice.service;

import io.digitallly2024.webservice.exception.ResourceNotFoundException;
import io.digitallly2024.webservice.kafka.KafkaProducer;
import io.digitallly2024.webservice.repository.UserRepository;
import io.digitallly2024.webservice.response.AuthenticationResponse;
import io.digitallly2024.webservice.response.ResponseMessage;
import io.digitallly2024.webservice.utils.JwtUtils;
import io.digitallly2024.webservice.entity.User;
import io.digitallly2024.commonlib.domain.message.ResetPasswordMessage;
import io.digitallly2024.webservice.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final KafkaProducer kafkaProducer;

    private final Map<String, TokenInfo> tokenStore = new HashMap<>();

    @Autowired
    public AuthService(
            JwtUtils jwtUtils,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            KafkaProducer kafkaProducer) {
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public AuthenticationResponse register(UserRegistrationRequest request) {
        User user = new User(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()),
                "USER");

        User savedUser = userRepository.save(user);
        String token = jwtUtils.generateToken(savedUser);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No user found with given email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public ResponseMessage changePassword(ChangePasswordRequest request) {
        Long userId = getCurrentUser().getId();
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + userId));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return new ResponseMessage("Password changed successfully");
    }

    public ResponseMessage forgetPassword(ForgotPasswordRequest request) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(request.getEmail(), new TokenInfo(token, Instant.now().plus(Duration.ofMinutes(15))));
        kafkaProducer.sendTestMessage(new ResetPasswordMessage(request.getEmail(), token));
        return new ResponseMessage("An email has be sent with a link to reset your password");
    }

    public ResponseMessage resetPassword(ResetPasswordRequest request) {
        TokenInfo tokenInfo = tokenStore.get(request.getEmail());
        if (tokenInfo != null && request.getToken().equals(tokenInfo.token)) {
            if (Instant.now().isBefore(tokenInfo.expirationTime)) {
                User user = userRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "No user found with given email: " + request.getEmail()));
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
                tokenStore.remove(request.getEmail());
                return new ResponseMessage("Your password has been reset successfully");
            } else {
                return new ResponseMessage("Your token has expired, Please generate a new token");
            }
        } else {
            return new ResponseMessage("Invalid token provided");
        }
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal == null || principal.equals("anonymousUser") ? new User() : (User) principal;
    }

    private static class TokenInfo {
        String token;
        Instant expirationTime;

        public TokenInfo(String token, Instant expirationTime) {
            this.token = token;
            this.expirationTime = expirationTime;
        }
    }

}
