package com.platform.dao;

import com.platform.entity.MapVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: platform
 * @description: 地图dao层
 * @author: Yuan
 * @create: 2020-08-13 17:50
 **/
@Mapper
public interface ApiMapMapper extends BaseDao<MapVo>{

    List<MapVo> allMap();
}
