package com.platform.api;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.annotation.LoginUser;
import com.platform.dao.ApiConfigWildMapper;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;


@Api(tags = "捕捉接口")
@RestController
@RequestMapping("/api")
public class ApiUserCatchController extends ApiBaseAction {

    @Autowired
    private ApiMapService mapService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiConfigEffectService configEffectService;

    @Autowired
    private ApiConfigDogService configDogService;
    @Autowired
    private ApiConfigForageService configForageService;
    @Autowired
    private ApiConfigCatchEquipService configCatchEquipService;
    @Autowired
    private ApiConfigFightEquipService configFightEquipService;
    @Autowired
    private ApiConfigPropService configPropService;
    @Autowired
    private ApiConfigWildMapper configWildMapper;


    @Autowired
    private ApiUserDogService userDogService;
    @Autowired
    private ApiUserForageService userForageService;
    @Autowired
    private ApiUserCatchEquipService userCatchEquipService;
    @Autowired
    private ApiUserFightEquipService userFightEquipService;
    @Autowired
    private ApiUserPropService userPropService;
    @Autowired
    private ApiUserWildService userWildService;

    @Autowired
    private ApiMailService mailService;

    @Autowired
    private ApiConfigMapPetRatioService mapPetRatioService;
    @Autowired
    private ApiConfigMapWildRatioService mapWildRatioService;
    @Autowired
    private ApiConfigMapForageRatioService mapForageRatioService;
    @Autowired
    private ApiConfigMapFightRatioService mapFightRatioService;
    @Autowired
    private ApiConfigMapCatchRatioService mapCatchRatioService;
    @Autowired
    private ApiConfigMapGemRatioService mapGemRatioService;

    @Autowired
    private ApiLogASGService logASGService;

