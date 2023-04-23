package com.platform.dao;

import com.platform.entity.UserPropVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiUserPropMapper extends BaseDao<UserPropVo>{

    UserPropVo queryObjectByUser(@Param("userId") Long userId, @Param("id") Integer id);

    void receiveRewards(@Param("userId") Long userId, @Param("propId") Integer propId, @Param("awardNum") Integer awardNum);

    void makeUseOf(@Param("userId") Long userId, @Param("id") Integer id, @Param("useNum") Integer useNum);

    List<UserPropVo> allList(@Param("userId") Long userId);

    List<UserPropVo> queryListByMap(Map<String, Object> map);

    int deleteByUser(@Param("userId") Long userId, @Param("id") Integer id);

    int updateNFT(Map<String, Object> map);

    int updateByNFT(Map<String, Object> map);
}
