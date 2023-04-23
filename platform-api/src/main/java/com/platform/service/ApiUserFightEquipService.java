package com.platform.service;

import com.platform.dao.ApiUserFightEquipMapper;
import com.platform.entity.UserFightEquipVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiUserFightEquipService {

    @Autowired
    private ApiUserFightEquipMapper fightEquipMapper;

    /**
     * 查询某个战斗道具详情
     * @param id
     * @return
     */
    public UserFightEquipVo queryObject(Integer id) { return fightEquipMapper.queryObject(id); }

    /**
     * 查询战斗道具列表
     * @param map
     * @return
     */
    public List<UserFightEquipVo> queryList(Map<String, Object> map) { return fightEquipMapper.queryList(map); }

    /**
     * 获取战斗道具总数
     * @param map
     * @return
     */
    public int queryTotal(Map<String, Object> map) { return fightEquipMapper.queryTotal(map); }

    public void save(UserFightEquipVo userFightEquipVo) { fightEquipMapper.save(userFightEquipVo); }

    public void update(Map<String, Object> map) { fightEquipMapper.update(map); }

    public UserFightEquipVo queryObjectByUser(Long userId, Integer id) { return fightEquipMapper.queryObjectByUser(userId, id); }

    public void receiveRewards(Long userId, Integer fightId, Integer awardNum) { fightEquipMapper.receiveRewards(userId, fightId, awardNum); }

    public void makeUseOf(Long userId, Integer id, Integer useNum) { fightEquipMapper.makeUseOf(userId, id, useNum); }

    public List<UserFightEquipVo> allList(Long userId) { return fightEquipMapper.allList(userId);}

    public List<UserFightEquipVo> queryListByMap(Map<String, Object> map){ return fightEquipMapper.queryListByMap(map); }

    public int deleteByUser(Long userId, Integer id){ return fightEquipMapper.deleteByUser(userId, id); }

    public int updateNFT(Map<String, Object> map) { return fightEquipMapper.updateNFT(map); }

    public int updateByNFT(Map<String, Object> map) { return fightEquipMapper.updateByNFT(map); }
}
