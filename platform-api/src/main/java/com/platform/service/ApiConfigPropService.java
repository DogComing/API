package com.platform.service;

import com.platform.dao.ApiConfigPropMapper;
import com.platform.entity.ConfigPropVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConfigPropService {

    @Autowired
    private ApiConfigPropMapper configPropMapper;

    /**
     * 查询某个道具详情
     * @param id
     * @return
     */
    public ConfigPropVo queryObject(Integer id) { return configPropMapper.queryObject(id); }

    public List<ConfigPropVo> queryListByMap(Map<String, Object> map){ return configPropMapper.queryListByMap(map); }

    /**
     * 查询所有道具
     * @return
     */
    public List<ConfigPropVo> allProp() { return configPropMapper.allProp(); }
}
