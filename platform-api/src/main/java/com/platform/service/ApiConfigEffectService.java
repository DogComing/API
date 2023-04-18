package com.platform.service;

import com.platform.dao.ApiConfigEffectMapper;
import com.platform.entity.ConfigEffectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiConfigEffectService {

    @Autowired
    private ApiConfigEffectMapper configEffectMapper;

    /**
     * 查询配置表额外奖励详情
     * @param id
     * @return
     */
    public ConfigEffectVo queryObject(Integer id) { return configEffectMapper.queryObject(id); }

    /**
     * 查询配置表所有额外奖励
     * @return
     */
    public List<ConfigEffectVo> allEffect() { return configEffectMapper.allEffect(); }
}
