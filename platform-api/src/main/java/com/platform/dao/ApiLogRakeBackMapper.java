package com.platform.dao;

import com.platform.entity.LogRakeBackVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiLogRakeBackMapper extends BaseDao<LogRakeBackVo>{

    LogRakeBackVo queryObjectByDay(@Param("userId") Long userId, @Param("day") String day);

    List<LogRakeBackVo> allLog(@Param("day") String day);
}
