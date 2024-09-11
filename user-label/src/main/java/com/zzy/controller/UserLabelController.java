package com.zzy.controller;

import com.zzy.dto.LabelDTO;
import com.zzy.dto.UserDTO;
import com.zzy.dto.UserInfo;
import com.zzy.dto.UserLabelDTO;
import com.zzy.feign.UserFeignController;
import com.zzy.result.Code;
import com.zzy.result.Result;
import com.zzy.service.UserLabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "通过用户名查询用户的所有标签")
    @GetMapping("getUserInfoByUsername/{username}")
    public Result<UserInfo> getUserInfoByUsername(@RequestHeader(value = "jwt",required = true) String jwt,
                                                  @PathVariable("username") String username){
        Result<UserDTO> result = userFeignController.getUserByUsername(jwt, username);
        if(result.getCode().equals(Code.ERROR)){
            return Result.error("没有此用户");
        }
        UserDTO userDTO = result.getData();
        Result<List<UserLabelDTO>> listResult = userLabelService.getUserLabelByUserId(userDTO.getUserId());
        if(listResult.getCode().equals(Code.ERROR)){
            return Result.error("此用户没有标签");
        }
        List<UserLabelDTO> data = listResult.getData();
        List<LabelDTO> labelDTOList = data.stream().map(userLabelDTO -> userFeignController.selectByLabelId(jwt, userLabelDTO.getLabelId()).getData()).toList();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserDTO(userDTO);
        userInfo.setLabelDTOList(labelDTOList);
        return Result.ok(userInfo);
    }

    @Operation(summary = "通过id查询用户的所有标签")
    @GetMapping("getUserInfoByUserId/{userId}")
    public Result<UserInfo> getUserInfoByUserId(@RequestHeader(value = "jwt",required = true) String jwt,
                                                  @PathVariable("userId") Integer userId){
        Result<UserDTO> result = userFeignController.getUserByUserId(jwt, userId);
        if(result.getCode().equals(Code.ERROR)){
            return Result.error("没有此用户");
        }
        UserDTO userDTO = result.getData();
        Result<List<UserLabelDTO>> listResult = userLabelService.getUserLabelByUserId(userDTO.getUserId());
        if(listResult.getCode().equals(Code.ERROR)){
            return Result.error("此用户没有标签");
        }
        List<UserLabelDTO> data = listResult.getData();
        List<LabelDTO> labelDTOList = data.stream().map(userLabelDTO -> userFeignController.selectByLabelId(jwt, userLabelDTO.getLabelId()).getData()).toList();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserDTO(userDTO);
        userInfo.setLabelDTOList(labelDTOList);
        return Result.ok(userInfo);
    }

    @Operation(summary = "通过标签id和页码（分页）查询用户")
    @GetMapping("getUserInfoByLabelIdAndPage/{labelId}/{page}")
    public Result<List<UserInfo>> getUserInfoByLabelIdAndPage(@RequestHeader("jwt") String jwt,
                                                       @PathVariable("labelId") Integer labelId,
                                                       @PathVariable("page") Integer page){
        Result<List<UserLabelDTO>> userLabelList = userLabelService.getUserInfoByLabelIdAndPage(labelId, page);
        return Result.ok(userLabelList.getData().stream().map(userLabelDTO -> {
            Result<UserInfo> userInfoByUserId = getUserInfoByUserId(jwt, userLabelDTO.getUserId());
            return userInfoByUserId.getData();
        }).toList());
    }
}
