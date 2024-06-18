package com.study.springsecuritystudy.user.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String username;
    private String password;
}
