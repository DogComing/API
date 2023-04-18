package com.platform.service;

import com.platform.dao.ApiLogASGMapper;
import com.platform.entity.LogASGVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiLogASGService {

    @Autowired
    private ApiLogASGMapper logMapper;

    /**
     * 查询ASG记录详情
     * @param userId
     * @param logId
     * @return
     */
    public LogASGVo queryObjectByUser(Long userId, Integer logId) { return logMapper.queryObjectByUser(userId, logId); }

    public List<LogASGVo> queryObjectByType(Long userId, String day) { return logMapper.queryObjectByType(userId, day); }

    /**
     * 查询所有ASG记录
     * @return
     */
    public List<LogASGVo> allLog() { return logMapper.allLog(); }

    /**
     * 新增ASG记录
     * @param logASGVo
     */
    public void save(LogASGVo logASGVo) { logMapper.save(logASGVo); }

    /**
     * 删除所有ASG记录
     * @return
     */
    public int deleteAllLog() { return logMapper.deleteAllLog(); }
}