    /**
     * 捕捉接口
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "捕捉接口")
    @PostMapping(value = "catch")
    public Object catchDog(@LoginUser UserVo loginUser) {

        try {

            // 创建带有权重的对象包装
            List<WeightRandom.WeightObj<String>> oneList = new ArrayList<WeightRandom.WeightObj<String>>();
            // 构造参数、设置比例
            oneList.add(new WeightRandom.WeightObj<>("success", 50));  // 可以进行捕捉 进入下一个概率池
            oneList.add(new WeightRandom.WeightObj<>("fail", 50));     // 直接捕捉失败
            // 创建带有权重的随机生成器
            WeightRandom oneWr = RandomUtil.weightRandom(oneList);

            // 查询当前用户使用地图对应数据
            MapVo mapVo = mapService.queryObject(loginUser.getMapId());
            // 减少用户身上对应体力数值
            if (loginUser.getResidueMuscleNum() < mapVo.getUseBrawn()) return toResponsFail("亲～您的体力不足");
            // 减少用户身上对应体力数值
            if (loginUser.getDogCoin() < mapVo.getUseAgs()) return toResponsFail("亲～您的AGS不足");

            // 判断是否使用捕捉装备
            Map params = new HashMap();
            params.put("isUse", 1);
            params.put("userId", loginUser.getUserId());

            // 查询用户是否有使用捕捉装备
            List<UserCatchEquipVo> catchEquipVoList = userCatchEquipService.queryList(params);
            // 如果有使用捕捉装备 则 进入下面方法
            if (catchEquipVoList.size() > 0) {
                for (int i = 0; i < catchEquipVoList.size(); i++){
                    // 减少用户使用的捕捉道具的耐久点数
                    if (catchEquipVoList.get(i).getDurabilityResidue() == 1){
                        userCatchEquipService.deleteByUser(loginUser.getUserId(), catchEquipVoList.get(i).getId());
                    } else {
                        userCatchEquipService.makeUseOf(loginUser.getUserId(), catchEquipVoList.get(i).getId(), 1);
                    }
                }
            }

            userService.useBrawnNum(loginUser.getUserId(), mapVo.getUseBrawn());
            userService.useDogCoinNum(loginUser.getUserId(), mapVo.getUseAgs());

            Map userMap = new HashMap();
            userMap.put("daysUseBrawn", loginUser.getDaysUseBrawn() + mapVo.getUseAgs());
            userMap.put("userId", loginUser.getUserId());
            userService.update(userMap);

            LogASGVo logASGVo = new LogASGVo();
            logASGVo.setUserId(loginUser.getUserId());
            logASGVo.setNum(new BigDecimal(mapVo.getUseAgs()));
            logASGVo.setLogType(3);
            logASGVo.setLogTypeTxt("捕捉消耗");
            logASGVo.setAsgType(1);
            logASGVo.setAgsTypeTxt("消耗");
            logASGVo.setCreateTime(new Date());
            logASGVo.setRemarks("捕捉消耗" + mapVo.getUseAgs() + "ASG");
            logASGVo.setAddress(loginUser.getAddress());
            logASGService.save(logASGVo);

            Map resultMap = new HashMap();
            resultMap.put("type", 7);
            if("fail".equals(oneWr.next().toString())) return toResponsSuccess(resultMap);

            // 创建带有权重的对象包装
            List<WeightRandom.WeightObj<String>> list = new ArrayList<WeightRandom.WeightObj<String>>();
            // 构造参数、设置比例
            list.add(new WeightRandom.WeightObj<>("宠物", mapVo.getPetRatio().doubleValue()));     // 宠物
            list.add(new WeightRandom.WeightObj<>("装备", mapVo.getEquipRatio().doubleValue()));   // 装备
            list.add(new WeightRandom.WeightObj<>("饲料", mapVo.getForageRatio().doubleValue()));  // 饲料
            list.add(new WeightRandom.WeightObj<>("珍宝", mapVo.getGemRatio().doubleValue()));     // 珍宝
            list.add(new WeightRandom.WeightObj<>("野生", mapVo.getWildRatio().doubleValue()));    // 野生

            // 创建带有权重的随机生成器
            WeightRandom wr = RandomUtil.weightRandom(list);
            String randomStr = wr.next().toString();

            System.out.println("您捕捉到: " + randomStr);

            if("宠物".equals(randomStr)){           // 捕捉到狗狗
                UserDogVo userDogVo = receiveDog(mapVo.getId(), loginUser.getUserId(), mapVo.getAttributeNum());
                userDogVo.setType(1);
                userDogVo.setDiscardType(1);
                return toResponsSuccess(userDogVo);
            } else if ("装备".equals(randomStr)){   // 捕捉到装备

                // ObjectMapper实例
                ObjectMapper objectMapper = new ObjectMapper();
                // 转化为实体类PartnerRegisterBo
                Map tempMap = allEquipOdds(mapVo.getId());
                // type = 1 对战装备 否则 捕捉装备
                if (tempMap.get("type").toString().equals("1")){
                    ConfigFightEquipVo configFightEquipVo = objectMapper.convertValue(tempMap.get("equip"), ConfigFightEquipVo.class);
                    UserFightEquipVo userFightEquipVo = receiveFightEquip(loginUser.getUserId(), configFightEquipVo,1);
                    userFightEquipVo.setType(6);
                    userFightEquipVo.setDiscardType(6);
                    return toResponsSuccess(userFightEquipVo);
                } else {
                    ConfigCatchEquipVo configCatchEquipVo = objectMapper.convertValue(tempMap.get("equip"), ConfigCatchEquipVo.class);
                    UserCatchEquipVo userCatchEquipVo = receiveCatchEquip(loginUser.getUserId(), configCatchEquipVo,1);
                    userCatchEquipVo.setType(5);
                    userCatchEquipVo.setDiscardType(5);
                    return toResponsSuccess(userCatchEquipVo);
                }
            } else if ("饲料".equals(randomStr)){   // 捕捉到饲料

                ConfigForageVo configForageVo = allForageOdds(mapVo.getId());
                UserForageVo userForageVo = receiveForage(loginUser.getUserId(), configForageVo, 1);
                userForageVo.setType(3);
                userForageVo.setDiscardType(3);
                return toResponsSuccess(userForageVo);
            } else if ("珍宝".equals(randomStr)){   // 捕捉到珍宝

                Integer type = allGemOdds(mapVo.getId());
                return toResponsSuccess(receiveWildGem(loginUser.getUserId(), type));
            } else if ("野生".equals(randomStr)){   // 捕捉到野生

                UserWildVo userWildVo = receiveWild(loginUser.getUserId(), mapVo.getId(), 1);
                userWildVo.setType(4);
                userWildVo.setDiscardType(4);

                return toResponsSuccess(userWildVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("捕捉失败！");
    }

    /**
     * 捕捉 狗狗 成功
     * @param mapId 地图ID
     * @param userId 用户userId
     * @param attributeNum 宠物速度、心情、耐力、幸运总值
     * @return
     */
    private UserDogVo receiveDog(Integer mapId, Long userId, Integer attributeNum){

        ConfigDogVo configDogVo = allDogOdds(mapId);

        UserDogVo dogVo = new UserDogVo();
        dogVo.setDogId(configDogVo.getId());
        dogVo.setDogGrade(configDogVo.getDogGrade());
        dogVo.setDogName(configDogVo.getDogName());
        dogVo.setAnimationName(configDogVo.getAnimationName());
        dogVo.setDogDesc(configDogVo.getDogDesc());
        dogVo.setImgName(configDogVo.getImgName());
        dogVo.setDogBreed(configDogVo.getDogBreed());
        dogVo.setFightingNum(configDogVo.getInitialFightingNum());
        dogVo.setMaxGrowUpNum(configDogVo.getGrowUpNum());
        dogVo.setInbornNum(configDogVo.getInbornNum());

        Random r = new Random();
        List<Integer> battle = new ArrayList<>();
        //判断集合的长度是不是小于4
        while (battle.size() < 4) {
            //产生一个随机数，添加到集合
            int number = r.nextInt(8) % (8 - 3 + 1) + 3;
            battle.add(number);
        }

        int minNum = 0;
        switch (attributeNum){
            case 6:
            case 8:
            case 10:
                minNum = 1;
                break;
            case 12:
            case 14:
            case 16:
                minNum = 2;
                break;
            case 18:
            case 20:
                minNum = 3;
                break;
            case 22:
                minNum = 4;
                break;
            case 24:
                minNum = 5;
                break;
            default:
                minNum = 1;
                break;
        }
        List<Integer> tempList = getWeight(4, attributeNum, minNum);
        dogVo.setSpeed(tempList.get(0));
        dogVo.setMood(tempList.get(1));
        dogVo.setEndurance(tempList.get(2));
        dogVo.setLuck(tempList.get(3));

        dogVo.setUserId(userId);
        dogVo.setCreateTime(new Date());
        userDogService.save(dogVo);

        return dogVo;
    }

