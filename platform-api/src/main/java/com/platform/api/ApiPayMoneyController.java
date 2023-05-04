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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.platform.config.ConstantConfig.*;


@Api(tags = "调取基地支付接口")
@RestController
@RequestMapping("/api/pay")
public class ApiPayMoneyController extends ApiBaseAction {

    @Autowired
    private ApiMapService mapService;
    @Autowired
    private ApiUserService userService;

    @Autowired
    private ApiConfigCatchEquipService catchEquipService;
    @Autowired
    private ApiUserCatchEquipService userCatchEquipService;

    @Autowired
    private ApiLogPayService logPayService;

    /***
     * @Description: 购买捕捉装备
     * @Param: [loginUser, id]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "购买捕捉装备 id: 捕捉装备ID")
    @PostMapping("catch/equip")
    public Object buyCatchEquip(@LoginUser UserVo loginUser, @RequestParam(value = "id", defaultValue = "1") Integer id) {

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String startTime = dateFormat.format(now);
            Date sd1 = dateFormat.parse(startTime); // 当前时间

            // 当前时间 大于 可以购买时间
            if (loginUser.getNextBuyEquipTime() != null && sd1.compareTo(loginUser.getNextBuyEquipTime()) < 0) return toResponsObject(400, "与上一次购买时间需间隔48小时", null);

            MapVo mapVo = mapService.queryObject(loginUser.getOpenMapNum());

            if(loginUser.getOpenMapNum() + 1 != mapVo.getId() + 1) return toResponsObject(400, "请先解锁上一个地图", null);

            ConfigCatchEquipVo catchEquipVo = catchEquipService.queryObject(id);
            if (catchEquipVo == null) return toResponsObject(400, "查询道具信息错误", null);

            Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
            Map<String,Object> form = new HashMap<>();
            form.put("address", loginUser.getAddress());
            form.put("nonce", nonce);
            form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            form.put("coin_code", "usdt");
            form.put("num", catchEquipVo.getUnum());
            form.put("order_sn", CommonUtil.generateOrderNumber());
            String sign = MD5Util.encodeSign(form, key);
            form.put("sign", sign);
            HttpResponse res = HttpRequest.post(decCoin).form(form).header("Authorization", authorization).timeout(10000).execute();
            String body = res.body();
            System.out.println("购买装备基地响应数据：" + body);
            JSONObject result = JSON.parseObject(body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                UserCatchEquipVo userCatchEquipVo = new UserCatchEquipVo();

                userCatchEquipVo.setEquipId(catchEquipVo.getId());
                userCatchEquipVo.setUserId(loginUser.getUserId());
                userCatchEquipVo.setEquipName(catchEquipVo.getEquipName());
                userCatchEquipVo.setEquipDesc(catchEquipVo.getEquipDesc());
                userCatchEquipVo.setImgName(catchEquipVo.getImgName());
                userCatchEquipVo.setCatchType(catchEquipVo.getCatchType());
                userCatchEquipVo.setGrade(catchEquipVo.getGrade());
                userCatchEquipVo.setDeedType(catchEquipVo.getDeedType());
                userCatchEquipVo.setExtraOne(catchEquipVo.getExtraOne());
                userCatchEquipVo.setExtraTwo(catchEquipVo.getExtraTwo());
                userCatchEquipVo.setDurabilityMax(catchEquipVo.getDurability());
                userCatchEquipVo.setDurabilityResidue(catchEquipVo.getDurability());
                userCatchEquipVo.setIsGem(catchEquipVo.getIsGem());
                userCatchEquipVo.setEquipNum(1);
                userCatchEquipVo.setCreateTime(new Date());
                userCatchEquipService.save(userCatchEquipVo);

                Map userMap = new HashMap();
                userMap.put("nextBuyEquipTime", new Date((new Date()).getTime() + 1000 * 60 * 60 * 48));
                userMap.put("userId", loginUser.getUserId());
                userService.update(userMap);

                LogPayVo logPayVo = new LogPayVo();
                logPayVo.setUserId(loginUser.getUserId());
                logPayVo.setMoney(new BigDecimal(catchEquipVo.getUnum()));
                logPayVo.setType(3);
                logPayVo.setTypeTxt("购买捕捉装备");
                logPayVo.setCurrencyType(2);
                logPayVo.setCurrencyTxt("USDT");
                logPayVo.setCreateTime(new Date());
                logPayVo.setRemarks("成功支付" + catchEquipVo.getUnum() + "USDT，获得一件 " + catchEquipVo.getEquipName() + " 捕捉装备，装备等级 " + catchEquipVo.getGrade() + "级");
                logPayVo.setAddress(loginUser.getAddress());
                logPayService.save(logPayVo);

                return toResponsSuccess(true);
            }

            // 调用基地扣钱失败
            return toResponsObject(400,"支付失败,请检查钱包是否有钱", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 续存对战装备
     * @Param: [loginUser, type]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "续存对战装备 type 1:对战装备 2：野生对战装备")
    @PostMapping("fight/equip")
    public Object buyFightEquip(@LoginUser UserVo loginUser, @RequestParam(value = "type", defaultValue = "0") Integer type, @RequestParam(value = "id", defaultValue = "1") Integer id){

        try {
            MapVo mapVo = mapService.queryObject(loginUser.getOpenMapNum());

            if(loginUser.getOpenMapNum() + 1 != mapVo.getId() + 1) return toResponsObject(400, "请先解锁上一个地图", null);

            ConfigCatchEquipVo catchEquipVo = catchEquipService.queryObject(id);
            if (catchEquipVo == null) return toResponsObject(400, "查询道具信息错误", null);

            Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
            Map<String,Object> form = new HashMap<>();
            form.put("address", loginUser.getAddress());
            form.put("nonce", nonce);
            form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            form.put("coin_code", "usdt");
            form.put("num", catchEquipVo.getUnum());
            form.put("order_sn", CommonUtil.generateOrderNumber());
            String sign = MD5Util.encodeSign(form, key);
            form.put("sign", sign);
            HttpResponse res = HttpRequest.post(decCoin).form(form).header("Authorization", authorization).timeout(10000).execute();
            String body = res.body();
            System.out.println(body);
            JSONObject result = JSON.parseObject(body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                UserCatchEquipVo userCatchEquipVo = new UserCatchEquipVo();

                userCatchEquipVo.setEquipId(catchEquipVo.getId());
                userCatchEquipVo.setUserId(loginUser.getUserId());
                userCatchEquipVo.setEquipName(catchEquipVo.getEquipName());
                userCatchEquipVo.setEquipDesc(catchEquipVo.getEquipDesc());
                userCatchEquipVo.setImgName(catchEquipVo.getImgName());
                userCatchEquipVo.setCatchType(catchEquipVo.getCatchType());
                userCatchEquipVo.setGrade(catchEquipVo.getGrade());
                userCatchEquipVo.setDeedType(catchEquipVo.getDeedType());
                userCatchEquipVo.setExtraOne(catchEquipVo.getExtraOne());
                userCatchEquipVo.setExtraTwo(catchEquipVo.getExtraTwo());
                userCatchEquipVo.setDurabilityMax(catchEquipVo.getDurability());
                userCatchEquipVo.setDurabilityResidue(catchEquipVo.getDurability());
                userCatchEquipVo.setIsGem(catchEquipVo.getIsGem());
                userCatchEquipVo.setEquipNum(1);
                userCatchEquipVo.setCreateTime(new Date());
                userCatchEquipService.save(userCatchEquipVo);

                return toResponsSuccess(true);
            }

            // 调用基地扣钱失败
            return toResponsObject(400,"支付失败,请检查钱包是否有钱", null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 购买30天签到
     * @Param: [loginUser, type]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "购买30天签到")
    @PostMapping("sign/in")
    public Object buySignIn(@LoginUser UserVo loginUser){

        try {

            if(loginUser.getIsSignIn() == 1) return toResponsObject(400, "您已支付成功，请刷新界面", null);

            Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
            Map<String,Object> form = new HashMap<>();
            form.put("address", loginUser.getAddress());
            form.put("nonce", nonce);
            form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            form.put("coin_code", "usdt");
            form.put("num", 10);
            form.put("order_sn", CommonUtil.generateOrderNumber());
            String sign = MD5Util.encodeSign(form, key);
            form.put("sign", sign);
            HttpResponse res = HttpRequest.post(decCoin).form(form).header("Authorization", authorization).timeout(10000).execute();
            String body = res.body();
            System.out.println("购买30天签到基地响应数据：" + body);
            JSONObject result = JSON.parseObject(body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                Map userMap = new HashMap();
                userMap.put("isSignIn", 1);
                userMap.put("signInDayNum", 0);
                userMap.put("isTodayCheck", 2);
                userMap.put("buyDayNum", 1);
                userMap.put("userId", loginUser.getUserId());
                userService.updateSignIn(userMap);

                LogPayVo logPayVo = new LogPayVo();
                logPayVo.setUserId(loginUser.getUserId());
                logPayVo.setMoney(new BigDecimal(10));
                logPayVo.setType(1);
                logPayVo.setTypeTxt("购买30天签到");
                logPayVo.setCurrencyType(2);
                logPayVo.setCurrencyTxt("USDT");
                logPayVo.setCreateTime(new Date());
                logPayVo.setRemarks("成功支付" + 10 + "USDT，获得" + 30 + "天签到礼包");
                logPayVo.setAddress(loginUser.getAddress());
                logPayService.save(logPayVo);

                return toResponsSuccess(true);
            }

            // 调用基地扣钱失败
            return toResponsObject(400,"支付失败,请检查钱包是否有钱", null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 补充体力
     * @Param: [loginUser, userVo]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @PostMapping("replenish")
    @ApiOperation(value = "补充体力")
    public Object replenishBrawn(@LoginUser UserVo loginUser){

        try {

            if (loginUser.getBuyBrawnNum() <= 0) return toResponsObject(400,"今天购买精力次数已达到上限", null);

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
            System.out.println("购买30点体力基地响应数据：" + body);
            JSONObject result = JSON.parseObject(body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                userService.addBrawnNum(loginUser.getUserId(), 30);
                Map userMap = new HashMap();
                userMap.put("buyBrawnNum", loginUser.getBuyBrawnNum() - 1);
                userMap.put("userId", loginUser.getUserId());
                userService.update(userMap);

                LogPayVo logPayVo = new LogPayVo();
                logPayVo.setUserId(loginUser.getUserId());
                logPayVo.setMoney(new BigDecimal(1));
                logPayVo.setType(2);
                logPayVo.setTypeTxt("购买精力");
                logPayVo.setCurrencyType(2);
                logPayVo.setCurrencyTxt("USDT");
                logPayVo.setCreateTime(new Date());
                logPayVo.setRemarks("成功支付" + 1 + "USDT，补充" + 30 + "点精力");
                logPayVo.setAddress(loginUser.getAddress());
                logPayService.save(logPayVo);
                return toResponsSuccess(true);
            }

            // 调用基地扣钱失败
            return toResponsObject(400,"补充体力失败,请检查钱包是否有钱", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsObject(400,"补充体力失败", null);
    }

}
