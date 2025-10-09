package com.trustlypay.DashboardApis.Controllers;


import com.trustlypay.DashboardApis.Models.LoginRequest;
import com.trustlypay.DashboardApis.Models.LoginResponse;
import com.trustlypay.DashboardApis.Service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Login Controller", description = "APIs for user login")
@RestController
@RequestMapping("/gateway")
public class LoginController {
   private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/loginUser")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        String token = loginService.authenticate(
                loginRequest.getUserName(),
                loginRequest.getPassword()
        );

        if (token != null) {
            return ResponseEntity.ok(new LoginResponse(true, "Login Successful", token));
        } else {
            return ResponseEntity.status(401)
                    .body(new LoginResponse(false, "Invalid username or password", null));
        }
    }

    }








