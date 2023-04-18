package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户仓库数据接口
 *
 * @author xingGuangTeam
 * @email 249893127@qq.com
 * @date 2019-03-23 15:31
 */
@Api(tags = "用户仓库数据接口")
@RestController
@RequestMapping("/api/store")
public class ApiStorehouseController extends ApiBaseAction {

    @Autowired
    private ApiUserDogService userDogService;
    @Autowired
    private ApiUserForageService userForageService;
    @Autowired
    private ApiUserWildService userWildService;
    @Autowired
    private ApiUserCatchEquipService userCatchEquipService;
    @Autowired
    private ApiUserFightEquipService userFightEquipService;
    @Autowired
    private ApiUserPropService userPropService;
    @Autowired
    private ApiConfigDogService configDogService;
    @Autowired
    private ApiConfigCatchEquipService configCatchEquipService;
    @Autowired
    private ApiConfigEffectService configEffectService;

    /***
     * @Description: 仓库分类列表【1：宠物、2：珍宝、3：饲料、4：野生、5：装备】
     * @Param: [loginUser, type, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "仓库分类列表【1：宠物、2：珍宝、3：饲料、4：野生、5：装备】")
    @PostMapping("all")
    public Object storeList(@LoginUser UserVo loginUser, @RequestParam(value = "type", defaultValue = "1") Integer type, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {

        try {
            //查询列表数据
            List<Map> tempList = new ArrayList<>();
            if(type.equals(1)){
                List<UserDogVo> list = userDogService.allList(loginUser.getUserId());
                return toResponsSuccess(list);
            } else if (type.equals(2)){
                return toResponsSuccess(gemList(loginUser.getUserId(), tempList));
            } else if (type.equals(3)){
                List<UserForageVo> list = userForageService.allList(loginUser.getUserId());
                for (int i = 0; i < list.size(); i++){
                    Map map = new HashMap();
                    map.put("id", list.get(i).getId());
                    map.put("grade", list.get(i).getGrade());
                    map.put("name", list.get(i).getForageName());
                    map.put("desc", list.get(i).getForageDesc());
                    map.put("img", list.get(i).getImgName());
                    map.put("num", list.get(i).getForageNum());
                    map.put("isNft", list.get(i).getIsNft());
                    map.put("isIgnoreTalent", list.get(i).getIsIgnoreTalent());
                    map.put("fightingNum", list.get(i).getFightingNum());
                    map.put("nftType", 3);
                    map.put("discardType", 3);
                    tempList.add(map);
                }
            } else if (type.equals(4)){
                List<UserWildVo> list = userWildService.allList(loginUser.getUserId());
                for (int i = 0; i < list.size(); i++){
                    Map map = new HashMap();
                    map.put("id", list.get(i).getId());
                    map.put("grade", list.get(i).getGrade());
                    map.put("name", list.get(i).getWildName());
                    map.put("desc", list.get(i).getWildDesc());
                    map.put("img", list.get(i).getImgName());
                    map.put("num", list.get(i).getWildNum());
                    map.put("isNft", list.get(i).getIsNft());
                    map.put("fightingNum", list.get(i).getFightingNum());
                    map.put("nftType", 4);
                    map.put("discardType", 4);
                    tempList.add(map);
                }
            } else if (type.equals(5)){ // 捕捉装备 和 对战装备

                // 捕捉装备
                ConfigEffectVo one = null;
                ConfigEffectVo two = null;
                List<UserCatchEquipVo> listc = userCatchEquipService.allList(loginUser.getUserId());
                for (int i = 0; i < listc.size(); i++){
                    Map map = new HashMap();
                    map.put("id", listc.get(i).getId());
                    map.put("grade", listc.get(i).getGrade());
                    map.put("name", listc.get(i).getEquipName());
                    map.put("desc", listc.get(i).getEquipDesc());
                    map.put("img", listc.get(i).getImgName());
                    map.put("num", listc.get(i).getEquipNum());
                    map.put("isNft", listc.get(i).getIsNft());
                    map.put("durabilityMax", listc.get(i).getDurabilityMax());
                    map.put("durabilityResidue", listc.get(i).getDurabilityResidue());
                    map.put("nftType", 5);
                    map.put("discardType", 5);

                    if(listc.get(i).getExtraOne() > 0){
                        one = configEffectService.queryObject(listc.get(i).getExtraOne());
                        map.put("extraOneDesc", one.getEffectDesc());
                    } else {
                        map.put("extraOneDesc", "无");
                    }

                    if (listc.get(i).getExtraTwo() > 0){
                        two = configEffectService.queryObject(listc.get(i).getExtraTwo());
                        map.put("extraTwoDesc", two.getEffectDesc());
                    } else {
                        map.put("extraTwoDesc", "无");
                    }

                    tempList.add(map);
                }

                // 对战装备
                List<UserFightEquipVo> listf = userFightEquipService.allList(loginUser.getUserId());
                for (int i = 0; i < listf.size(); i++){
                    Map map = new HashMap();
                    map.put("id", listf.get(i).getId());
                    map.put("grade", listf.get(i).getGrade());
                    map.put("name", listf.get(i).getFightName());
                    map.put("desc", listf.get(i).getFightDesc());
                    map.put("img", listf.get(i).getImgName());
                    map.put("num", listf.get(i).getFightNum());
                    map.put("isNft", listf.get(i).getIsNft());
                    map.put("durabilityMax", listf.get(i).getDurabilityMax());
                    map.put("durabilityResidue", listf.get(i).getDurabilityResidue());
                    map.put("nftType", 6);
                    map.put("discardType", 6);
                    tempList.add(map);
                }

                tempList.sort((x,y)->Integer.compare(Integer.parseInt(y.get("grade").toString()), Integer.parseInt(x.get("grade").toString())));
            }

            return toResponsSuccess(tempList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 返回所有珍宝
     * @param userId
     * @param tempList
     * @return
     */
    public List<Map> gemList(Long userId, List<Map> tempList) {

        //查询列表数据
        Map params = new HashMap();
        params.put("forageType", 2);
        params.put("propType", 2);
        params.put("isGem", 1);
        params.put("userId", userId);

        // 珍宝饲料
        List<UserForageVo> list = userForageService.queryListByMap(params);
        for (int i = 0; i < list.size(); i++){
            Map map = new HashMap();
            map.put("id", list.get(i).getId());
            map.put("grade", list.get(i).getGrade());
            map.put("name", list.get(i).getForageName());
            map.put("desc", list.get(i).getForageDesc());
            map.put("img", list.get(i).getImgName());
            map.put("num", list.get(i).getForageNum());
            map.put("isNft", list.get(i).getIsNft());
            map.put("fightingNum", list.get(i).getFightingNum());
            map.put("type", 1);
            map.put("nftType", 3);
            map.put("discardType", 3);
            tempList.add(map);
        }

        // 珍宝道具
        List<UserPropVo> listp = userPropService.queryListByMap(params);
        for (int i = 0; i < listp.size(); i++){
            Map map = new HashMap();
            map.put("id", listp.get(i).getId());
            map.put("grade", listp.get(i).getGrade());
            map.put("name", listp.get(i).getPropName());
            map.put("desc", listp.get(i).getPropDesc());
            map.put("img", listp.get(i).getImgName());
            map.put("num", listp.get(i).getPropNum());
            map.put("isNft", listp.get(i).getIsNft());
            map.put("type", 2);
            map.put("nftType", 2);
            map.put("discardType", 2);
            tempList.add(map);
        }

        // 珍宝捕捉装备
        ConfigEffectVo one = null;
        ConfigEffectVo two = null;
        List<UserCatchEquipVo> listc = userCatchEquipService.queryListByMap(params);
        for (int i = 0; i < listc.size(); i++){
            Map map = new HashMap();
            map.put("id", listc.get(i).getId());
            map.put("grade", listc.get(i).getGrade());
            map.put("name", listc.get(i).getEquipName());
            map.put("desc", listc.get(i).getEquipDesc());
            map.put("img", listc.get(i).getImgName());
            map.put("num", listc.get(i).getEquipNum());
            map.put("isNft", listc.get(i).getIsNft());
            map.put("durabilityMax", listc.get(i).getDurabilityMax());
            map.put("durabilityResidue", listc.get(i).getDurabilityResidue());
            map.put("type", 3);
            map.put("nftType", 5);
            map.put("discardType", 5);
            if(listc.get(i).getExtraOne() > 0){
                one = configEffectService.queryObject(listc.get(i).getExtraOne());
                map.put("extraOneDesc", one.getEffectDesc());
            } else {
                map.put("extraOneDesc", "无");
            }

            if (listc.get(i).getExtraTwo() > 0){
                two = configEffectService.queryObject(listc.get(i).getExtraTwo());
                map.put("extraTwoDesc", two.getEffectDesc());
            } else {
                map.put("extraTwoDesc", "无");
            }
            tempList.add(map);
        }

        // 珍宝对战装备
        List<UserFightEquipVo> listf = userFightEquipService.queryListByMap(params);
        for (int i = 0; i < listf.size(); i++){
            Map map = new HashMap();
            map.put("id", listf.get(i).getId());
            map.put("grade", listf.get(i).getGrade());
            map.put("name", listf.get(i).getFightName());
            map.put("desc", listf.get(i).getFightDesc());
            map.put("img", listf.get(i).getImgName());
            map.put("num", listf.get(i).getFightNum());
            map.put("isNft", listf.get(i).getIsNft());
            map.put("durabilityMax", listf.get(i).getDurabilityMax());
            map.put("durabilityResidue", listf.get(i).getDurabilityResidue());
            map.put("type", 4);
            map.put("nftType", 6);
            map.put("discardType", 6);
            tempList.add(map);
        }

        if (tempList.size() > 0) {
//            tempList.sort((m1, m2) -> {
//                if (StringUtils.equals("desc", "desc")) {
//                    return String.valueOf(m2.get("nftType")).compareTo(String.valueOf(m1.get("nftType")));
//                } else {
//                    return String.valueOf(m1.get("nftType")).compareTo(String.valueOf(m2.get("nftType")));
//                }
//            });
            // 或者 Collections类里面的sort方法也是list.sort()与上面一样
             Collections.sort(tempList, (m1, m2)-> String.valueOf(m1.get("fightingNum")).compareTo(String.valueOf(m2.get("fightingNum")))); // lamuda排序
        }
        return tempList;
    }

