package com.platform.dao;

import com.platform.entity.ConfigEffectVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApiConfigEffectMapper extends BaseDao<ConfigEffectVo>{


    List<ConfigEffectVo> allEffect();
}
