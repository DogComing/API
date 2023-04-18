package com.platform.service;

import com.platform.dao.ApiConfigMapFightRatioMapper;
import com.platform.entity.ConfigMapFightRatioVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConfigMapFightRatioService {

    @Autowired
    private ApiConfigMapFightRatioMapper apiConfigMapFightRatioMapper;

    public ConfigMapFightRatioVo queryObject(Integer id) { return apiConfigMapFightRatioMapper.queryObject(id); }

    public List<ConfigMapFightRatioVo> queryList(Map<String, Object> map) { return apiConfigMapFightRatioMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return apiConfigMapFightRatioMapper.queryTotal(map); }

    public List<ConfigMapFightRatioVo> allGoods() { return apiConfigMapFightRatioMapper.allGoods(); }

    public ConfigMapFightRatioVo queryByMapId(Integer mapId) { return apiConfigMapFightRatioMapper.queryByMapId(mapId); }
}
