package com.platform.service;

import com.platform.dao.ApiConfigMapPetRatioMapper;
import com.platform.entity.ConfigMapPetRatioVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConfigMapPetRatioService {

    @Autowired
    private ApiConfigMapPetRatioMapper configMapPetRatioMapper;

    public ConfigMapPetRatioVo queryObject(Integer id) { return configMapPetRatioMapper.queryObject(id); }

    public List<ConfigMapPetRatioVo> queryList(Map<String, Object> map) { return configMapPetRatioMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return configMapPetRatioMapper.queryTotal(map); }

    public List<ConfigMapPetRatioVo> allGoods() { return configMapPetRatioMapper.allGoods(); }

    public ConfigMapPetRatioVo queryByMapId(Integer mapId) { return configMapPetRatioMapper.queryByMapId(mapId); }
}
