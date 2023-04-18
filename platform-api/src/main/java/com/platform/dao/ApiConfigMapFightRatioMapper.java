package com.platform.dao;

import com.platform.entity.ConfigMapFightRatioVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiConfigMapFightRatioMapper extends BaseDao<ConfigMapFightRatioVo>{

    List<ConfigMapFightRatioVo> allGoods();

    ConfigMapFightRatioVo queryByMapId(@Param("mapId") Integer mapId);
}
