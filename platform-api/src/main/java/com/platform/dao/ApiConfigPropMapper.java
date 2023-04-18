package com.platform.dao;

import com.platform.entity.ConfigPropVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiConfigPropMapper extends BaseDao<ConfigPropVo>{

    List<ConfigPropVo> queryListByMap(Map<String, Object> map);

    List<ConfigPropVo> allProp();
}
