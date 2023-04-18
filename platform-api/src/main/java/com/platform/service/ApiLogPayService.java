package com.platform.service;

import com.platform.dao.ApiLogPayMapper;
import com.platform.entity.LogPayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiLogPayService {

    @Autowired
    private ApiLogPayMapper logPayMapper;

    /**
     * 查询支付详情
     * @param userId
     * @param logId
     * @return
     */
    public LogPayVo queryObjectByUser(Long userId, Integer logId) { return logPayMapper.queryObjectByUser(userId, logId); }

    /**
     * 查询所有支付记录
     * @return
     */
    public List<LogPayVo> allLog() { return logPayMapper.allLog(); }

    /**
     * 新增支付记录
     * @param logPayVo
     */
    public void save(LogPayVo logPayVo) { logPayMapper.save(logPayVo); }

    /**
     * 删除所有支付记录
     * @return
     */
    public int deleteAllLog() { return logPayMapper.deleteAllLog(); }

}
