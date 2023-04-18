package com.platform.dao;

import com.platform.entity.ConfigMapCatchRatioVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiConfigMapCatchRatioMapper extends BaseDao<ConfigMapCatchRatioVo>{

    List<ConfigMapCatchRatioVo> allGoods();

    ConfigMapCatchRatioVo queryByMapId(@Param("mapId") Integer mapId);
}
