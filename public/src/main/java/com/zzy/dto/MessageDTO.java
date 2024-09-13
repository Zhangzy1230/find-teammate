package com.zzy.dto;

import lombok.Data;

import java.util.Date;
@Data
public class MessageDTO {
    private String sendUsername;
    private String receiveUsername;
    private String content;
    private Date sendTime;
}
