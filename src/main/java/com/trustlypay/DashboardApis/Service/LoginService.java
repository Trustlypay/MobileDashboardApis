package com.trustlypay.DashboardApis.Service;

import com.trustlypay.DashboardApis.Utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    private final String fixedUsername = "admin";
    private final String fixedPasswordHash = passwordEncoder.encode("918273");

    public LoginService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(String userName, String rawPassword) {


        if (!fixedUsername.equals(userName)) {
            return null;
        }


        if (!passwordEncoder.matches(rawPassword, fixedPasswordHash)) {
            return null;
        }


        return jwtUtil.generateToken(fixedUsername);
    }
}