    /**
     * 捕捉 野生 成功
     * @param userId 用户id
     * @param mapId 地图ID
     * @param awardNum 奖励数量
     * @return
     */
    private UserWildVo receiveWild(Long userId, Integer mapId, Integer awardNum){

        ConfigWildVo configWildVo = allWildOdds(mapId);;

        UserWildVo wildVo = new UserWildVo();
        wildVo.setWildId(configWildVo.getId());
        wildVo.setGrade(configWildVo.getGrade());
        wildVo.setUserId(userId);
        wildVo.setWildName(configWildVo.getWildName());
        wildVo.setWildDesc(configWildVo.getWildDesc());
        wildVo.setImgName(configWildVo.getImgName());
        wildVo.setFightingNum(configWildVo.getFightingNum());
        wildVo.setIsFight(configWildVo.getIsFight());
        wildVo.setWildType(configWildVo.getWildType());
        wildVo.setIsIgnoreTalent(configWildVo.getIsIgnoreTalent());
        wildVo.setDurabilityMax(configWildVo.getDurability());
        wildVo.setDurabilityResidue(configWildVo.getDurability());
        wildVo.setWildNum(awardNum);
        wildVo.setCreateTime(new Date());
        userWildService.save(wildVo);
        return wildVo;
    }

    /**
     * 捕捉 珍宝 成功
     * @param userId
     * @param type
     * @return
     */
    private Map receiveWildGem(Long userId, Integer type){

        Random rand = new Random();
        Map map = new HashMap();
        Map resultMap = new HashMap();
        if (type.equals(1)){ // 饲料珍宝
            map.put("forageType", 2);
            List<ConfigForageVo> configForageVo = configForageService.queryListByMap(map);
            UserForageVo userForageVo = receiveForage(userId, configForageVo.get(rand.nextInt(configForageVo.size())), 1);
            resultMap.put("id", userForageVo.getId());
            resultMap.put("name", userForageVo.getForageName());
            resultMap.put("desc", userForageVo.getForageDesc());
            resultMap.put("image", userForageVo.getImgName());
            resultMap.put("number", 1);
            resultMap.put("discardType", 3);
        } else if (type.equals(2)){ // 道具珍宝
            map.put("propType", 2);
            List<ConfigPropVo> configPropVo = configPropService.queryListByMap(map);
            UserPropVo userPropVo = receiveProp(userId, configPropVo.get(rand.nextInt(configPropVo.size())), 1);
            resultMap.put("id", userPropVo.getId());
            resultMap.put("name", userPropVo.getPropName());
            resultMap.put("desc", userPropVo.getPropDesc());
            resultMap.put("image", userPropVo.getImgName());
            resultMap.put("number", 1);
            resultMap.put("discardType", 2);
        } else if (type.equals(3)){ // 捕捉装备珍宝
            map.put("isGem", 1);
            List<ConfigCatchEquipVo> configCatchEquipVo = configCatchEquipService.queryListByMap(map);
            UserCatchEquipVo userCatchEquipVo = receiveCatchEquip(userId, configCatchEquipVo.get(rand.nextInt(configCatchEquipVo.size())), 1);
            resultMap.put("id", userCatchEquipVo.getId());
            resultMap.put("name", userCatchEquipVo.getEquipName());
            resultMap.put("desc", userCatchEquipVo.getEquipDesc());
            resultMap.put("image", userCatchEquipVo.getImgName());
            resultMap.put("number", 1);
            resultMap.put("discardType", 5);
        } else if (type.equals(4)){ // 对战装备珍宝
            map.put("isGem", 1);
            List<ConfigFightEquipVo> configFightEquipVo = configFightEquipService.queryListByMap(map);
            UserFightEquipVo userFightEquipVo = receiveFightEquip(userId, configFightEquipVo.get(rand.nextInt(configFightEquipVo.size())), 1);
            resultMap.put("id", userFightEquipVo.getId());
            resultMap.put("name", userFightEquipVo.getFightName());
            resultMap.put("desc", userFightEquipVo.getFightDesc());
            resultMap.put("image", userFightEquipVo.getImgName());
            resultMap.put("number", 1);
            resultMap.put("discardType", 6);
        }

        resultMap.put("type", 2);
        return resultMap;
    }

