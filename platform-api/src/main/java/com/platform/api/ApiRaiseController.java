package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.UserDogVo;
import com.platform.entity.UserForageVo;
import com.platform.entity.UserVo;
import com.platform.entity.UserWildVo;
import com.platform.service.ApiUserDogService;
import com.platform.service.ApiUserForageService;
import com.platform.service.ApiUserWildService;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: platform
 * @description: 饲养宠物接口
 * @author: Yuan
 * @create: 2020-09-04 11:50
 **/
@Api(tags = "饲养宠物接口")
@RestController
@RequestMapping("/api")
public class ApiRaiseController extends ApiBaseAction {

    @Autowired
    private ApiUserDogService userDogService;
    @Autowired
    private ApiUserForageService userForageService;
    @Autowired
    private ApiUserWildService userWildService;

    /**
     * 饲养宠物
     */
    @IgnoreAuth
    @GetMapping(value = "raise")
    @ApiOperation(value = "饲养宠物【id：饲料/野生ID，dogId：狗狗ID，type：投喂类型（1：饲料 2：野生）】")
    public Object keepingPets(@LoginUser UserVo loginUser, @RequestParam("id") Integer id, @RequestParam("dogId") Integer dogId, @RequestParam(value = "type") Integer type) {

        try {

            Map map = new HashMap();
            map.put("dogId", dogId);
            map.put("userId", loginUser.getUserId());
            UserDogVo dogInfo = userDogService.queryUserDogInfo(map); // 查询狗狗信息
            if (dogInfo == null) return toResponsObject(400,"宠物信息不存在！",null);
            if (dogInfo.getIsUse().equals(0)) return toResponsObject(400,"该宠物未设置使用状态！",null);

            if (dogInfo.getCurrentGrowUpNum() >= dogInfo.getMaxGrowUpNum()) return toResponsObject(400,"该宠物成长次数已达到最大，不可再次投喂！",null);

            Map mapDog = new HashMap();
            mapDog.put("dogId", dogId);
            mapDog.put("userId", loginUser.getUserId());

            if(type.equals(1)) { // 使用饲料喂养
                UserForageVo forageVo = userForageService.queryObjectByUser(loginUser.getUserId(), id);
                if (forageVo == null) return toResponsObject(400, "该饲料已使用完！", null);

                Integer fightingNum = forageVo.getFightingNum();
                BigDecimal num = new BigDecimal(fightingNum);
                BigDecimal result = num.add(dogInfo.getFightingNum());
                if (forageVo.getIsIgnoreTalent().equals(0)){
                    result = dogInfo.getInbornNum().multiply(num);
                    result = result.add(dogInfo.getFightingNum());
                }
                mapDog.put("currentGrowUpNum", dogInfo.getCurrentGrowUpNum() + 1);
                mapDog.put("fightingNum", result);
                userForageService.deleteByUser(loginUser.getUserId(), id);
            } else if (type.equals(2)) { // 使用野生喂养
                UserWildVo wildVo = userWildService.queryObjectByUser(loginUser.getUserId(), id);
                if (wildVo == null || wildVo.getWildNum() <= 0) return toResponsObject(400, "该野生已使用完！", null);

                BigDecimal num = wildVo.getFightingNum();
                BigDecimal result = num.add(dogInfo.getFightingNum());
                if (wildVo.getIsIgnoreTalent().equals(0)){
                    result = dogInfo.getInbornNum().multiply(num);
                    result = result.add(dogInfo.getFightingNum());
                }
                mapDog.put("currentGrowUpNum", dogInfo.getCurrentGrowUpNum() + 1);
                mapDog.put("fightingNum", result);
                userWildService.deleteByUser(loginUser.getUserId(), id);
            } else {
                return toResponsObject(400,"参数错误!",null);
            }

            userDogService.update(mapDog);
            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsSuccess(false);
    }

    @IgnoreAuth
    @PostMapping(value = "gobble")
    @ApiOperation(value = "吞噬宠物")
    public Object gobbleUp(@LoginUser UserVo loginUser, @RequestParam("dogId") Integer dogId){

        try {
            Map useMap = new HashMap();
            useMap.put("isUse", 1);
            useMap.put("userId", loginUser.getUserId());
            UserDogVo useDog = userDogService.queryUserDogInfo(useMap); // 查询正在等待投喂的狗狗信息
            if (null == useDog) return toResponsObject(400, "未查询到正在等待投喂的狗狗信息", "");

            if (useDog.getCurrentGrowUpNum() >= useDog.getMaxGrowUpNum()) return toResponsObject(400,"该宠物成长次数已达到最大，不可再次投喂！",null);

            UserDogVo feedDog = userDogService.queryObjectByUser(loginUser.getUserId(), dogId); // 将要被投喂的狗狗信息
            if (null == feedDog) return toResponsObject(400, "未查询到将要被投喂的狗狗信息", "");

            Map updateMap = new HashMap();

            // 受天赋影响
            updateMap.put("fightingNum", useDog.getFightingNum().add(feedDog.getFightingNum().multiply(useDog.getInbornNum())));
            updateMap.put("dogId", useDog.getId());
            updateMap.put("currentGrowUpNum", useDog.getCurrentGrowUpNum() + 1);
            updateMap.put("userId", loginUser.getUserId());
            userDogService.update(updateMap);

            userDogService.deleteByUser(loginUser.getUserId(), dogId);

            return toResponsSuccess(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsFail("信息错误");
    }
}
