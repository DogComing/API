package com.platform.service;

import com.platform.dao.ApiConfigMapWildRatioMapper;
import com.platform.entity.ConfigMapWildRatioVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConfigMapWildRatioService {

    @Autowired
    private ApiConfigMapWildRatioMapper configMapWildRatioMapper;

    public ConfigMapWildRatioVo queryObject(Integer id) { return configMapWildRatioMapper.queryObject(id); }

    public List<ConfigMapWildRatioVo> queryList(Map<String, Object> map) { return configMapWildRatioMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return configMapWildRatioMapper.queryTotal(map); }

    public List<ConfigMapWildRatioVo> allGoods() { return configMapWildRatioMapper.allGoods(); }

    public ConfigMapWildRatioVo queryByMapId(Integer mapId) { return configMapWildRatioMapper.queryByMapId(mapId); }
}
