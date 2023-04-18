package com.platform.service;

import com.platform.dao.ApiConfigAdMapper;
import com.platform.entity.ConfigAdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiConfigAdService {

    @Autowired
    private ApiConfigAdMapper configAdMapper;

    /**
     * 查询配置表广告详情
     * @param id
     * @return
     */
    public ConfigAdVo queryObject(Integer id) { return configAdMapper.queryObject(id); }

    /**
     * 查询配置表所有广告
     * @return
     */
    public List<ConfigAdVo> allAd() { return configAdMapper.allAd(); }
}
