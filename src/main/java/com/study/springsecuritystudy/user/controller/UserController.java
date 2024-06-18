package com.study.springsecuritystudy.user.controller;

import com.study.springsecuritystudy.security.JwtUtil;
import com.study.springsecuritystudy.user.dto.InfoResponseDto;
import com.study.springsecuritystudy.user.dto.LoginRequestDto;
import com.study.springsecuritystudy.user.dto.LoginResponseDto;
import com.study.springsecuritystudy.user.dto.SingnupRequestDto;
import com.study.springsecuritystudy.user.entity.UserRoleEnum;
import com.study.springsecuritystudy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@Slf4j(topic = "로그인 및 JWT 생성")
public class UserController {

    final UserService userService;
    final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    //회원가입 (POST /user) JWT X
    @PostMapping("/user")
    public ResponseEntity<String> signup(@RequestBody SingnupRequestDto requestDto) {
        userService.signup(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //로그인 (Post /login) JWT x
    @PostMapping("/login")
    public ResponseEntity<String> signup(@RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = userService.login(requestDto);
        return new ResponseEntity<>(jwtUtil.createToken(responseDto.getUsername(), responseDto.getRole(), responseDto.getInfo()),HttpStatus.OK);
    }

    //JWT를 이용한 UserName 반환 GET 만들기 (GET /user/info) -> JWT O
    @GetMapping("/user/info")
    public ResponseEntity<InfoResponseDto> getInfo(HttpServletRequest req) {
        InfoResponseDto responseDto = userService.readInfo(req);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //request response는 마음대로 몇개를 만드셔도 상관 없습니다.

    @PostMapping("/jwt")
    public ResponseEntity<String> createJWT(){
        String username = "박성원";
        UserRoleEnum role = UserRoleEnum.USER;
        String info = "테스트";

        return new ResponseEntity<>(jwtUtil.createToken(username, role, info), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("잘 들어 옵니다?",HttpStatus.OK);
    }
}
