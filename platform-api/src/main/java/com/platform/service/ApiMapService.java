package com.platform.service;

import com.platform.dao.ApiMapMapper;
import com.platform.entity.MapVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiMapService {

    @Autowired
    private ApiMapMapper mapMapper;

    /**
     * 查询某个地图详情
     * @param id
     * @return
     */
    public MapVo queryObject(Integer id) { return mapMapper.queryObject(id); }

    /**
     * 查询地图列表
     * @param map
     * @return
     */
    public List<MapVo> queryList(Map<String, Object> map) { return mapMapper.queryList(map); }

    /**
     * 获取地图总数
     * @param map
     * @return
     */
    public int queryTotal(Map<String, Object> map) { return mapMapper.queryTotal(map); }

    /**
     * 查询所有地图
     * @return
     */
    public List<MapVo> allMap() { return mapMapper.allMap(); }
}
