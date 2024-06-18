package com.study.springsecuritystudy.user.dto;

import com.study.springsecuritystudy.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    private String username;
    private UserRoleEnum role;
    private String info;
    public LoginResponseDto(String username, UserRoleEnum role, String info) {
        this.username = username;
        this.role = role;
        this.info = info;
    }
}
