package com.petal.oauth2.auth;


import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTest {

    private PasswordEncoder passwordEncoder;

    @Test
    public void passwordMatch(){

        passwordEncoder = new BCryptPasswordEncoder();

        String userInputPassword = "123456";
        String correctPassword = "{bcrypt}$2a$10$RpFJjxYiXdEsAGnWp/8fsOetMuOON96Ntk/Ym2M/RKRyU0GZseaDC";

        System.out.println(passwordEncoder.matches(userInputPassword,correctPassword));

    }


}
