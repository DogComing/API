package com.platform.dao;

import com.platform.entity.LogASGVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiLogASGMapper extends BaseDao<LogASGVo>{

    List<LogASGVo> allLog();

    LogASGVo queryObjectByUser(@Param("userId") Long userId, @Param("logId") Integer logId);

    List<LogASGVo> queryObjectByType(@Param("userId") Long userId, @Param("day") String day);

    int deleteAllLog();
}
