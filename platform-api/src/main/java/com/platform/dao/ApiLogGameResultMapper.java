package com.platform.dao;

import com.platform.entity.LogGameResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiLogGameResultMapper extends BaseDao<LogGameResultVo>{

    List<LogGameResultVo> queryListByGameNum(@Param("gameNum") String gameNum);
}
