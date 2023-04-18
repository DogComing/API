package com.platform.dao;

import com.platform.entity.ConfigCatchEquipVo;
import com.platform.entity.ConfigForageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiConfigCatchEquipMapper extends BaseDao<ConfigCatchEquipVo>{

    List<ConfigCatchEquipVo> queryListByMap(Map<String, Object> map);

    List<ConfigCatchEquipVo> allCatchEquip();

    ConfigCatchEquipVo queryObjectByType(@Param("grade") Integer grade, @Param("type") Integer type);

    ConfigCatchEquipVo queryObjectByGrade(@Param("grade") Integer grade);
}
