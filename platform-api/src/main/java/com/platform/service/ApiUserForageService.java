package com.platform.service;

import com.platform.dao.ApiUserForageMapper;
import com.platform.entity.UserForageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiUserForageService {

    @Autowired
    private ApiUserForageMapper apiUserForageMapper;

    public UserForageVo queryObject(Integer id) { return apiUserForageMapper.queryObject(id); }

    public List<UserForageVo> queryList(Map<String, Object> map) { return apiUserForageMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return apiUserForageMapper.queryTotal(map); }

    public int save(UserForageVo userForageVo) { return apiUserForageMapper.save(userForageVo); }

    public int update(Map<String, Object> map) { return apiUserForageMapper.update(map); }

    public UserForageVo queryObjectByUser(Long userId, Integer id) { return apiUserForageMapper.queryObjectByUser(userId, id); }

    public int receiveRewards(Long userId, Integer forageId, Integer awardNum) { return apiUserForageMapper.receiveRewards(userId, forageId, awardNum); }

    public void makeUseOf(Long userId, Integer forageId, Integer useNum) { apiUserForageMapper.makeUseOf(userId, forageId, useNum); }

    public List<UserForageVo> allList(Long userId) { return apiUserForageMapper.allList(userId);}

    public List<UserForageVo> queryListByMap(Map<String, Object> map){ return apiUserForageMapper.queryListByMap(map); }

    public int deleteByUser(Long userId, Integer id){ return apiUserForageMapper.deleteByUser(userId, id); }

    public int updateNFT(Map<String, Object> map) { return apiUserForageMapper.updateNFT(map); }
}
