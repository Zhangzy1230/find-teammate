package com.zzy.controller;

import com.zzy.domain.Label;
import com.zzy.dto.LabelDTO;
import com.zzy.dto.UserDTO;
import com.zzy.feign.UserFeignController;
import com.zzy.request.AdminAddLabelRequest;
import com.zzy.result.Code;
import com.zzy.result.Result;
import com.zzy.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Tag(name = "label")
public class LabelController {
    @Resource
    private LabelService labelService;
    @Resource
    private UserFeignController userFeignController;

    @Operation(summary = "通过id查询标签")
    @GetMapping("selectByLabelId/{labelId}")
    public Result<LabelDTO> selectByLabelId(@RequestHeader(value = "jwt",required = true) String jwt,
                                              @PathVariable("labelId") Integer labelId){
        return labelService.selectByLabelId(labelId);
    }

    @Operation(summary = "通过名字查询标签")
    @GetMapping("selectByLabelName/{labelName}")
    public Result<LabelDTO> selectByLabelName(@RequestHeader(value = "jwt",required = true) String jwt,
                                              @PathVariable("labelName") String labelName){
        return labelService.selectByLabelName(labelName);
    }
    @Operation(summary = "查询所有标签")
    @PostMapping("selectAllLabels")
    public Result<List<LabelDTO>> selectAllLabels(@RequestHeader(value = "jwt",required = true) String jwt){
        return labelService.selectAllLabels();
    }

    @Operation(summary = "管理员添加标签")
    @PostMapping("admin/add/label")
    public Result adminAddLabel(@RequestHeader(value = "jwt",required = true) String jwt,
                                @RequestBody AdminAddLabelRequest adminAddLabelRequest){
        Result<UserDTO> result = userFeignController.getUserByJWT(jwt);
        if(result.getCode().equals(Code.ERROR)){
            return Result.error("jwt不合法");
        }
        String username = result.getData().getUserName();
        if(!"admin".equals(username)){
            return Result.error("不是管理员");
        }
        return labelService.adminAddLabel(adminAddLabelRequest.getLabelName());
    }

}
