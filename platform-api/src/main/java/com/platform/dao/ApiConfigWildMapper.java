package com.platform.dao;

import com.platform.entity.ConfigWildVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApiConfigWildMapper extends BaseDao<ConfigWildVo>{

    List<ConfigWildVo> allWild();
}
