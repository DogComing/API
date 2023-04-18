package com.platform.dao;

import com.platform.entity.LogAwardVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiLogAwardMapper extends BaseDao<LogAwardVo>{

    List<LogAwardVo> logByGameNum(@Param("userId") Long userId, @Param("gameNum") String gameNum);

    List<LogAwardVo> queryListByGameNum(@Param("gameNum") String gameNum);
}
