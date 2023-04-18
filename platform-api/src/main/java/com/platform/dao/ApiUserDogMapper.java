package com.platform.dao;

import com.platform.entity.UserDogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiUserDogMapper extends BaseDao<UserDogVo>{

    UserDogVo queryObjectByUser(@Param("userId")Long userId, @Param("dogId") Integer dogId);

    void receiveRewards(@Param("userId")Long userId, @Param("dogId") Integer dogId, @Param("awardNum") Integer awardNum);

    void makeUseOf(@Param("userId")Long userId, @Param("dogId") Integer dogId);

    /**
     * 获取用户所有狗狗的信息
     */
    List<UserDogVo> queryAllListByUser(Long userId);

    /**
     * 查询用户某个狗狗的信息
     * @param map
     * @return
     */
    UserDogVo queryUserDogInfo(Map<String, Object> map);

    List<UserDogVo> allList(@Param("userId") Long userId);

    void updateBatchIsUse(@Param("userId") Long userId);

    void updateBatchById(@Param("updateMap") List<UserDogVo> updateMap);

    List<UserDogVo> queryListByCool(@Param("isCool") Integer isCool);

    void updateCoolStatus(@Param("status") Integer status, @Param("dogId") Integer dogId);

    int deleteByUser(@Param("userId")Long userId, @Param("dogId") Integer dogId);

    int updateNFT(Map<String, Object> map);
}
