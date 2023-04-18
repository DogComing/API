package com.platform.dao;

import com.platform.entity.ConfigMapGemRatioVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiConfigMapGemRatioMapper extends BaseDao<ConfigMapGemRatioVo>{

    List<ConfigMapGemRatioVo> allGoods();

    ConfigMapGemRatioVo queryByMapId(@Param("mapId") Integer mapId);
}
