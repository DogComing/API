package com.platform.dao;

import com.platform.entity.LogPayVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiLogPayMapper extends BaseDao<LogPayVo>{

    List<LogPayVo> allLog();

    LogPayVo queryObjectByUser(@Param("userId") Long userId, @Param("logId") Integer logId);

    int deleteAllLog();
}
