package com.platform.dao;

import com.platform.entity.ConfigForageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiConfigForageMapper extends BaseDao<ConfigForageVo>{

    public List<ConfigForageVo> queryListByMap(Map<String, Object> map);

    List<ConfigForageVo> allForage();

    ConfigForageVo queryObjectByGrade(@Param("grade") Integer grade);

    ConfigForageVo queryObjectByMap(Map<String, Object> map);
}
