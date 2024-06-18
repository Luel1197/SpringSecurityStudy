package com.study.springsecuritystudy.user.dto;

import lombok.Getter;

@Getter
public class InfoResponseDto {
    private String username;
    private String info;

    public InfoResponseDto(String subject, Object info) {
        this.username = subject;
        this.info = info.toString();
    }
}
