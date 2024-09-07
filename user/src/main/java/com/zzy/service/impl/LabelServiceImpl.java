package com.zzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.domain.Label;
import com.zzy.result.Result;
import com.zzy.service.LabelService;
import com.zzy.mapper.LabelMapper;
import jakarta.annotation.Resource;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zzy
* @description 针对表【label(标签表)】的数据库操作Service实现
* @createDate 2024-09-06 16:19:10
*/
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label>
    implements LabelService{
    @Resource
    private LabelMapper labelMapper;
    @Resource
    private RedissonClient redissonClient;
    private static final String ALL_LABELS = "all-labels";

    @Override
    public Result selectAllLabels() {
        RList<String> list = redissonClient.getList(ALL_LABELS);
        return Result.ok(list.readAll());
    }

    @Override
    public Result adminAddLabel(String labelName) {
        Label label = new Label();
        label.setLabelName(labelName);
        int i = labelMapper.insert(label);
        if(i < 1){
            return Result.error("mysql插入失败");
        }
        RList<String> list = redissonClient.getList(ALL_LABELS);
        list.add(labelName);
        return Result.ok(label);
    }

    @Override
    public Result<Label> selectByLabelName(String labelName) {
        LambdaQueryWrapper<Label> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Label::getLabelName,labelName);
        Label label = labelMapper.selectOne(queryWrapper);
        if(label == null){
            return Result.error("没有对应的标签");
        }
        return Result.ok(label);
    }
}




