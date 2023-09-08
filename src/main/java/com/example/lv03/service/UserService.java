package com.example.lv03.service;

import com.example.lv03.dto.login.LoginRequestDto;
import com.example.lv03.dto.MsgResponseDto;
import com.example.lv03.dto.login.SignupRequestDto;
import com.example.lv03.entity.User;
import com.example.lv03.entity.UserRoleEnum;
import com.example.lv03.jwt.JwtUtil;
import com.example.lv03.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public MsgResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        String checkAdmin = requestDto.getAdmin().toLowerCase();
        UserRoleEnum role = UserRoleEnum.USER;
        // 사용자 role확인
       if(checkAdmin.equals("admin")){
           role =UserRoleEnum.ADMIN;
       }

        // 사용자 등록
        User user = new User(username, password,role);
        userRepository.save(user);
        return new MsgResponseDto("회원가입 성공","200");
    }

    public MsgResponseDto login(LoginRequestDto requestDto, HttpServletResponse res) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("등록된 사용자가 없음")
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 다르다");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        String token = jwtUtil.createToken(user.getUsername(),user.getRole());
        jwtUtil.addJwtToCookie(token,res);

        return new MsgResponseDto("로그인 성공","200");
    }
}