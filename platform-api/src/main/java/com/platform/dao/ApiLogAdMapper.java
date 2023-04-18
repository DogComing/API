package com.platform.dao;

import com.platform.entity.LogAdVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiLogAdMapper extends BaseDao<LogAdVo>{

    List<LogAdVo> allAdLog();

    LogAdVo queryObjectByUser(@Param("userId") Long userId, @Param("adId") Integer adId);

    int deleteAllLog();
}
