package com.zzy.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest implements Serializable {
    private String sendUsername;
    private String receiveUsername;
    private String content;
}
