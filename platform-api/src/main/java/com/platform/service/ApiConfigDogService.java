package com.platform.service;

import com.platform.dao.ApiConfigDogMapper;
import com.platform.entity.ConfigDogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiConfigDogService {

    @Autowired
    private ApiConfigDogMapper configDogMapper;

    /**
     * 查询配置表狗狗详情
     * @param id
     * @return
     */
    public ConfigDogVo queryObject(Integer id) { return configDogMapper.queryObject(id); }

    /**
     * 根据等级查询配置表狗狗详情
     * @param dogGrade
     * @return
     */
    public ConfigDogVo queryObjectByGrade(Integer dogGrade) { return configDogMapper.queryObjectByGrade(dogGrade); }

    /**
     * 查询配置表所有狗狗
     * @return
     */
    public List<ConfigDogVo> allDog() { return configDogMapper.allDog(); }
}
