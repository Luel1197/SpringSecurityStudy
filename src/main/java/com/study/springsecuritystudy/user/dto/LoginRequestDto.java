package com.study.springsecuritystudy.user.dto;

import com.study.springsecuritystudy.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String username;
    private String password;


}
