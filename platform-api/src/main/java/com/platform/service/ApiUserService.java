package com.platform.service;

import com.platform.dao.ApiUserMapper;
import com.platform.entity.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiUserService {

    @Autowired
    private ApiUserMapper userMapper;

    public UserVo queryObject(Long userId) {
        return userMapper.queryObject(userId);
    }

    public List<UserVo> queryList(Map<String, Object> map) { return userMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) {return userMapper.queryTotal(map); }

    public List<UserVo> allUser() {return userMapper.allUser(); }

    public void save(UserVo userVo) { userMapper.save(userVo); }

    public void update(Map<String, Object> map) { userMapper.update(map); }

    public UserVo queryByOpenId(String openId) { return userMapper.queryByOpenId(openId); }

    public UserVo queryByAddress(String address) { return userMapper.queryByAddress(address); }

    public void countGameWinNum(Map<String, Object> map) { userMapper.countGameWinNum(map); }

    public void resettingSignIn(){ userMapper.resettingSignIn(); }

    public void resumeBrawn() { userMapper.resumeBrawn(); }

    /**
     * 使用体力/减少体力
     * @param userId
     * @param brawnNum
     */
    public void useBrawnNum(Long userId, Integer brawnNum) { userMapper.useBrawnNum(userId, brawnNum); }

    /**
     * 增加体力
     * @param userId
     * @param brawnNum
     */
    public void addBrawnNum(Long userId, Integer brawnNum) { userMapper.addBrawnNum(userId, brawnNum); }

    /**
     * 使用狗币/代币
     * @param userId
     * @param dogCoinNum
     */
    public void useDogCoinNum (Long userId, Integer dogCoinNum) { userMapper.useDogCoinNum(userId, dogCoinNum); }

    /**
     * 增加狗币/代币
     * @param userId
     * @param dogCoinNum
     */
    public void addDogCoinNum (Long userId, Integer dogCoinNum) { userMapper.addDogCoinNum(userId, dogCoinNum); }

    public int updateSignIn(Map<String, Object> map) { return userMapper.updateSignIn(map); }

}
