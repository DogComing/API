package com.platform.service;

import com.platform.dao.ApiUserDogMapper;
import com.platform.entity.UserDogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiUserDogService {

    @Autowired
    private ApiUserDogMapper userDogMapper;

    /**
     * 查询用户狗狗详情
     * @param id
     * @return
     */
    public UserDogVo queryObject(Integer id) { return userDogMapper.queryObject(id); }

    /**
     * 查询用户狗狗列表
     * @param map
     * @return
     */
    public List<UserDogVo> queryList(Map<String, Object> map) { return userDogMapper.queryList(map); }

    /**
     * 获取捕捉道具总数
     * @param map
     * @return
     */
    public int queryTotal(Map<String, Object> map) { return userDogMapper.queryTotal(map); }

    public void save(UserDogVo userDogVo) { userDogMapper.save(userDogVo); }

    public void update(Map<String, Object> map) { userDogMapper.update(map); }

    public UserDogVo queryObjectByUser(Long userId, Integer dogId) { return userDogMapper.queryObjectByUser(userId, dogId); }

    public void receiveRewards(Long userId, Integer dogId, Integer awardNum) { userDogMapper.receiveRewards(userId, dogId, awardNum); }

    public void makeUseOf(Long userId, Integer dogId) { userDogMapper.makeUseOf(userId, dogId); }

    public List<UserDogVo> queryAllListByUser(Long userId) { return userDogMapper.queryAllListByUser(userId); }

    public UserDogVo queryUserDogInfo(Map<String, Object> map) { return userDogMapper.queryUserDogInfo(map); }

    public List<UserDogVo> allList(Long userId) { return userDogMapper.allList(userId);}

    public void updateBatchIsUse(Long userId) { userDogMapper.updateBatchIsUse(userId); }

    public void updateBatchById(List<UserDogVo> updateMap) { userDogMapper.updateBatchById(updateMap); }

    public List<UserDogVo> queryListByCool(Integer isCool) { return userDogMapper.queryListByCool(isCool); }

    public void updateCoolStatus(Integer status, Integer dogId) { userDogMapper.updateCoolStatus(status, dogId); }

    public int deleteByUser(Long userId, Integer dogId) { return userDogMapper.deleteByUser(userId, dogId); }

    public int updateNFT(Map<String, Object> map) { return userDogMapper.updateNFT(map); }

    public int updateByNFT(Map<String, Object> map) { return userDogMapper.updateByNFT(map); }
}
