package com.platform.dao;

import com.platform.entity.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiUserMapper extends BaseDao<UserVo> {

    /**
     * 查询所有用户
     * @return
     */
    List<UserVo> allUser();

    /**
     * 查询团队成员总个数
     * @param map
     * @return
     */
    int queryTeamUserTotal(Map<String, Object> map);

    /**
     * 根据邀请id查询用户id
     * @param usersId
     * @return
     */
    List<UserVo> queryUserId(List<Long> usersId);

    /**
     * 根据钱包登录凭证查询用户
     * @param openId
     * @return
     */
    UserVo queryByOpenId(@Param("openId") String openId);

    /**
     * 根据钱包地址查询用户
     * @param address
     * @return
     */
    UserVo queryByAddress(@Param("openId") String address);

    /**
     * 根据phone查询用户
     * @param phone
     * @return
     */
    UserVo queryByPhone(@Param("phone") String phone);

    /**
     * 计算用户累计胜场次数
     * @param map
     */
    void countGameWinNum(Map<String, Object> map);

    /**
     * 更新账户信息
     * @param map
     */
    void updateAccountInfo(Map<String, Object> map);

    /**
     * 每日重置签到数据
     */
    void resettingSignIn();

    /**
     * 查询可释放积分的人
     */
    List<UserVo> queryReleaseList();

    /**
     * 自动恢复体力
     */
    void resumeBrawn();

    /**
     * 使用体力/减少体力
     * @param userId
     * @param brawnNum
     */
    void useBrawnNum(@Param("userId") Long userId, @Param("brawnNum") Integer brawnNum);

    /**
     * 增加体力/减少体力
     * @param userId
     * @param brawnNum
     */
    void addBrawnNum(@Param("userId") Long userId, @Param("brawnNum") Integer brawnNum);

    /**
     * 使用狗币/代币
     * @param userId
     * @param dogCoinNum
     */
    void useDogCoinNum(@Param("userId") Long userId, @Param("dogCoinNum") Integer dogCoinNum);

    /**
     * 增加狗币/代币
     * @param userId
     * @param dogCoinNum
     */
    void addDogCoinNum(@Param("userId") Long userId, @Param("dogCoinNum") Integer dogCoinNum);

    int updateSignIn(Map<String, Object> map);
}

