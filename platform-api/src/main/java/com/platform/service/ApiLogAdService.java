package com.platform.service;

import com.platform.dao.ApiLogAdMapper;
import com.platform.entity.LogAdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiLogAdService {

    @Autowired
    private ApiLogAdMapper logAdMapper;

    /**
     * 查询广告观看详情
     * @param userId
     * @param adId
     * @return
     */
    public LogAdVo queryObjectByUser(Long userId, Integer adId) { return logAdMapper.queryObjectByUser(userId, adId); }

    /**
     * 查询所有广告观看记录
     * @return
     */
    public List<LogAdVo> allAdLog() { return logAdMapper.allAdLog(); }

    /**
     * 新增广告观看记录
     * @param logAdVo
     */
    public void save(LogAdVo logAdVo) { logAdMapper.save(logAdVo); }

    /**
     * 删除所有广告观看记录【每日凌晨删除】
     * @return
     */
    public int deleteAllLog() { return logAdMapper.deleteAllLog(); }
}
