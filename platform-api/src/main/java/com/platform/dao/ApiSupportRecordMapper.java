package com.platform.dao;

import com.platform.entity.SupportRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiSupportRecordMapper extends BaseDao<SupportRecordVo>{

    List<SupportRecordVo> recordByGameNum(@Param("userId") Long userId, @Param("gameNum") String gameNum);

    List<SupportRecordVo> queryListByGameNum(@Param("gameNum") String gameNum);
}
