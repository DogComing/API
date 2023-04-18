package com.platform.service;

import com.platform.dao.ApiLogRakeBackMapper;
import com.platform.entity.LogRakeBackVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiLogRakeBackService {

    @Autowired
    private ApiLogRakeBackMapper logRakeBackMapper;

    /**
     * 查询自己昨日记录
     * @return
     */
    public LogRakeBackVo queryObjectByDay(Long userId, String day) { return logRakeBackMapper.queryObjectByDay(userId, day); }

    /**
     * 查询所有返佣记录
     * @return
     */
    public List<LogRakeBackVo> allLog(String day) { return logRakeBackMapper.allLog(day); }

    /**
     * 新增返佣记录
     * @param logRakeBackVo
     */
    public void save(LogRakeBackVo logRakeBackVo) { logRakeBackMapper.save(logRakeBackVo); }
}
