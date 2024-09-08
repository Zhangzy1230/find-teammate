package com.zzy.dto;

import lombok.Data;

@Data
public class UserDTO {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 年级
     */
    private Integer grade;
}
