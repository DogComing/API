package com.platform.dao;

import com.platform.entity.UserForageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiUserForageMapper extends BaseDao<UserForageVo>{

    UserForageVo queryObjectByUser(@Param("userId")Long userId, @Param("id") Integer id);

    int receiveRewards(@Param("userId")Long userId, @Param("forageId") Integer forageId, @Param("awardNum") Integer awardNum);

    void makeUseOf(@Param("userId")Long userId, @Param("forageId") Integer forageId, @Param("useNum") Integer useNum);

    List<UserForageVo> allList(@Param("userId")Long userId);

    List<UserForageVo> queryListByMap(Map<String, Object> map);

    int deleteByUser(@Param("userId") Long userId, @Param("id") Integer id);

    int updateNFT(Map<String, Object> map);
}
