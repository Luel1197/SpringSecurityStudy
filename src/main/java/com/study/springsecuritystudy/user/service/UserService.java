package com.study.springsecuritystudy.user.service;

import com.study.springsecuritystudy.security.JwtUtil;
import com.study.springsecuritystudy.user.dto.InfoResponseDto;
import com.study.springsecuritystudy.user.dto.LoginRequestDto;
import com.study.springsecuritystudy.user.dto.LoginResponseDto;
import com.study.springsecuritystudy.user.dto.SingnupRequestDto;
import com.study.springsecuritystudy.user.entity.User;
import com.study.springsecuritystudy.user.entity.UserRoleEnum;
import com.study.springsecuritystudy.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    final JwtUtil jwtUtil;

    @Autowired
    final UserRepository userRepository;
    //이거 빈에서 그냥 주는건가?
    private final PasswordEncoder passwordEncoder;

    public UserService(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void signup(SingnupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String info = requestDto.getInfo();

        //아이디 중복확인  .isPresent()  Optional<User> 이 값에 널이 있는지 없는지 체크
        Optional<User> checkUser = userRepository.findByUsername(username);
        if (checkUser.isPresent()) {
            throw new IllegalArgumentException("중복된 이름이 있습니다.");
        }

        //사용자 role 일단 지금은 어드민 없이 모든 사용자를 유저로 받는다
        UserRoleEnum role = UserRoleEnum.USER;

        //등록
        User user = new User(username, password, role, info);

        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        //아이디 확인
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다"));

        //비번확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponseDto(user.getUsername(), user.getRole(), user.getInfo());
    }

    public InfoResponseDto readInfo(HttpServletRequest req) {
        String tokenValue = jwtUtil.getTokenFromRequest(req);

        System.out.println("토큰 값 : " + tokenValue);

        if (StringUtils.hasText(tokenValue)) {
            tokenValue = jwtUtil.substringToken(tokenValue);
            System.out.println("잘라낸 토큰 값 : " + tokenValue);


            if (!jwtUtil.validateToken(tokenValue)) {
                throw new IllegalArgumentException("Token Error");

            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            System.out.println("jwt 내부 값" + info);

        }

        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

        InfoResponseDto responseDto = new InfoResponseDto(info.getSubject(), info.get("info"));
         return  responseDto;
    }
}
