package com.platform.dao;

import com.platform.entity.UserWildVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiUserWildMapper extends BaseDao<UserWildVo>{

    UserWildVo queryObjectByUser(@Param("userId") Long userId, @Param("id") Integer id);

    int receiveRewards(@Param("userId") Long userId, @Param("wildId") Integer wildId, @Param("awardNum") Integer awardNum);

    void makeUseOf(@Param("userId") Long userId, @Param("id") Integer id, @Param("useNum") Integer useNum);

    List<UserWildVo> allList(@Param("userId") Long userId);

    List<UserWildVo> allListByFight(@Param("userId") Long userId, @Param("isFight") Integer isFight);

    int deleteByUser(@Param("userId") Long userId, @Param("id") Integer id);

    int updateNFT(Map<String, Object> map);

    int updateByNFT(Map<String, Object> map);
}
