package com.zzy.service;

import com.zzy.domain.Label;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.dto.LabelDTO;
import com.zzy.result.Result;

import java.util.List;

/**
* @author zzy
* @description 针对表【label(标签表)】的数据库操作Service
* @createDate 2024-09-06 16:19:10
*/
public interface LabelService extends IService<Label> {
    Result<List<LabelDTO>> selectAllLabels();
    Result adminAddLabel(String labelName);
    Result<LabelDTO> selectByLabelName(String labelName);
    Result<LabelDTO> selectByLabelId(Integer labelId);
}
