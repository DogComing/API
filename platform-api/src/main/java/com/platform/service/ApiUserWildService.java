package com.platform.service;

import com.platform.dao.ApiUserWildMapper;
import com.platform.entity.UserWildVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiUserWildService {

    @Autowired
    private ApiUserWildMapper userWildMapper;

    public UserWildVo queryObject(Integer id) { return userWildMapper.queryObject(id); }

    public List<UserWildVo> queryList(Map<String, Object> map) { return userWildMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return userWildMapper.queryTotal(map); }

    public int save(UserWildVo userWildVo) { return userWildMapper.save(userWildVo); }

    public int update(Map<String, Object> map) { return userWildMapper.update(map); }

    public UserWildVo queryObjectByUser(Long userId, Integer id) { return userWildMapper.queryObjectByUser(userId, id); }

    public int receiveRewards(Long userId, Integer wildId, Integer awardNum) { return userWildMapper.receiveRewards(userId, wildId, awardNum); }

    public void makeUseOf(Long userId, Integer wildId, Integer useNum) { userWildMapper.makeUseOf(userId, wildId, useNum); }

    public List<UserWildVo> allList(Long userId) { return userWildMapper.allList(userId);}

    public List<UserWildVo> allListByFight (Long userId, Integer isFight) { return userWildMapper.allListByFight(userId, isFight);}

    public int deleteByUser(Long userId, Integer id) { return userWildMapper.deleteByUser(userId, id); }

    public int updateNFT(Map<String, Object> map) { return userWildMapper.updateNFT(map); }
}
