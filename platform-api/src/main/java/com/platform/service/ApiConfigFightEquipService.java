package com.platform.service;

import com.platform.dao.ApiConfigFightEquipMapper;
import com.platform.entity.ConfigFightEquipVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConfigFightEquipService {

    @Autowired
    private ApiConfigFightEquipMapper configFightEquipMapper;

    /**
     * 查询某个对战装备详情
     * @param id
     * @return
     */
    public ConfigFightEquipVo queryObject(Integer id) { return configFightEquipMapper.queryObject(id); }

    public List<ConfigFightEquipVo> queryListByMap(Map<String, Object> map){ return configFightEquipMapper.queryListByMap(map); }

    /**
     * 查询所有对战装备
     * @return
     */
    public List<ConfigFightEquipVo> allFightEquip() { return configFightEquipMapper.allFightEquip(); }

    public ConfigFightEquipVo queryObjectByType(Integer grade, Integer type) { return configFightEquipMapper.queryObjectByType(grade, type); }

    public ConfigFightEquipVo queryObjectByGrade(Integer grade) { return configFightEquipMapper.queryObjectByGrade(grade); }
}
