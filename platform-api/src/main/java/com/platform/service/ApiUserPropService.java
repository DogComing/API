package com.platform.service;

import com.platform.dao.ApiUserPropMapper;
import com.platform.entity.UserPropVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiUserPropService {

    @Autowired
    private ApiUserPropMapper userPropMapper;

    public UserPropVo queryObject(Integer id) { return userPropMapper.queryObject(id); }

    public List<UserPropVo> queryList(Map<String, Object> map) { return userPropMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return userPropMapper.queryTotal(map); }

    public int save(UserPropVo userPropVo) { return userPropMapper.save(userPropVo); }

    public int update(Map<String, Object> map) { return userPropMapper.update(map); }

    public UserPropVo queryObjectByUser(Long userId, Integer id) { return userPropMapper.queryObjectByUser(userId, id); }

    public void receiveRewards(Long userId, Integer propId, Integer awardNum) { userPropMapper.receiveRewards(userId, propId, awardNum); }

    public void makeUseOf(Long userId, Integer id, Integer useNum) { userPropMapper.makeUseOf(userId, id, useNum); }

    public List<UserPropVo> allList(Long userId) { return userPropMapper.allList(userId);}

    public List<UserPropVo> queryListByMap(Map<String, Object> map){ return userPropMapper.queryListByMap(map); }

    public int deleteByUser(Long userId, Integer id){ return userPropMapper.deleteByUser(userId, id); }

    public int updateNFT(Map<String, Object> map) { return userPropMapper.updateNFT(map); }
}
