package com.platform.dao;

import com.platform.entity.ConfigMapForageRatioVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiConfigMapForageRatioMapper extends BaseDao<ConfigMapForageRatioVo>{

    List<ConfigMapForageRatioVo> allGoods();

    ConfigMapForageRatioVo queryByMapId(@Param("mapId") Integer mapId);
}
