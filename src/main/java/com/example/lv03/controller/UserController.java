package com.example.lv03.controller;

import com.example.lv03.dto.login.LoginRequestDto;
import com.example.lv03.dto.MsgResponseDto;
import com.example.lv03.dto.login.SignupRequestDto;
import com.example.lv03.service.UserService;;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/user/signup")
    public MsgResponseDto signup(@Validated SignupRequestDto requestDto){
        System.out.println(requestDto);
        return userService.signup(requestDto);
    }

    @PostMapping("/user/login")
    public MsgResponseDto login(@ModelAttribute LoginRequestDto requestDto, HttpServletResponse res) {

        return userService.login(requestDto, res);
    }

}