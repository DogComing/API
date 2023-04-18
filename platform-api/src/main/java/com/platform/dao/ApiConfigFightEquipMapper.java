package com.platform.dao;

import com.platform.entity.ConfigFightEquipVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiConfigFightEquipMapper extends BaseDao<ConfigFightEquipVo>{

    List<ConfigFightEquipVo> queryListByMap(Map<String, Object> map);

    List<ConfigFightEquipVo> allFightEquip();

    ConfigFightEquipVo queryObjectByType(@Param("grade") Integer grade, @Param("type") Integer type);

    ConfigFightEquipVo queryObjectByGrade(@Param("grade") Integer grade);
}
