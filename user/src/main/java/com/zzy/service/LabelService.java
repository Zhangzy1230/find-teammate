package com.zzy.service;

import com.zzy.domain.Label;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.result.Result;

/**
* @author zzy
* @description 针对表【label(标签表)】的数据库操作Service
* @createDate 2024-09-06 16:19:10
*/
public interface LabelService extends IService<Label> {
    Result selectAllLabels();
    Result adminAddLabel(String labelName);
    Result<Label> selectByLabelName(String labelName);
}
