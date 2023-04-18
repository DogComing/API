package com.platform.dao;

import com.platform.entity.ConfigMapPetRatioVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiConfigMapPetRatioMapper extends BaseDao<ConfigMapPetRatioVo>{

    List<ConfigMapPetRatioVo> allGoods();

    ConfigMapPetRatioVo queryByMapId(@Param("mapId") Integer mapId);
}
