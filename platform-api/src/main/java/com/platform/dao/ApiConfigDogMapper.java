package com.platform.dao;

import com.platform.entity.ConfigDogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiConfigDogMapper extends BaseDao<ConfigDogVo>{

    ConfigDogVo queryObjectByGrade(@Param("dogGrade") Integer dogGrade);

    List<ConfigDogVo> allDog();
}
