package com.platform.dao;

import com.platform.entity.UserCatchEquipVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiUserCatchEquipMapper extends BaseDao<UserCatchEquipVo> {

    UserCatchEquipVo queryObjectByUser(@Param("userId") Long userId, @Param("id") Integer id);

    List<UserCatchEquipVo> queryObjectByEquipId(@Param("userId") Long userId, @Param("equipId") Integer equipId);

    int receiveRewards(@Param("userId")Long userId, @Param("equipId") Integer equipId, @Param("awardNum") Integer awardNum);

    void makeUseOf(@Param("userId")Long userId, @Param("id") Integer id, @Param("awardNum") Integer awardNum);

    List<UserCatchEquipVo> allList(@Param("userId")Long userId);

    List<UserCatchEquipVo> queryListByMap(Map<String, Object> map);

    List<UserCatchEquipVo> queryObjectByGrade(@Param("userId") Long userId, @Param("grade") Integer grade, @Param("type")Integer type);

    int deleteByUser(@Param("userId") Long userId, @Param("id") Integer id);

    int updateNFT(Map<String, Object> map);

    int updateBatchIsUse(@Param("userId") Long userId, @Param("catchType") Integer catchType);
}
