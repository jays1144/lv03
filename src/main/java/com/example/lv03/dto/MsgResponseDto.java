package com.example.lv03.dto;

import lombok.Getter;

@Getter
public class MsgResponseDto {
    private String msg;
    private String status;

    public MsgResponseDto(String msg, String status) {
        this.msg = msg;
        this.status = status;
    }
}
