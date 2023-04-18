package com.platform.api;

import com.platform.annotation.LoginUser;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.util.EffectiveTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.platform.config.ConstantConfig.*;

/**
 * @program: platform
 * @description: 比赛预匹配
 * @author: Yuan
 * @create: 2020-08-13 17:34
 **/
@Api(tags = "比赛预匹配")
@RestController
@RequestMapping("/api/mate")
public class ApiPreMatchController extends ApiBaseAction {

    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiUserDogService userDogService;
    @Autowired
    private ApiUserFightEquipService userFightEquipService;
    @Autowired
    private ApiUserPropService userPropService;
    @Autowired
    private ApiUserWildService userWildService;

    /***
     * @Description: 报名比赛
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "报名比赛")
    @PostMapping("enroll")
    public Object takePart(@LoginUser UserVo loginUser, @RequestParam("dogId") Integer dogId, @RequestParam(value = "equip01", defaultValue = "0") Integer equipOne, @RequestParam(value = "equip02", defaultValue = "0") Integer equipTwo, @RequestParam(value = "equip03", defaultValue = "0") Integer equipThree,
                           @RequestParam(value = "equipType01", defaultValue = "0") Integer equipType01, @RequestParam(value = "equipType02", defaultValue = "0") Integer equipType02, @RequestParam(value = "equipType03", defaultValue = "0") Integer equipType03,
                           @RequestParam(value = "brawn", defaultValue = "0") Integer brawn, @RequestParam(value = "gameCard", defaultValue = "0") Integer gameCard, @RequestParam(value = "fight", defaultValue = "0") Integer fight, @RequestParam(value = "cool", defaultValue = "0") Integer cool) {

        if (!EffectiveTimeUtil.timeCalendar()) return toResponsObject(400,"当前不在参赛时间", null);

        Map map = new HashMap();
        map.put("dogId", dogId);
        map.put("userId", loginUser.getUserId());
        UserDogVo dogInfo = userDogService.queryUserDogInfo(map); // 查询狗狗信息

        if (brawn > 0){
            UserPropVo userPropVo = userPropService.queryObjectByUser(loginUser.getUserId(), brawn);
            if (userPropVo == null) return toResponsObject(400,"您没有精力卡", null);
        }

        if (gameCard > 0){
            UserPropVo userPropVo = userPropService.queryObjectByUser(loginUser.getUserId(), gameCard);
            if (userPropVo == null) return toResponsObject(400,"您没有参赛卡", null);
        }

        if (fight > 0){
            UserPropVo userPropVo = userPropService.queryObjectByUser(loginUser.getUserId(), fight);
            if (userPropVo == null) return toResponsObject(400,"您没有战力提升卡", null);
        }

        if (cool > 0){
            UserPropVo userPropVo = userPropService.queryObjectByUser(loginUser.getUserId(), cool);
            if (userPropVo == null) return toResponsObject(400,"您没有冷却卡", null);
        }

        // 判断该用户是有狗狗否比赛中
//        if (dogInfo.getIsGame() == 1) return toResponsObject(1,"亲～您有一只狗狗正在比赛中", null);

        // 判断该狗狗是否在冷却中
        if (dogInfo.getIsCool() == 1) return toResponsObject(400,"亲～您的狗狗正在冷却中", null);

        System.out.println(brawn);
        System.out.println(gameCard);
        System.out.println(fight);
        System.out.println(cool);
        if (brawn > 0){
            // 减少道具数量
            userPropService.makeUseOf(loginUser.getUserId(), brawn, 1);
        }
        if (gameCard > 0){
            // 减少道具数量
            userPropService.makeUseOf(loginUser.getUserId(), gameCard, 1);
        }
        if (fight > 0){
            // 减少道具数量
            userPropService.makeUseOf(loginUser.getUserId(), fight, 1);
        }
        if (cool > 0){
            // 减少道具数量
            userPropService.makeUseOf(loginUser.getUserId(), cool, 1);
        }

        if (brawn == 0){
            // 减少用户身上对应体力数值
            if (loginUser.getResidueMuscleNum() < 5) return toResponsObject(1,"亲～您的体力不足", null);
            userService.useBrawnNum(loginUser.getUserId(), 5); // 扣除用户体力

            Map userMap = new HashMap();
            userMap.put("daysUseBrawn", loginUser.getDaysUseBrawn() + 5);
            userMap.put("userId", loginUser.getUserId());
            userService.update(userMap);
        }

        if (gameCard == 0){
            // 减少用户身上对应狗币/代币数值
            if (loginUser.getDogCoin() < 1000) return toResponsObject(1,"亲～您的ASG不足", null);
            userService.useDogCoinNum(loginUser.getUserId(), 1000); // 扣除用户AGS
        }

        // 增加装备战斗力
        BigDecimal addFightNum = BigDecimal.ZERO;
        if (equipOne > 0) {
            if(equipType01 == 1){
                UserFightEquipVo userFightEquipOne = userFightEquipService.queryObjectByUser(loginUser.getUserId(), equipOne);
                addFightNum = addFightNum.add(dogInfo.getFightingNum().multiply(userFightEquipOne.getFightingAddition().divide(new BigDecimal(100))));
                userFightEquipService.makeUseOf(loginUser.getUserId(), equipOne, 1);
            } else if (equipType01 == 2){
                UserWildVo userWildOne = userWildService.queryObjectByUser(loginUser.getUserId(), equipOne);
                addFightNum = userWildOne.getFightingNum().multiply(dogInfo.getInbornNum());
                userWildService.makeUseOf(loginUser.getUserId(), equipOne, 1);
            }
        }
        if (equipTwo > 0) {
            if (equipType02 == 1){
                UserFightEquipVo userFightEquipTwo = userFightEquipService.queryObjectByUser(loginUser.getUserId(), equipTwo);
                addFightNum = addFightNum.add(dogInfo.getFightingNum().multiply(userFightEquipTwo.getFightingAddition().divide(new BigDecimal(100))));
                userFightEquipService.makeUseOf(loginUser.getUserId(), equipTwo, 1);
            } else if (equipType02 == 2){
                UserWildVo userWildTwo = userWildService.queryObjectByUser(loginUser.getUserId(), equipTwo);
                addFightNum = userWildTwo.getFightingNum().multiply(dogInfo.getInbornNum());
                userWildService.makeUseOf(loginUser.getUserId(), equipTwo, 1);
            }
        }
        if (equipThree > 0) {
            if (equipType03 == 1){
                UserFightEquipVo userFightEquipThree = userFightEquipService.queryObjectByUser(loginUser.getUserId(), equipThree);
                addFightNum = addFightNum.add(dogInfo.getFightingNum().multiply(userFightEquipThree.getFightingAddition().divide(new BigDecimal(100))));
                userFightEquipService.makeUseOf(loginUser.getUserId(), equipThree, 1);
            } else if (equipType03 == 2){
                UserWildVo userWildThree = userWildService.queryObjectByUser(loginUser.getUserId(), equipThree);
                addFightNum = userWildThree.getFightingNum().multiply(dogInfo.getInbornNum());
                userWildService.makeUseOf(loginUser.getUserId(), equipThree, 1);
            }
        }

        if(fight == 0){
            dogInfo.setFightingNum(dogInfo.getFightingNum().add(addFightNum));
        } else {
            BigDecimal tempNum = BigDecimal.ZERO;
            tempNum = dogInfo.getFightingNum().multiply(new BigDecimal(0.05));
            tempNum = dogInfo.getFightingNum().add(tempNum);
            tempNum = tempNum.add(addFightNum);
            dogInfo.setFightingNum(tempNum);
        }

        dogInfo.setMasterName(loginUser.getUserName());

        // 如果 没有正在参加比赛的狗狗 并且 选中的狗狗不在冷却中 则可以添加到报名列表
//        if (dogInfo.getIsCool() == 0 && dogInfo.getIsGame() == 0) enrollList.add(dogInfo);
        if (dogInfo.getIsCool() == 0) enrollList.add(dogInfo);

        Map dogMap = new HashMap();
        dogMap.put("dogId", dogId);
        dogMap.put("isCool", 1);
        dogMap.put("isGame", 1);
        if(cool == 0){
            dogMap.put("coolTime", new Date((new Date()).getTime() + 1000 * 60 * 60));
        } else {
            dogMap.put("coolTime", new Date((new Date()).getTime() + 1000 * 60 * 60));
        }
        userDogService.update(dogMap); // 更新狗狗冷却状态

        return toResponsSuccess(true);
    }

    /***
     * @Description: 取消报名
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "取消报名")
    @PostMapping("cancel")
    public Object cancelEnroll(@LoginUser UserVo loginUser, @RequestParam("dogId") Integer dogId) {

        Boolean isHave = false;
        Map dogMap = new HashMap();
        dogMap.put("isGame", 1);
        dogMap.put("dogId", dogId);
        dogMap.put("userId", loginUser.getUserId());
        UserDogVo userDogVo = userDogService.queryUserDogInfo(dogMap); // 查询取消报名狗狗信息

        if (userDogVo == null) return toResponsObject(400,"未查询到报名信息！", null);

        // 从报名池中删除狗狗（二者只会有一个存在 取消报名的狗狗）
        for (int i = 0; i < enrollList.size(); i++){
            if (enrollList.get(i).getId().equals(userDogVo.getId())) {
                enrollList.remove(i);
                isHave = true;
                break;
            }
        }
        // 从匹配池中删除狗狗（二者只会有一个存在 取消报名的狗狗）
        for (int i = 0; i < matchList.size(); i++){
            if (matchList.get(i).getId().equals(userDogVo.getId())) {
                matchList.remove(i);
                isHave = true;
                break;
            }
        }

        if (matchList.size() == 0) matchSecondsCurrent = 0;

        if (isHave == false) return toResponsObject(400,"取消报名失败！", null);

        // 返还用户体力
        userService.addBrawnNum(loginUser.getUserId(), 5);

        // 返还用户ASG
        userService.addDogCoinNum(loginUser.getUserId(), 1000);

        Map map = new HashMap();
        map.put("dogId", userDogVo.getId());
        map.put("isCool", 0);
        map.put("isGame", 0);
        userDogService.update(map); // 更新狗狗 冷却/参战状态

        return toResponsSuccess(true);
    }

}