    /**
     * 捕捉 饲料 成功
     * @param userId
     * @param configForageVo
     * @param awardNum
     * @return
     */
    private UserForageVo receiveForage(Long userId, ConfigForageVo configForageVo, Integer awardNum){

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
        forageVo.setForageNum(awardNum);
        forageVo.setCreateTime(new Date());
        userForageService.save(forageVo);

        return forageVo;
    }

    /**
     * 捕捉 道具 成功
     * @param userId
     * @param configPropVo
     * @param awardNum
     * @return
     */
    private UserPropVo receiveProp(Long userId, ConfigPropVo configPropVo, Integer awardNum){

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
        propVo.setPropNum(awardNum);
        propVo.setCreateTime(new Date());
        userPropService.save(propVo);

        return propVo;
    }

    /**
     * 捕捉 捕捉装备 成功
     * @param userId
     * @param configCatchEquipVo
     * @param awardNum
     * @return
     */
    private UserCatchEquipVo receiveCatchEquip(Long userId, ConfigCatchEquipVo configCatchEquipVo, Integer awardNum){

        UserCatchEquipVo catchEquipVo = new UserCatchEquipVo();

        catchEquipVo.setEquipId(configCatchEquipVo.getId());
        catchEquipVo.setUserId(userId);
        catchEquipVo.setEquipName(configCatchEquipVo.getEquipName());
        catchEquipVo.setEquipDesc(configCatchEquipVo.getEquipDesc());
        catchEquipVo.setImgName(configCatchEquipVo.getImgName());
        catchEquipVo.setCatchType(configCatchEquipVo.getCatchType());
        catchEquipVo.setGrade(configCatchEquipVo.getGrade());
        catchEquipVo.setDeedType(configCatchEquipVo.getDeedType());

        if (configCatchEquipVo.getExtraOne() == 1){
            catchEquipVo.setExtraOne(getEffect());
        } else {
            catchEquipVo.setExtraOne(configCatchEquipVo.getExtraOne());
        }

        if (configCatchEquipVo.getExtraTwo() == 1){
            catchEquipVo.setExtraTwo(getEffect());
        } else {
            catchEquipVo.setExtraTwo(configCatchEquipVo.getExtraTwo());
        }

        catchEquipVo.setIsGem(configCatchEquipVo.getIsGem());
        catchEquipVo.setDurabilityMax(configCatchEquipVo.getDurability());
        catchEquipVo.setDurabilityResidue(configCatchEquipVo.getDurability());
        catchEquipVo.setEquipNum(awardNum);
        catchEquipVo.setCreateTime(new Date());
        userCatchEquipService.save(catchEquipVo);

        return catchEquipVo;
    }

