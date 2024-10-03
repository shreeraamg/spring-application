package io.digitallly2024.webservice.controller;

import com.example.app.request.*;
import io.digitallly2024.webservice.response.AuthenticationResponse;
import io.digitallly2024.webservice.response.ResponseMessage;
import io.digitallly2024.webservice.service.AuthService;
import io.digitallly2024.webservice.request.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = "/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Authentication API", description = "API for user authentication, registration and password management")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping(path = "/register")
    @PermitAll
    @Operation(summary = "User Registration")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRegistrationRequest request) {
        AuthenticationResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping(path = "/login")
    @PermitAll
    @Operation(summary = "User Login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        AuthenticationResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping(path = "/change-password")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "Change Password")
    public ResponseEntity<ResponseMessage> changePassword(@RequestBody ChangePasswordRequest request) {
        ResponseMessage response = authService.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping(path = "/forget-password")
    @PermitAll
    @Operation(summary = "Forgot Password")
    public ResponseEntity<ResponseMessage> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        ResponseMessage response = authService.forgetPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping(path = "/reset-password")
    @PermitAll
    @Operation(summary = "Reset Password")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestBody ResetPasswordRequest request) {
        ResponseMessage message = authService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
