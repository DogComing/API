package com.platform.service;

import com.platform.dao.ApiUserCatchEquipMapper;
import com.platform.entity.UserCatchEquipVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: platform
 * @description:
 * @author: Yuan
 * @create: 2020-08-14 09:40
 **/
@Service
public class ApiUserCatchEquipService {

    @Autowired
    private ApiUserCatchEquipMapper userCatchEquipMapper;

    /**
     * 查询某个捕捉道具详情
     * @param id
     * @return
     */
    public UserCatchEquipVo queryObject(Integer id) { return userCatchEquipMapper.queryObject(id); }

    /**
     * 查询捕捉道具列表
     * @param map
     * @return
     */
    public List<UserCatchEquipVo> queryList(Map<String, Object> map) { return userCatchEquipMapper.queryList(map); }

    /**
     * 获取捕捉道具总数
     * @param map
     * @return
     */
    public int queryTotal(Map<String, Object> map) { return userCatchEquipMapper.queryTotal(map); }

    public void save(UserCatchEquipVo userCatchEquipVo) { userCatchEquipMapper.save(userCatchEquipVo); }

    public void update(Map<String, Object> map) { userCatchEquipMapper.update(map); }

    /**
     * 通过用户ID 和 主键ID，查询某个捕捉道具信息
     * @param userId 用户ID
     * @param id 用户捕捉道具表主键ID
     * @return
     */
    public UserCatchEquipVo queryObjectByUser(Long userId, Integer id) { return userCatchEquipMapper.queryObjectByUser(userId, id); }

    /**
     * 通过用户ID 和 捕捉道具ID，查询捕捉道具信息
     * @param userId 用户ID
     * @param equipId 道具ID（对应道具配置表主键ID）
     * @return
     */
    public List<UserCatchEquipVo> queryObjectByEquipId(Long userId, Integer equipId) { return userCatchEquipMapper.queryObjectByEquipId(userId, equipId); }

    public int receiveRewards(Long userId, Integer equipId, Integer awardNum) { return userCatchEquipMapper.receiveRewards(userId, equipId, awardNum); }

    public void makeUseOf(Long userId, Integer id, Integer awardNum) { userCatchEquipMapper.makeUseOf(userId, id, awardNum); }

    public List<UserCatchEquipVo> allList(Long userId) { return userCatchEquipMapper.allList(userId);}

    public List<UserCatchEquipVo> queryListByMap(Map<String, Object> map){ return userCatchEquipMapper.queryListByMap(map); }

    public  List<UserCatchEquipVo> queryObjectByGrade(Long userId, Integer grade, Integer type){ return userCatchEquipMapper.queryObjectByGrade(userId, grade, type); }

    public int deleteByUser(Long userId, Integer id) { return userCatchEquipMapper.deleteByUser(userId, id); }

    public int updateNFT(Map<String, Object> map) { return userCatchEquipMapper.updateNFT(map); }

    public int updateBatchIsUse(Long userId, Integer catchType) { return userCatchEquipMapper.updateBatchIsUse(userId, catchType); }
}