    /**
     * 捕捉 战斗装备 成功
     * @param userId
     * @param configFightEquipVo
     * @param awardNum
     * @return
     */
    private UserFightEquipVo receiveFightEquip(Long userId, ConfigFightEquipVo configFightEquipVo, Integer awardNum){

        UserFightEquipVo fightEquipVo = new UserFightEquipVo();
        fightEquipVo.setFightId(configFightEquipVo.getId());
        fightEquipVo.setUserId(userId);
        fightEquipVo.setFightName(configFightEquipVo.getFightName());
        fightEquipVo.setFightDesc(configFightEquipVo.getFightDesc());
        fightEquipVo.setImgName(configFightEquipVo.getImgName());
        fightEquipVo.setGrade(configFightEquipVo.getGrade());
        fightEquipVo.setFightingAddition(configFightEquipVo.getFightingAddition());
        fightEquipVo.setIsGem(configFightEquipVo.getIsGem());
        fightEquipVo.setFightType(configFightEquipVo.getFightType());
        fightEquipVo.setDurabilityMax(configFightEquipVo.getDurability());
        fightEquipVo.setDurabilityResidue(configFightEquipVo.getDurability());
        fightEquipVo.setFightNum(awardNum);
        fightEquipVo.setCreateTime(new Date());
        userFightEquipService.save(fightEquipVo);

        return fightEquipVo;
    }