    /***
     * @Description: 道具使用(随机捕捉装备道具、随机宠物道具)
     * @Param: [loginUser, type, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "道具使用")
    @PostMapping("use/prop")
    public Object useProp(@LoginUser UserVo loginUser, @RequestParam(value = "propId") Integer propId, @RequestParam(value = "dogId", defaultValue = "0") Integer dogId){

        try {
            UserPropVo userPropVo = userPropService.queryObjectByUser(loginUser.getUserId(), propId);

            if (userPropVo == null) return toResponsFail("道具信息错误");
            if (userPropVo.getPropNum() <= 0) return toResponsFail("道具已使用完");

            // 创建随机数对象
            Random r = new Random();
            // 产生一个随机数 1 ~ 5
            int number = r.nextInt(5) % (5 - 1 + 1) + 1;

            if(propId == 1){
                UserDogVo userDogVo = userDogService.queryObjectByUser(loginUser.getUserId(), dogId);
                if (userDogVo == null) return toResponsObject(1,"为查询到该狗狗信息", null);
                if (userDogVo.getMaxGrowUpNum() >= 6) return toResponsObject(1,"亲～您的狗狗最大成长次数已达到峰值，请换只狗狗再试", null);
                Map map = new HashMap();
                map.put("id", userDogVo.getId());
                map.put("maxGrowUpNum", userDogVo.getMaxGrowUpNum() + 1);
                map.put("userId", loginUser.getUserId());
                userDogService.update(map);
            } else if(propId == 6){ // 捕捉装备盲盒使用
                ConfigCatchEquipVo configCatchEquipVo = configCatchEquipService.queryObject(number);
                UserCatchEquipVo catchEquipVo = new UserCatchEquipVo();
                catchEquipVo.setEquipId(configCatchEquipVo.getId());
                catchEquipVo.setUserId(loginUser.getUserId());
                catchEquipVo.setEquipName(configCatchEquipVo.getEquipName());
                catchEquipVo.setEquipDesc(configCatchEquipVo.getEquipDesc());
                catchEquipVo.setImgName(configCatchEquipVo.getImgName());
                catchEquipVo.setCatchType(configCatchEquipVo.getCatchType());
                catchEquipVo.setGrade(configCatchEquipVo.getGrade());
                catchEquipVo.setDeedType(configCatchEquipVo.getDeedType());
                catchEquipVo.setExtraOne(configCatchEquipVo.getExtraOne());
                catchEquipVo.setExtraTwo(configCatchEquipVo.getExtraTwo());
                catchEquipVo.setDurabilityMax(configCatchEquipVo.getDurability());
                catchEquipVo.setDurabilityResidue(configCatchEquipVo.getDurability());
                catchEquipVo.setIsGem(configCatchEquipVo.getIsGem());
                catchEquipVo.setEquipNum(1);
                catchEquipVo.setCreateTime(new Date());
                userCatchEquipService.save(catchEquipVo);
            } else if (propId == 7){ // 宠物盲盒使用
                ConfigDogVo configDogVo = configDogService.queryObject(number);

                UserDogVo dogVo = new UserDogVo();
                dogVo.setDogGrade(configDogVo.getDogGrade());
                dogVo.setDogName(configDogVo.getDogName());
                dogVo.setAnimationName(configDogVo.getAnimationName());
                dogVo.setDogDesc(configDogVo.getDogDesc());
                dogVo.setImgName(configDogVo.getImgName());
                dogVo.setDogBreed(configDogVo.getDogBreed());
                dogVo.setFightingNum(configDogVo.getInitialFightingNum());
                dogVo.setMaxGrowUpNum(configDogVo.getGrowUpNum());
                dogVo.setInbornNum(configDogVo.getInbornNum());
                dogVo.setUserId(loginUser.getUserId());
                dogVo.setCreateTime(new Date());
                userDogService.save(dogVo);
            }

            // 减少道具数量
            userPropService.makeUseOf(loginUser.getUserId(), propId, 1);

            return toResponsSuccess(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 销毁装备【1：对战装备、2：野生对战装备、3：捕捉装备】
     * @Param: [loginUser, type, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "销毁装备【1：对战装备、2：野生对战装备、3：捕捉装备】")
    @PostMapping("destroy")
    public Object destroyEquip(@LoginUser UserVo loginUser, @RequestParam(value = "type", defaultValue = "0") Integer type, @RequestParam(value = "id", defaultValue = "0") Integer id){

        try {

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 获取用户战斗道具
     */
    @IgnoreAuth
    @ApiOperation(value = "获取战斗道具")
    @GetMapping(value = "all/Prop")
    public Object getAllProp(@LoginUser UserVo loginUser){

        try {
            //查询列表数据
            Map params = new HashMap();
            params.put("useType", 1);
            params.put("userId", loginUser.getUserId());

            List<UserPropVo> map = new ArrayList<>();
            UserPropVo one = null;
            UserPropVo two = null;
            UserPropVo three = null;
            UserPropVo four = null;
            List<UserPropVo> propVoList = userPropService.queryListByMap(params);
            for (int i = 0; i < propVoList.size(); i++){
                if(propVoList.get(i).getPropId() == 2){
                    if(one != null){
                        one.setPropNum(one.getPropNum() + 1);
                    } else {
                        one = propVoList.get(i);
                    }
                }

                if(propVoList.get(i).getPropId() == 3){
                    if(two != null){
                        two.setPropNum(two.getPropNum() + 1);
                    } else {
                        two = propVoList.get(i);
                    }
                }

                if(propVoList.get(i).getPropId() == 4){
                    if(three != null){
                        three.setPropNum(three.getPropNum() + 1);
                    } else {
                        three = propVoList.get(i);
                    }
                }

                if(propVoList.get(i).getPropId() == 5){
                    if(four != null){
                        four.setPropNum(four.getPropNum() + 1);
                    } else {
                        four = propVoList.get(i);
                    }
                }
            }
            if (one != null){
                map.add(one);
            }
            if (two != null){
                map.add(two);
            }
            if (three != null){
                map.add(three);
            }
            if (four != null){
                map.add(four);
            }

            return toResponsSuccess(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }
}
