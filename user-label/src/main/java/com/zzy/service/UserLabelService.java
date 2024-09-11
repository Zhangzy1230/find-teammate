package com.zzy.service;

import com.zzy.domain.UserLabel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.dto.UserLabelDTO;
import com.zzy.result.Result;

import java.util.List;

/**
* @author zzy
* @description 针对表【user_label(用户-标签表)】的数据库操作Service
* @createDate 2024-09-08 19:58:17
*/
public interface UserLabelService extends IService<UserLabel> {
    Result addLabel(Integer labelId,Integer userId);
    Result deleteLabel(Integer labelId,Integer userId);
    Result<List<UserLabelDTO>> getUserLabelByUserId(Integer userId);
    Result<List<UserLabelDTO>> getUserInfoByLabelIdAndPage(Integer labelId,Integer page);
}
