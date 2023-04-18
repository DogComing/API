package com.platform.service;

import com.platform.dao.ApiConfigWildMapper;
import com.platform.entity.ConfigWildVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiConfigWildService {

    @Autowired
    private ApiConfigWildMapper configWildMapper;

    /**
     * 查询某个野生详情
     * @param id
     * @return
     */
    public ConfigWildVo queryObject(Integer id) { return configWildMapper.queryObject(id); }

    /**
     * 查询所有道具
     * @return
     */
    public List<ConfigWildVo> allProp() { return configWildMapper.allWild(); }
}
