package com.platform.service;

import com.platform.dao.ApiConfigMapGemRatioMapper;
import com.platform.entity.ConfigMapGemRatioVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConfigMapGemRatioService {

    @Autowired
    private ApiConfigMapGemRatioMapper configMapGemRatioMapper;

    public ConfigMapGemRatioVo queryObject(Integer id) { return configMapGemRatioMapper.queryObject(id); }

    public List<ConfigMapGemRatioVo> queryList(Map<String, Object> map) { return configMapGemRatioMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return configMapGemRatioMapper.queryTotal(map); }

    public List<ConfigMapGemRatioVo> allGoods() { return configMapGemRatioMapper.allGoods(); }

    public ConfigMapGemRatioVo queryByMapId(Integer mapId) { return configMapGemRatioMapper.queryByMapId(mapId); }
}