    /**
     * 丢弃物品接口
     */
    @ApiOperation(value = "丢弃类型 【1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备】")
    @PostMapping(value = "discard")
    private Object discardDog(@LoginUser UserVo loginUser, @RequestParam(value = "type", defaultValue = "1") Integer type, @RequestParam(value = "id", defaultValue = "0") Integer id){

        try {
            if (type == 0 || id == 0) return toResponsObject(400,"丢弃失败！",null);

            // 创建随机数对象
            Random r = new Random();
            // 产生一个随机数 20 ~ 120
            int number = r.nextInt(120) % (120 - 20 + 1) + 20;
            // 计算返还AGS数量
            BigDecimal ags = new BigDecimal(100).multiply(new BigDecimal(number).divide(new BigDecimal(100)));

            MailVo mailVo = new MailVo();
            mailVo.setMailTitle("丢弃返还");

            if(type == 1){
                UserDogVo userDogVo = userDogService.queryObjectByUser(loginUser.getUserId(), id);
                if (userDogVo == null) return toResponsObject(400,"宠物查询失败，请重试！",null);
                mailVo.setMailContent("您丢弃一只 " + userDogVo.getDogName() + " 系统返还您：" + ags.intValue() + " " + "ASG");
                userDogService.deleteByUser(loginUser.getUserId(), id);
            } else if(type == 2){
                UserPropVo userPropVo = userPropService.queryObjectByUser(loginUser.getUserId(), id);
                if (userPropVo == null) return toResponsObject(400,"道具查询失败，请重试！",null);
                mailVo.setMailContent("您丢弃一个 " + userPropVo.getPropNum() + " 系统返还您：" + ags.intValue() + " " + "ASG");
                userPropService.deleteByUser(loginUser.getUserId(), id);
            } else if(type == 3){
                UserForageVo userForageVo = userForageService.queryObjectByUser(loginUser.getUserId(), id);
                if (userForageVo == null) return toResponsObject(400,"饲料查询失败，请重试！",null);
                mailVo.setMailContent("您丢弃一个 " + userForageVo.getForageName() + " 系统返还您：" + ags.intValue() + " " + "ASG");
                userForageService.deleteByUser(loginUser.getUserId(), id);
            } else if(type == 4){
                UserWildVo userWildVo = userWildService.queryObjectByUser(loginUser.getUserId(), id);
                if (userWildVo == null) return toResponsObject(400,"装备查询失败，请重试！",null);
                mailVo.setMailContent("您丢弃一个 " + userWildVo.getWildName() + " 系统返还您：" + ags.intValue() + " " + "ASG");
                userWildService.deleteByUser(loginUser.getUserId(), id);
            } else if(type == 5){
                UserCatchEquipVo userCatchEquipVo = userCatchEquipService.queryObjectByUser(loginUser.getUserId(), id);
                if (userCatchEquipVo == null) return toResponsObject(400,"捕捉装备查询失败，请重试！",null);
                mailVo.setMailContent("您丢弃一件 " + userCatchEquipVo.getEquipName() + "装备 系统返还您：" + ags.intValue() + " " + "ASG");
                userCatchEquipService.deleteByUser(loginUser.getUserId(), id);
            } else if(type == 6){
                UserFightEquipVo userFightEquipVo = userFightEquipService.queryObjectByUser(loginUser.getUserId(), id);
                if (userFightEquipVo == null) return toResponsObject(400,"装备查询失败，请重试！",null);
                mailVo.setMailContent("您丢弃一件 " + userFightEquipVo.getFightName() + "装备 系统返还您：" + ags.intValue() + " " + "ASG");
                userFightEquipService.deleteByUser(loginUser.getUserId(), id);
            }

            mailVo.setImgName("jinbi");
            mailVo.setAwardNum(ags.intValue());
            mailVo.setIsAttribute(0);
            mailVo.setAwardType(1); // 代币/ASG
            mailVo.setUserId(loginUser.getUserId());
            mailVo.setCreateTime(new Date());
            mailVo.setType(1);
            mailVo.setIsReceive(0);
            mailVo.setIsDelete(0);
            mailService.save(mailVo);

            Map map = new HashMap();
            map.put("userId", loginUser.getUserId());
            map.put("isHaveUnread", 1);
            userService.update(map);

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("丢弃失败！");
    }

    public static List<Integer> getWeight(int num, int max, int min) {

        List<Integer> result = new ArrayList<Integer>();
        if (num == 1) {
            result.add(max);
            return result;
        } else {
            int num1 = getRandom(min, (max / num + 1));
            result.add(num1);
            int total = max;
            for (int i = 1; i < num; i++) {
                total = total - num1;
                while (total < min) {
                    int maxc = Collections.max(result);
                    result.set(result.indexOf(maxc), min);
                    int s = result.stream().map(e -> e).reduce(Integer::sum).get();
                    total = max - s;
                }
                if (i != num - 1) {
                    num1 = getRandom(min, total);
                    result.add(num1);
                } else {
                    result.add(total);
                }
            }
        }
        return result;
    }

    public static int getRandom(int MIN, int MAX) {
        Random random = new Random();
        return random.nextInt(MAX - MIN + 1) + MIN;
    }

    /**
     * 获取额外效果
     * @return
     */
    public int getEffect(){

        List<ConfigEffectVo> effectVoList = configEffectService.allEffect();

        // 创建带有权重的对象包装
        List<WeightRandom.WeightObj<String>> effectList = new ArrayList<WeightRandom.WeightObj<String>>();
        effectList.add(new WeightRandom.WeightObj<>("effect", 0.5));
        for (int i = 0; i < effectVoList.size(); i++){
            // 构造参数、设置比例
            effectList.add(new WeightRandom.WeightObj<>("effect" + i, effectVoList.get(i).getRatio().doubleValue()));
        }

        // 创建带有权重的随机生成器
        WeightRandom wr = RandomUtil.weightRandom(effectList);
        String randomStr = wr.next().toString();

        for (int i = 0; i < effectVoList.size(); i++){
            if (randomStr.equals("effect" + i)){
                return effectVoList.get(i).getId();
            }
        }
        return 0;
    }

    /**
     * 按配置随机产出各品质狗狗
     * @param mapId
     * @return
     */
    public ConfigDogVo allDogOdds(Integer mapId){

        List<Map> tempList = new ArrayList<>();
        ConfigMapPetRatioVo mapPetRatioVo = mapPetRatioService.queryByMapId(mapId);
        Map map = new HashMap();
        map.put("grade", 1);
        map.put("weight", mapPetRatioVo.getPetGrade01());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 2);
        map.put("weight", mapPetRatioVo.getPetGrade02());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 3);
        map.put("weight", mapPetRatioVo.getPetGrade03());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 4);
        map.put("weight", mapPetRatioVo.getPetGrade04());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 5);
        map.put("weight", mapPetRatioVo.getPetGrade05());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 6);
        map.put("weight", mapPetRatioVo.getPetGrade06());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 7);
        map.put("weight", mapPetRatioVo.getPetGrade07());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 8);
        map.put("weight", mapPetRatioVo.getPetGrade08());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 9);
        map.put("weight", mapPetRatioVo.getPetGrade09());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 10);
        map.put("weight", mapPetRatioVo.getPetGrade10());
        tempList.add(map);

        Integer grade = lottery(tempList);
        return configDogService.queryObjectByGrade(grade);
    }

    /**
     * 按配置随机产出各品质饲料
     * @param mapId
     * @return
     */
    private ConfigForageVo allForageOdds(Integer mapId){

        List<Map> tempList = new ArrayList<>();
        ConfigMapForageRatioVo mapRatio = mapForageRatioService.queryByMapId(mapId);
        Map map = new HashMap();
        map.put("grade", 1);
        map.put("weight", mapRatio.getForageGrade01());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 2);
        map.put("weight", mapRatio.getForageGrade02());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 3);
        map.put("weight", mapRatio.getForageGrade03());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 4);
        map.put("weight", mapRatio.getForageGrade04());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 5);
        map.put("weight", mapRatio.getForageGrade05());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 6);
        map.put("weight", mapRatio.getForageGrade06());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 7);
        map.put("weight", mapRatio.getForageGrade07());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 8);
        map.put("weight", mapRatio.getForageGrade08());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 9);
        map.put("weight", mapRatio.getForageGrade09());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 10);
        map.put("weight", mapRatio.getForageGrade10());
        tempList.add(map);

        Integer grade = lottery(tempList);
        Map params = new HashMap();
        params.put("grade", grade);
        params.put("forageType", 1);
        return configForageService.queryObjectByMap(params);
    }

    /**
     * 按配置随机产出各品质装备 （对战装备、捕捉装备）
     * @param mapId
     * @return
     */
    private Map allEquipOdds(Integer mapId){

        List<Map> tempList = new ArrayList<>();
        ConfigMapFightRatioVo mapFightRatio = mapFightRatioService.queryByMapId(mapId);
        ConfigMapCatchRatioVo mapCatchRatio = mapCatchRatioService.queryByMapId(mapId);

        Map map = new HashMap();
        map.put("grade", 1);
        map.put("fightWeight", mapFightRatio.getEquipGrade01());
        map.put("catchWeight", mapCatchRatio.getEquipGrade01());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 2);
        map.put("fightWeight", mapFightRatio.getEquipGrade02());
        map.put("catchWeight", mapCatchRatio.getEquipGrade02());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 3);
        map.put("fightWeight", mapFightRatio.getEquipGrade03());
        map.put("catchWeight", mapCatchRatio.getEquipGrade03());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 4);
        map.put("fightWeight", mapFightRatio.getEquipGrade04());
        map.put("catchWeight", mapCatchRatio.getEquipGrade04());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 5);
        map.put("fightWeight", mapFightRatio.getEquipGrade05());
        map.put("catchWeight", mapCatchRatio.getEquipGrade05());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 6);
        map.put("fightWeight", mapFightRatio.getEquipGrade06());
        map.put("catchWeight", mapCatchRatio.getEquipGrade06());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 7);
        map.put("fightWeight", mapFightRatio.getEquipGrade07());
        map.put("catchWeight", mapCatchRatio.getEquipGrade07());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 8);
        map.put("fightWeight", mapFightRatio.getEquipGrade08());
        map.put("catchWeight", mapCatchRatio.getEquipGrade08());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 9);
        map.put("fightWeight", mapFightRatio.getEquipGrade09());
        map.put("catchWeight", mapCatchRatio.getEquipGrade09());
        tempList.add(map);
        map = new HashMap();
        map.put("grade", 10);
        map.put("fightWeight", mapFightRatio.getEquipGrade10());
        map.put("catchWeight", mapCatchRatio.getEquipGrade10());
        tempList.add(map);

        return lotteryTwo(tempList);
    }

    /**
     * 按配置随机产出各品质野生
     * @param mapId
     * @return
     */
    public ConfigWildVo allWildOdds(Integer mapId){

        List<Map> tempList = new ArrayList<>();
        ConfigMapWildRatioVo mapRatio = mapWildRatioService.queryByMapId(mapId);
        Map map = new HashMap();
        map.put("id", 1);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);
        map = new HashMap();
        map.put("id", 2);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);
        map = new HashMap();
        map.put("id", 3);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);
        map = new HashMap();
        map.put("id", 4);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);
        map = new HashMap();
        map.put("id", 5);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);
        map = new HashMap();
        map.put("id", 6);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);
        map = new HashMap();
        map.put("id", 7);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);
        map = new HashMap();
        map.put("id", 8);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);
        map = new HashMap();
        map.put("id", 9);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);
        map = new HashMap();
        map.put("id", 10);
        map.put("weight", mapRatio.getWild());
        tempList.add(map);

        Integer id = lotteryThree(tempList);
        return configWildMapper.queryObject(id);
    }

    /**
     * 按配置随机产出各品质珍宝
     * @param mapId
     * @return
     */
    private int allGemOdds(Integer mapId){

        ConfigMapGemRatioVo mapRatio = mapGemRatioService.queryByMapId(mapId);

        List<Map> prizeList = new ArrayList<>();
        Map map = new HashMap();
        map.put("grade", 1);
        map.put("forageWeight", mapRatio.getGemForage());
        map.put("equipWeight", mapRatio.getGemEquip());
        map.put("propWeight", mapRatio.getGemProp());

        List<WeightRandom.WeightObj<String>> list = new ArrayList<WeightRandom.WeightObj<String>>();
        list.add(new WeightRandom.WeightObj<>("forageWeight", Integer.parseInt(map.get("forageWeight").toString())));
        list.add(new WeightRandom.WeightObj<>("equipWeight", Integer.parseInt(map.get("equipWeight").toString())));
        list.add(new WeightRandom.WeightObj<>("propWeight", Integer.parseInt(map.get("propWeight").toString())));

        // 创建带有权重的随机生成器
        WeightRandom wr = RandomUtil.weightRandom(list);
        String randomStr = wr.next().toString();

        if (randomStr.equals("forageWeight")){ // 饲料珍宝
            return 1;
        } else if (randomStr.equals("propWeight")){ // 道具珍宝
            return 2;
        } else if (randomStr.equals("equipWeight")){ // 捕捉/对战装备珍宝
            Random r = new Random();
            int number = r.nextInt(4) % (4 - 3 + 1) + 3;
            return number;
        }
        return 0;
    }

    /**
     * 获得的品质
     * @param prizeList
     * @return
     */
    public int lottery(List<Map> prizeList) {

        List<WeightRandom.WeightObj<Integer>> list = new ArrayList<WeightRandom.WeightObj<Integer>>();
        for (int i = 0; i < prizeList.size(); i++){
            list.add(new WeightRandom.WeightObj<>(Integer.parseInt(prizeList.get(i).get("grade").toString()), Integer.parseInt(prizeList.get(i).get("weight").toString())));
        }

        // 创建带有权重的随机生成器
        WeightRandom wr = RandomUtil.weightRandom(list);
        Integer grade = Integer.parseInt(wr.next().toString());
        return grade;
    }

    /**
     * 获得的品质
     * @param prizeList
     * @return
     */
    public Map lotteryTwo(List<Map> prizeList) {

        List<WeightRandom.WeightObj<Map>> list = new ArrayList<WeightRandom.WeightObj<Map>>();
        for (int i = 0; i < prizeList.size(); i++){
            list.add(new WeightRandom.WeightObj<>(prizeList.get(i), Integer.parseInt(prizeList.get(i).get("fightWeight").toString())));
            list.add(new WeightRandom.WeightObj<>(prizeList.get(i), Integer.parseInt(prizeList.get(i).get("catchWeight").toString())));
        }

        // 创建带有权重的随机生成器
        WeightRandom wr = RandomUtil.weightRandom(list);
        JSONObject json=(JSONObject) JSONObject.toJSON(wr.next());

        Map returnMap = new HashMap();

        if (Integer.parseInt(json.get("fightWeight").toString()) > 0){ // 获得对战装备
            Random r = new Random();
            int number = r.nextInt(3) % (3 - 1 + 1) + 1;
            returnMap.put("type", 1);
            returnMap.put("equip", configFightEquipService.queryObjectByType(Integer.parseInt(json.get("grade").toString()), number));
        } else {
            Random r = new Random();
            int number = r.nextInt(4) % (4 - 1 + 1) + 1;
            returnMap.put("type", 2);
            returnMap.put("equip", configCatchEquipService.queryObjectByType(Integer.parseInt(json.get("grade").toString()), number));
        }

        return returnMap;
    }

    /**
     * 获得的品质
     * @param prizeList
     * @return
     */
    public int lotteryThree(List<Map> prizeList) {

        List<WeightRandom.WeightObj<Integer>> list = new ArrayList<WeightRandom.WeightObj<Integer>>();
        for (int i = 0; i < prizeList.size(); i++){
            list.add(new WeightRandom.WeightObj<>(Integer.parseInt(prizeList.get(i).get("id").toString()), Integer.parseInt(prizeList.get(i).get("weight").toString())));
        }

        // 创建带有权重的随机生成器
//        WeightRandom wr = RandomUtil.weightRandom(list);
//        Integer id = Integer.parseInt(wr.next().toString());

        List<ConfigWildVo> wildVoList = configWildMapper.allWild();
        Random r = new Random();
        int number = r.nextInt(wildVoList.size()) % (wildVoList.size() - 1 + 1) + 1;

        return number;
    }
}
