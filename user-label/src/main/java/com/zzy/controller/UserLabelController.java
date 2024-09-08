package com.zzy.controller;

import com.zzy.dto.LabelDTO;
import com.zzy.dto.UserDTO;
import com.zzy.feign.UserFeignController;
import com.zzy.result.Code;
import com.zzy.result.Result;
import com.zzy.service.UserLabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@Tag(name = "user-label")
public class UserLabelController {
    @Resource
    private UserLabelService userLabelService;
    @Resource
    private UserFeignController userFeignController;

    @Operation(summary = "为自己添加标签")
    @PostMapping("addLabel/{labelName}")
    public Result addLabel(@RequestHeader("jwt") String jwt,
                           @PathVariable("labelName") String labelName){
        Result<UserDTO> userDTOResult = userFeignController.getUserByJWT(jwt);
        if(userDTOResult.getCode().equals(Code.ERROR)){
            return userDTOResult;
        }
        Result<LabelDTO> labelDTOResult = userFeignController.selectByLabelName(jwt,labelName);
        if(labelDTOResult.getCode().equals(Code.ERROR)){
            return labelDTOResult;
        }
        return userLabelService.addLabel(labelDTOResult.getData().getLabelId(),userDTOResult.getData().getUserId());
    }

    @Operation(summary = "为自己删除标签")
    @DeleteMapping("deleteLabel/{labelName}")
    public Result deleteLabel(@RequestHeader("jwt") String jwt,
                              @PathVariable("labelName") String labelName){
        Result<UserDTO> userDTOResult = userFeignController.getUserByJWT(jwt);
        if(userDTOResult.getCode().equals(Code.ERROR)){
            return userDTOResult;
        }
        Result<LabelDTO> labelDTOResult = userFeignController.selectByLabelName(jwt,labelName);
        if(labelDTOResult.getCode().equals(Code.ERROR)){
            return labelDTOResult;
        }
        return userLabelService.deleteLabel(labelDTOResult.getData().getLabelId(),userDTOResult.getData().getUserId());
    }
}
