package com.platform.service;

import com.platform.dao.ApiConfigMapCatchRatioMapper;
import com.platform.entity.ConfigMapCatchRatioVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ApiConfigMapCatchRatioService {

    @Autowired
    private ApiConfigMapCatchRatioMapper mapCatchRatioMapper;

    public ConfigMapCatchRatioVo queryObject(Integer id) { return mapCatchRatioMapper.queryObject(id); }

    public List<ConfigMapCatchRatioVo> queryList(Map<String, Object> map) { return mapCatchRatioMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return mapCatchRatioMapper.queryTotal(map); }

    public List<ConfigMapCatchRatioVo> allGoods() { return mapCatchRatioMapper.allGoods(); }

    public ConfigMapCatchRatioVo queryByMapId(Integer mapId) { return mapCatchRatioMapper.queryByMapId(mapId); }
}
