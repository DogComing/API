package com.platform.dao;

import com.platform.entity.UserFightEquipVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiUserFightEquipMapper extends BaseDao<UserFightEquipVo>{

    UserFightEquipVo queryObjectByUser(@Param("userId")Long userId, @Param("id") Integer id);

    void receiveRewards(@Param("userId")Long userId, @Param("fightId") Integer fightId, @Param("awardNum") Integer awardNum);

    void makeUseOf(@Param("userId")Long userId, @Param("id") Integer id, @Param("useNum") Integer useNum);

    List<UserFightEquipVo> allList(@Param("userId")Long userId);

    List<UserFightEquipVo> queryListByMap(Map<String, Object> map);

    int deleteByUser(@Param("userId") Long userId, @Param("id") Integer id);

    int updateNFT(Map<String, Object> map);
}
