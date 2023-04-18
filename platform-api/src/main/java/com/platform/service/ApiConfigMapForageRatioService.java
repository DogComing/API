package com.platform.service;

import com.platform.dao.ApiConfigMapForageRatioMapper;
import com.platform.entity.ConfigMapForageRatioVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConfigMapForageRatioService {

    @Autowired
    private ApiConfigMapForageRatioMapper configMapForageRatioMapper;

    public ConfigMapForageRatioVo queryObject(Integer id) { return configMapForageRatioMapper.queryObject(id); }

    public List<ConfigMapForageRatioVo> queryList(Map<String, Object> map) { return configMapForageRatioMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return configMapForageRatioMapper.queryTotal(map); }

    public List<ConfigMapForageRatioVo> allGoods() { return configMapForageRatioMapper.allGoods(); }

    public ConfigMapForageRatioVo queryByMapId(Integer mapId) { return configMapForageRatioMapper.queryByMapId(mapId); }
}
