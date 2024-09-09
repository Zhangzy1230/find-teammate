package com.zzy.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private UserDTO userDTO;
    private List<LabelDTO> labelDTOList;
}
