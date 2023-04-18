package com.platform.dao;

import com.platform.entity.ConfigMapWildRatioVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiConfigMapWildRatioMapper extends BaseDao<ConfigMapWildRatioVo> {

    List<ConfigMapWildRatioVo> allGoods();

    ConfigMapWildRatioVo queryByMapId(@Param("mapId") Integer mapId);
}
