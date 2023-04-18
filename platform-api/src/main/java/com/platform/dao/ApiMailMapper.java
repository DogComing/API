package com.platform.dao;

import com.platform.entity.MailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiMailMapper extends BaseDao<MailVo>{

    List<MailVo> allList(@Param("userId") Long userId);

    List<MailVo> allListByMap(Map<String, Object> map);

    int deleteByUser(Map<String, Object> map);

    int updateByUser(Map<String, Object> map);
}
