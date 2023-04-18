package com.platform.service;

import com.platform.dao.ApiConfigCatchEquipMapper;
import com.platform.entity.ConfigCatchEquipVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConfigCatchEquipService {

    @Autowired
    private ApiConfigCatchEquipMapper configCatchEquipMapper;

    /**
     * 查询某个饲料详情
     * @param id
     * @return
     */
    public ConfigCatchEquipVo queryObject(Integer id) { return configCatchEquipMapper.queryObject(id); }

    public List<ConfigCatchEquipVo> queryListByMap(Map<String, Object> map){ return configCatchEquipMapper.queryListByMap(map); }

    /**
     * 查询所有饲料
     * @return
     */
    public List<ConfigCatchEquipVo> allCatchEquip() { return configCatchEquipMapper.allCatchEquip(); }

    public ConfigCatchEquipVo queryObjectByType(Integer grade, Integer type) { return configCatchEquipMapper.queryObjectByType(grade, type); }

    public ConfigCatchEquipVo queryObjectByGrade(Integer grade) { return configCatchEquipMapper.queryObjectByGrade(grade); }
}
