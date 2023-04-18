package com.platform.dao;

import com.platform.entity.ConfigAdVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApiConfigAdMapper extends BaseDao<ConfigAdVo>{

    List<ConfigAdVo> allAd();
}
