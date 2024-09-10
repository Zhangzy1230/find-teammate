package com.zzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.domain.UserLabel;
import com.zzy.dto.UserLabelDTO;
import com.zzy.feign.UserFeignController;
import com.zzy.result.Result;
import com.zzy.service.UserLabelService;
import com.zzy.mapper.UserLabelMapper;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author zzy
* @description 针对表【user_label(用户-标签表)】的数据库操作Service实现
* @createDate 2024-09-08 19:58:17
*/
@Service
public class UserLabelServiceImpl extends ServiceImpl<UserLabelMapper, UserLabel>
    implements UserLabelService{
    @Resource
    private UserLabelMapper userLabelMapper;
//    @Resource
//    private UserFeignController userFeignController;
    @Resource
    private RedissonClient redissonClient;
    @Override
    public Result addLabel(Integer labelId, Integer userId) {
        LambdaQueryWrapper<UserLabel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLabel::getUserId,userId).eq(UserLabel::getLabelId,labelId);
        RLock rLock = redissonClient.getLock(labelId + ":" + userId);
        try{
            rLock.lock(10L, TimeUnit.SECONDS);
            UserLabel selectOne = userLabelMapper.selectOne(queryWrapper);
            if(selectOne != null){
                return Result.error("不能重复添加");
            }
            UserLabel userLabel = new UserLabel();
            userLabel.setUserId(userId);
            userLabel.setLabelId(labelId);
            int insert = userLabelMapper.insert(userLabel);
            if(insert > 0){
                return Result.ok(null);
            }
            return Result.error("mysql插入失败");
        }finally {
            rLock.unlock();
        }
    }

    @Override
    public Result deleteLabel(Integer labelId, Integer userId) {
        LambdaQueryWrapper<UserLabel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLabel::getLabelId,labelId).eq(UserLabel::getUserId,userId);
        int i = userLabelMapper.delete(queryWrapper);
        if(i > 0){
            return Result.ok(null);
        }
        return Result.error("mysql删除失败");
    }

    @Override
    public Result<List<UserLabelDTO>> getUserLabelByUserId(Integer userId) {
        LambdaQueryWrapper<UserLabel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLabel::getUserId,userId);
        List<UserLabel> userLabelList = userLabelMapper.selectList(queryWrapper);
        if(userLabelList == null || userLabelList.isEmpty()){
            return Result.error("此用户没有标签");
        }
        return Result.ok(userLabelList.stream().map(UserLabel::toUserLabelDTO).toList());
    }


}




