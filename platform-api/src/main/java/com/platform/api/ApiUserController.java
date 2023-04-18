package com.platform.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.util.CommonUtil;
import com.platform.util.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

import static com.platform.config.ConstantConfig.*;

/**
 * 用户信息接口
 *
 * @author xingGuangTeam
 * @email 249893127@qq.com
 * @date 2019-03-23 15:31
 */
@Api(tags = "用户信息接口")
@RestController
@RequestMapping("/api/user")
public class ApiUserController extends ApiBaseAction {

    @Autowired
    private ApiUserService userService;
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
    private ApiLogASGService logASGService;

    /***
     * @Description: 获取用户信息
     * @Param: [loginUser]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("info")
    public Object getUserInfo(@LoginUser UserVo loginUser){

        try {
            UserVo userVo = userService.queryObject(loginUser.getUserId()); // 查询用户信息
            if (null == userVo) return toResponsObject(400, "用户信息错误", null);

            Map map = new HashMap();
            map.put("isUse", 1);
            map.put("userId", loginUser.getUserId());
            UserDogVo dogInfo = userDogService.queryUserDogInfo(map); // 查询狗狗信息

            Map dogMap = new HashMap();
            if (null == dogInfo) {
                dogMap.put("id", 0);
                dogMap.put("dogName", "");
                dogMap.put("dogImg", "");
            } else {
                dogMap.put("id", dogInfo.getId());
                dogMap.put("dogName", dogInfo.getDogName());
                dogMap.put("dogImg", dogInfo.getImgName());
            }

            userVo.setDogMap(dogMap);
            userVo.setGemNum(gemList(loginUser.getUserId()).size());
            userVo.setDogNum(userDogService.queryTotal(map));

            return toResponsSuccess(userVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 修改个人信息
     * @Param: [loginUser, userVo]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @PostMapping("update")
    @ApiOperation(value = "修改个人信息")
    public Object updateUserInfo(@LoginUser UserVo loginUser, @RequestBody UserVo userVo) {

        try {

            Map map = new HashMap();
            map.put("userName", userVo.getUserName());
            map.put("language", userVo.getLanguage());
            map.put("isEffect", userVo.getIsEffect());
            map.put("isMusic", userVo.getIsMusic());
            map.put("userId", loginUser.getUserId());

            if (userVo.getUserName() != null){
                if (loginUser.getIsFreeNameEdit() == 1){
                    map.put("isFreeNameEdit", 0);
                    userService.update(map);
                    return toResponsSuccess(true);
                } else {
                    Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
                    Map<String,Object> form = new HashMap<>();
                    form.put("address", loginUser.getAddress());
                    form.put("nonce", nonce);
                    form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                    form.put("coin_code", "usdt");
                    form.put("num", 1);
                    form.put("order_sn", CommonUtil.generateOrderNumber());
                    String sign = MD5Util.encodeSign(form, key);
                    form.put("sign", sign);
                    HttpResponse res = HttpRequest.post(decCoin).form(form).header("Authorization", authorization).timeout(10000).execute();
                    String body = res.body();
                    System.out.println("修改昵称支付U响应数据：" + body);
                    JSONObject result = JSON.parseObject(body);
                    if (result.getInteger("code") == 200 && result.getString("message").equals("success")){
                        map.put("isFreeNameEdit", 0);
                        userService.update(map);
                        return toResponsSuccess(true);
                    }
                }
            } else {
                userService.update(map);
                return toResponsSuccess(true);
            }
            return toResponsObject(400, "操作异常请重试", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("修改失败");
    }

    /**
     * 从基地获取代币总数
     * @param loginUser
     * @return
     */
    @GetMapping("asg/total")
    @ApiOperation(value = "从基地获取代币总数")
    public Object getASGTotal(@LoginUser UserVo loginUser){

        try {
            Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
            Map<String,Object> form = new HashMap<>();
            form.put("address", loginUser.getAddress());
            form.put("nonce", nonce);
            form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            String sign = MD5Util.encodeSign(form, key);
            form.put("sign", sign);
            HttpResponse res = HttpRequest.post(coinTotal).form(form).header("Authorization", authorization).timeout(10000).execute();
            String body = res.body();
            System.out.println(body);
            JSONObject result = JSON.parseObject(body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){
                JSONObject data = result.getJSONObject("data");
                Map resultMap = new HashMap();
                resultMap.put("asg", data.getString("asg"));
                resultMap.put("usdt", data.getString("usdt"));
                return toResponsSuccess(resultMap);
            }

            return toResponsObject(400,"查询代币数量失败", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("查询代币数量失败");
    }

    /***
     * @Description: 充值
     * @Param: [loginUser, userVo]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @PostMapping("recharge")
    @ApiOperation(value = "充值")
    public Object recharge(@LoginUser UserVo loginUser, @RequestParam(value = "money", defaultValue = "0") Integer money){

        try {

            Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
            Map<String,Object> form = new HashMap<>();
            form.put("address", loginUser.getAddress());
            form.put("nonce", nonce);
            form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            form.put("num", money);
            form.put("order_sn", CommonUtil.generateOrderNumber());
            String sign = MD5Util.encodeSign(form, key);
            form.put("sign", sign);
            HttpResponse res = HttpRequest.post(decCoin).form(form).header("Authorization", authorization).timeout(10000).execute();
            String body = res.body();
            System.out.println(body);
            JSONObject result = JSON.parseObject(body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                userService.addDogCoinNum(loginUser.getUserId(), money);

                LogASGVo logASGVo = new LogASGVo();
                logASGVo.setUserId(loginUser.getUserId());
                logASGVo.setNum(new BigDecimal(money));
                logASGVo.setLogType(1);
                logASGVo.setLogTypeTxt("充值");
                logASGVo.setAsgType(2);
                logASGVo.setAgsTypeTxt("获得");
                logASGVo.setCreateTime(new Date());
                logASGVo.setRemarks("从基地成功提取" + money + "ASG 到游戏");
                logASGVo.setAddress(loginUser.getAddress());
                logASGService.save(logASGVo);

                return toResponsSuccess(true);
            }

            // 调用基地充值失败
            return toResponsObject(400,"充值操作失败", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsObject(400,"充值操作失败", null);
    }

    /***
     * @Description: 提现
     * @Param: [loginUser, userVo]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @PostMapping("withdraw")
    @ApiOperation(value = "提现")
    public Object withdraw(@LoginUser UserVo loginUser, @RequestParam(value = "money", defaultValue = "0") Integer money){
        try {
            if (loginUser.getDogCoin() < money) return toResponsObject(400,"AGS不足", null);

            Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
            Map<String,Object> form = new HashMap<>();
            form.put("address", loginUser.getAddress());
            form.put("nonce", nonce);
            form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            form.put("num", money);
            form.put("order_sn", CommonUtil.generateOrderNumber());
            String sign = MD5Util.encodeSign(form, key);
            form.put("sign", sign);
            HttpResponse res = HttpRequest.post(addCoin).form(form).header("Authorization", authorization).timeout(10000).execute();
            String body = res.body();
            System.out.println(body);
            JSONObject result = JSON.parseObject(body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                userService.useDogCoinNum(loginUser.getUserId(), money);

                LogASGVo logASGVo = new LogASGVo();
                logASGVo.setUserId(loginUser.getUserId());
                logASGVo.setNum(new BigDecimal(money));
                logASGVo.setLogType(2);
                logASGVo.setLogTypeTxt("提现");
                logASGVo.setAsgType(1);
                logASGVo.setAgsTypeTxt("消耗");
                logASGVo.setCreateTime(new Date());
                logASGVo.setRemarks("从游戏成功提取" + money + "ASG 到基地");
                logASGVo.setAddress(loginUser.getAddress());
                logASGService.save(logASGVo);

                return toResponsSuccess(true);
            }

            // 调用基地提现失败
            return toResponsObject(400,"提现操作失败", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsObject(400,"提现操作失败", null);
    }

    /***
     * @Description: 补充体力配置
     * @Param: [loginUser, userVo]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @PostMapping("brawn/config")
    @ApiOperation(value = "补充体力配置")
    public Object replenishBrawn(@LoginUser UserVo loginUser){
        try {

            Map map = new HashMap();
            map.put("usdt", 1);
            map.put("brawn", 30);
            map.put("buyBrawnNum", loginUser.getBuyBrawnNum());
            return toResponsSuccess(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsObject(400,"补充体力失败", null);
    }

    public List<Map> gemList(Long userId) {

        //查询列表数据
        List<Map> tempList = new ArrayList<>();

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
            map.put("id", list.get(i).getForageId());
            map.put("name", list.get(i).getForageName());
            map.put("desc", list.get(i).getForageDesc());
            map.put("img", list.get(i).getImgName());
            map.put("num", list.get(i).getForageNum());
            map.put("type", 1);
            tempList.add(map);
        }

        // 珍宝道具
        List<UserPropVo> listp = userPropService.queryListByMap(params);
        for (int i = 0; i < listp.size(); i++){
            Map map = new HashMap();
            map.put("id", listp.get(i).getPropId());
            map.put("name", listp.get(i).getPropName());
            map.put("desc", listp.get(i).getPropDesc());
            map.put("img", listp.get(i).getImgName());
            map.put("num", listp.get(i).getPropNum());
            map.put("type", 2);
            tempList.add(map);
        }

        // 珍宝捕捉装备
        List<UserCatchEquipVo> listc = userCatchEquipService.queryListByMap(params);
        for (int i = 0; i < listc.size(); i++){
            Map map = new HashMap();
            map.put("id", listc.get(i).getEquipId());
            map.put("name", listc.get(i).getEquipName());
            map.put("desc", listc.get(i).getEquipDesc());
            map.put("img", listc.get(i).getImgName());
            map.put("num", listc.get(i).getEquipNum());
            map.put("type", 3);
            tempList.add(map);
        }

        // 珍宝对战装备
        List<UserFightEquipVo> listf = userFightEquipService.queryListByMap(params);
        for (int i = 0; i < listf.size(); i++){
            Map map = new HashMap();
            map.put("id", listf.get(i).getFightId());
            map.put("name", listf.get(i).getFightName());
            map.put("desc", listf.get(i).getFightDesc());
            map.put("img", listf.get(i).getImgName());
            map.put("num", listf.get(i).getFightNum());
            map.put("type", 4);
            tempList.add(map);
        }

        return tempList;
    }
}
