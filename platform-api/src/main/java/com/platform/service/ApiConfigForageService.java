package com.platform.service;

import com.platform.dao.ApiConfigForageMapper;
import com.platform.entity.ConfigForageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConfigForageService {

    @Autowired
    private ApiConfigForageMapper configForageMapper;

    /**
     * 查询某个饲料详情
     * @param id
     * @return
     */
    public ConfigForageVo queryObject(Integer id) { return configForageMapper.queryObject(id); }

    public List<ConfigForageVo> queryListByMap(Map<String, Object> map){ return configForageMapper.queryListByMap(map); }

    /**
     * 查询所有饲料
     * @return
     */
    public List<ConfigForageVo> allForage() { return configForageMapper.allForage(); }

    public ConfigForageVo queryObjectByGrade(Integer grade) { return configForageMapper.queryObjectByGrade(grade); }

    public ConfigForageVo queryObjectByMap(Map<String, Object> map) { return configForageMapper.queryObjectByMap(map); }
}
