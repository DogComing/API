package com.platform.api;

import com.platform.annotation.LoginUser;
import com.platform.dao.ApiConfigCatchEquipMapper;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.utils.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户邮件接口
 *
 * @author xingGuangTeam
 * @email 249893127@qq.com
 * @date 2019-03-23 15:31
 */
@Api(tags = "用户邮件接口")
@RestController
@RequestMapping("/api/mail")
public class ApiMailController extends ApiBaseAction {

    @Autowired
    private ApiMailService mailService;
    @Autowired
    private ApiUserService userService;

    @Autowired
    private ApiConfigForageService configForageService;
    @Autowired
    private ApiConfigCatchEquipMapper configCatchEquipMapper;
    @Autowired
    private ApiConfigFightEquipService configFightEquipService;
    @Autowired
    private ApiConfigPropService configPropService;

    @Autowired
    private ApiUserForageService userForageService;
    @Autowired
    private ApiUserCatchEquipService userCatchEquipService;
    @Autowired
    private ApiUserFightEquipService userFightEquipService;
    @Autowired
    private ApiUserPropService userPropService;

    /**
     * 获取所有邮件
     * @param loginUser
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "获取所有邮件")
    @PostMapping("all")
    public Object getAddressList(@LoginUser UserVo loginUser, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size){

        try {
            Map params = new HashMap();
            params.put("page", page);
            params.put("limit", size);
            params.put("sidx", "id");
            params.put("order", "desc");
            params.put("userId", loginUser.getUserId());

            //查询列表数据
            Query query = new Query(params);
            int total = mailService.queryTotal(query);
            List<MailVo> mailVoList = mailService.queryList(query);
            ApiPageUtils pageUtil = new ApiPageUtils(mailVoList, total, query.getLimit(), query.getPage());

            return toResponsSuccess(pageUtil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 获取邮件详情
     * @Param: [loginUser]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "获取邮件详情")
    @GetMapping(value = "detail/{id}")
    public Object getAddressInfo(@LoginUser UserVo loginUser, @PathVariable("id") Integer id){

        try {
            MailVo mailVo = mailService.queryObject(id);
            if (null == mailVo) return toResponsObject(400, "未查询到该邮件信息", "");

            Map map = new HashMap();
            map.put("id", id);
            map.put("isRead", 1);
            mailService.update(map);

            List<MailVo> mailVoList = mailService.allList(loginUser.getUserId());

            for (int i = 0; i < mailVoList.size(); i++){
                if (mailVoList.get(i).getIsRead() == 0) return toResponsSuccess(mailVo);
            }

            Map userMap = new HashMap();
            userMap.put("isHaveUnread", 2);
            userMap.put("userId", loginUser.getUserId());
            userService.update(userMap);

            return toResponsSuccess(mailVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 领取奖励
     * @param id
     * @return
     */
    @PutMapping("receive/{id}")
    @ApiOperation(value = "领取奖励")
    public Object receiveMail(@LoginUser UserVo loginUser, @PathVariable("id") Integer id) {

        try {

            MailVo mailVo = mailService.queryObject(id);
            Integer awardType = mailVo.getAwardType(); // 签到奖励类型【1:代币  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备】
            Integer kindId = mailVo.getKindId(); // 种类ID【例如：饲料表 一级饲料对应的ID；道具表 某个道具对应的ID】

            Map userMap = new HashMap();
            if (mailVo.getType() == 1){
                if (awardType.equals(1)){
                    userMap.put("dogCoin", mailVo.getAwardNum());
                    userMap.put("userId", loginUser.getUserId());
                    userService.update(userMap);
                } else if (awardType.equals(2)){
                    userMap.put("residueMuscleNum", mailVo.getAwardNum());
                    userMap.put("userId", loginUser.getUserId());
                    userService.update(userMap);
                } else if (awardType.equals(3)){
                    receiveForage(kindId, loginUser.getUserId(), mailVo.getAwardNum());
                } else if (awardType.equals(4)){
                    receiveProp(kindId, loginUser.getUserId(), mailVo.getAwardNum());
                } else if (awardType.equals(5)){
                    receiveCatchEquip(kindId, loginUser.getUserId(), mailVo.getAwardNum());
                } else if (awardType.equals(6)){
                    receiveFightEquip(kindId, loginUser.getUserId(), mailVo.getAwardNum());
                }
            } else {
                userMap.put("residueMuscleNum", mailVo.getBrawnNum());
                userMap.put("dogCoin", mailVo.getAwardNum());
                userMap.put("userId", loginUser.getUserId());
                userService.update(userMap);
            }

            Map map = new HashMap();
            map.put("id", id);
            map.put("isReceive", 1);
            mailService.update(map);

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsObject(400, "领取失败！", null);
    }

    /**
     * 一键领取未领取奖励邮件
     * @param loginUser
     * @return
     */
    @PutMapping("receive/all")
    @ApiOperation(value = "一键领取未领取奖励邮件")
    public Object allReceiveMail(@LoginUser UserVo loginUser) {

        try {

            Map mailMap = new HashMap();
            mailMap.put("isReceive", 0);
            mailMap.put("userId", loginUser.getUserId());
            List<MailVo> mailVoList = mailService.allListByMap(mailMap);
            for (int i = 0; i < mailVoList.size(); i++){

                Integer awardType = mailVoList.get(i).getAwardType(); // 签到奖励类型【1:代币  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备】
                Integer kindId = mailVoList.get(i).getKindId(); // 种类ID【例如：饲料表 一级饲料对应的ID；道具表 某个道具对应的ID】

                Map userMap = new HashMap();
                if (mailVoList.get(i).getType() == 1) {
                    if (awardType.equals(1)){
                        userMap.put("dogCoin", mailVoList.get(i).getAwardNum());
                        userMap.put("userId", loginUser.getUserId());
                        userService.update(userMap);
                    } else if (awardType.equals(2)){
                        userMap.put("residueMuscleNum", mailVoList.get(i).getAwardNum());
                        userMap.put("userId", loginUser.getUserId());
                        userService.update(userMap);
                    } else if (awardType.equals(3)){
                        receiveForage(kindId, loginUser.getUserId(), mailVoList.get(i).getAwardNum());
                    } else if (awardType.equals(4)){
                        receiveProp(kindId, loginUser.getUserId(), mailVoList.get(i).getAwardNum());
                    } else if (awardType.equals(5)){
                        receiveCatchEquip(kindId, loginUser.getUserId(), mailVoList.get(i).getAwardNum());
                    } else if (awardType.equals(6)){
                        receiveFightEquip(kindId, loginUser.getUserId(), mailVoList.get(i).getAwardNum());
                    }
                } else {
                    userMap.put("residueMuscleNum", mailVoList.get(i).getBrawnNum());
                    userMap.put("dogCoin", mailVoList.get(i).getAwardNum());
                    userMap.put("userId", loginUser.getUserId());
                    userService.update(userMap);
                }
            }

            Map map = new HashMap();
            map.put("isRead", 1);
            map.put("isReceive", 1);
            map.put("updateTime", new Date());
            map.put("userId", loginUser.getUserId());
            mailService.updateByUser(map);

            Map userMap = new HashMap();
            userMap.put("isHaveUnread", 2);
            userMap.put("userId", loginUser.getUserId());
            userService.update(userMap);

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsObject(400, "领取失败！", null);
    }

    /**
     * 一键删除已领取奖励邮件
     * @param loginUser
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation(value = "一键删除已领取奖励邮件")
    public Object deleteAllMail(@LoginUser UserVo loginUser) {

        try {

            Map mailMap = new HashMap();
            mailMap.put("isReceive", 1);
            mailMap.put("isRead", 1);
            mailMap.put("userId", loginUser.getUserId());
            List<MailVo> mailVoList = mailService.allListByMap(mailMap);
            for (int i = 0; i < mailVoList.size(); i++){

                Map map = new HashMap();
                map.put("isRead", 1);
                map.put("isReceive", 1);
                map.put("userId", loginUser.getUserId());
                mailService.deleteByUser(map);
            }

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("删除失败！");
    }

    /**
     * 领取饲料奖励
     * @param kindId
     * @param userId
     */
    private void receiveForage(Integer kindId, Long userId, Integer awardNum){

        ConfigForageVo configForageVo = configForageService.queryObject(kindId);

        for (int i = 0; i < awardNum; i++){
            UserForageVo forageVo = new UserForageVo();
            forageVo.setForageId(configForageVo.getId());
            forageVo.setGrade(configForageVo.getGrade());
            forageVo.setUserId(userId);
            forageVo.setForageName(configForageVo.getForageName());
            forageVo.setForageDesc(configForageVo.getForageDesc());
            forageVo.setImgName(configForageVo.getImgName());
            forageVo.setFightingNum(configForageVo.getFightingNum());
            forageVo.setForageType(configForageVo.getForageType());
            forageVo.setIsIgnoreTalent(configForageVo.getIsIgnoreTalent());
            forageVo.setForageNum(1);
            forageVo.setCreateTime(new Date());
            userForageService.save(forageVo);
        }
    }

    /**
     * 领取道具奖励
     * @param kindId
     * @param userId
     */
    private void receiveProp(Integer kindId, Long userId, Integer awardNum){

        ConfigPropVo configPropVo = configPropService.queryObject(kindId);

        for (int i = 0; i < awardNum; i++){
            UserPropVo propVo = new UserPropVo();
            propVo.setPropId(configPropVo.getId());
            propVo.setGrade(configPropVo.getGrade());
            propVo.setUserId(userId);
            propVo.setPropName(configPropVo.getPropName());
            propVo.setPropDesc(configPropVo.getPropDesc());
            propVo.setImgName(configPropVo.getImgName());
            propVo.setPropType(configPropVo.getPropType());
            propVo.setAttributeType(configPropVo.getAttributeType());
            propVo.setUseType(configPropVo.getUseType());
            propVo.setPropNum(1);
            propVo.setCreateTime(new Date());
            userPropService.save(propVo);
        }
    }

    /**
     * 领取捕捉装备奖励
     * @param kindId
     * @param userId
     */
    private void receiveCatchEquip(Integer kindId, Long userId, Integer awardNum){

        ConfigCatchEquipVo configCatchEquipVo = configCatchEquipMapper.queryObject(kindId);

        for (int i = 0; i < awardNum; i++){
            UserCatchEquipVo catchEquipVo = new UserCatchEquipVo();
            catchEquipVo.setEquipId(configCatchEquipVo.getId());
            catchEquipVo.setUserId(userId);
            catchEquipVo.setEquipName(configCatchEquipVo.getEquipName());
            catchEquipVo.setEquipDesc(configCatchEquipVo.getEquipDesc());
            catchEquipVo.setImgName(configCatchEquipVo.getImgName());
            catchEquipVo.setCatchType(configCatchEquipVo.getCatchType());
            catchEquipVo.setGrade(configCatchEquipVo.getGrade());
            catchEquipVo.setDeedType(configCatchEquipVo.getDeedType());
            catchEquipVo.setExtraOne(configCatchEquipVo.getExtraOne());
            catchEquipVo.setExtraTwo(configCatchEquipVo.getExtraTwo());
            catchEquipVo.setIsGem(configCatchEquipVo.getIsGem());
            catchEquipVo.setDurabilityMax(configCatchEquipVo.getDurability());
            catchEquipVo.setDurabilityResidue(configCatchEquipVo.getDurability());
            catchEquipVo.setEquipNum(1);
            catchEquipVo.setCreateTime(new Date());
            userCatchEquipService.save(catchEquipVo);
        }
    }

    /**
     * 领取战斗装备奖励
     * @param kindId
     * @param userId
     */
    private void receiveFightEquip(Integer kindId, Long userId, Integer awardNum){

        ConfigFightEquipVo configFightEquipVo = configFightEquipService.queryObject(kindId);

        for (int i = 0; i < awardNum; i++){
            UserFightEquipVo fightEquipVo = new UserFightEquipVo();
            fightEquipVo.setFightId(configFightEquipVo.getId());
            fightEquipVo.setUserId(userId);
            fightEquipVo.setFightName(configFightEquipVo.getFightName());
            fightEquipVo.setFightDesc(configFightEquipVo.getFightDesc());
            fightEquipVo.setImgName(configFightEquipVo.getImgName());
            fightEquipVo.setGrade(configFightEquipVo.getGrade());
            fightEquipVo.setFightingAddition(configFightEquipVo.getFightingAddition());
            fightEquipVo.setIsGem(configFightEquipVo.getIsGem());
            fightEquipVo.setDurabilityMax(configFightEquipVo.getDurability());
            fightEquipVo.setDurabilityResidue(configFightEquipVo.getDurability());
            fightEquipVo.setFightNum(awardNum);
            fightEquipVo.setCreateTime(new Date());
            userFightEquipService.save(fightEquipVo);
        }
    }
}
