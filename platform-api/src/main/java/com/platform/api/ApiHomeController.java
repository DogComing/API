package com.platform.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.LogRakeBackVo;
import com.platform.entity.NftVo;
import com.platform.entity.UserVo;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.util.FileUtil;
import com.platform.util.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.platform.config.ConstantConfig.*;
import static com.platform.util.CommonUtil.getToday;

/**
 * @program: platform
 * @description: 基地回掉相关相关控制层
 * @author: Yuan
 * @create: 2020-09-04 11:50
 **/
@Api(tags = "基地相关")
@RestController
@RequestMapping("/api/home")
public class ApiHomeController extends ApiBaseAction {

    @Autowired
    private ApiUserFightEquipService userFightEquipService;
    @Autowired
    private ApiUserCatchEquipService userCatchEquipService;
    @Autowired
    private ApiUserForageService userForageService;
    @Autowired
    private ApiUserWildService userWildService;
    @Autowired
    private ApiUserPropService userPropService;
    @Autowired
    private ApiUserDogService userDogService;
    @Autowired
    private ApiLogRakeBackService logRakeBackService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiNftService nftService;

    /***
     * @Description: 获取返佣数据 基地调用
     * @Param: [loginUser, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @IgnoreAuth
    @ApiOperation(value = "获取返佣数据")
    @GetMapping("rebate/log")
    public Object getRebateLog() {

        Map returnMap = new HashMap();
        Map dataMap = new HashMap();

        try {
            List<LogRakeBackVo> allLog = logRakeBackService.allLog(getToday(new Date()));

            returnMap.put("code", 200);
            returnMap.put("message", "success");
            dataMap.put("info", allLog);
            returnMap.put("data", dataMap);
            return toResponsSuccess(allLog);
        } catch (Exception e) {
            e.printStackTrace();
        }

        returnMap.put("code", 400);
        returnMap.put("message", "fail");
        dataMap.put("info", "fail");
        returnMap.put("data", dataMap);
        return returnMap;
    }

    /***
     * @Description: 这里是基地NFT充值回调接口
     * @Param: [request, response]
     * @return: void
     * @Author: Yuan
     * @Date: 2019/7/27
     */
    @IgnoreAuth
    @ApiOperation(value = "基地NFT充值回调接口")
    @PostMapping("recharge/notify")
    public Object rechargeNotify(@RequestParam Map map) throws Exception {

        System.out.println("====== 基地NFT充值回调接口 ======");
        System.out.println(map);
        System.out.println("====== 基地NFT充值回调接口 ======");

        try {
            if (map.get("nft_type").toString().equals("1.0")){
                transformNotify(map.get("address").toString(), map.get("nft_id").toString());
            } else if (map.get("nft_type").toString().equals("2.0")){
                Boolean isSuccess = transferNotify(map.get("address").toString(), map.get("nft_id").toString());
                if (isSuccess) return successMap();
            } else {
                return failMap();
            }
            return successMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return failMap();
    }

    /**
     * 1.0狗狗 转化成 2.0狗狗
     * @param address
     * @param nftId
     */
    public void transformNotify(String address, String nftId) {

        try {
            Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
            Map<String,Object> form = new HashMap<>();
            form.put("address", address);
            form.put("nonce", nonce);
            form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            form.put("nft_id", nftId);
            String sign = MD5Util.encodeSign(form, key);
            form.put("sign", sign);
            HttpResponse res = HttpRequest.post(infoNFT).form(form).header("Authorization", authorization).timeout(10000).execute();
            String body = res.body();
            JSONObject result = JSON.parseObject(body);
            System.out.println("请求NFT详情：" + body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){
                JSONObject data = result.getJSONObject("data");
                if (FileUtil.saveUrlAs(data.getString("url"), data.getString("id"),"GET")){
                    File file = new File(dog01JsonPath + data.getString("id") + ".json");
                    jacksonMethod(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 狗狗：1万~1亿-1
     * 装备：1亿-2亿-1
     * 珍宝：2亿-3亿-1
     * 道具：3亿-4亿-1
     * 野生：4亿-5亿-1
     * 饲料：5亿到6亿-1
     * 2.0狗狗数据从基地划转到游戏
     * @param address
     * @param nftId
     */
    private Boolean transferNotify(String address, String nftId){

        try {
            UserVo userVo = userService.queryByAddress(address);
            if (userVo == null) return false;
            NftVo nftVo = nftService.queryObjectByNft(Long.parseLong(nftId));
            if (nftVo == null) return false;
            Map nftMap = new HashMap();
            nftMap.put("isDraw", 0);
            nftMap.put("userId", userVo.getUserId());
            nftMap.put("nftId", nftVo.getNftId());
            nftService.updateByMap(nftMap);

            // 1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备 7：珍宝
            if (nftVo.getType() == 1){
                userDogService.updateByNFT(nftMap);
            } else if (nftVo.getType() == 2){
                userPropService.updateByNFT(nftMap);
            } else if (nftVo.getType() == 3){
                userForageService.updateByNFT(nftMap);
            } else if (nftVo.getType() == 4){
                userWildService.updateByNFT(nftMap);
            } else if (nftVo.getType() == 5){
                userCatchEquipService.updateByNFT(nftMap);
            } else if (nftVo.getType() == 6){
                userFightEquipService.updateByNFT(nftMap);
            } else if (nftVo.getType() == 7){
                if (nftVo.getGemType() == 1){
                    userForageService.updateByNFT(nftMap);
                } else if (nftVo.getGemType() == 2){
                    userCatchEquipService.updateByNFT(nftMap);
                } else if (nftVo.getGemType() == 3){
                    userFightEquipService.updateByNFT(nftMap);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void jacksonMethod(File file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(file, Map.class);
        System.out.println(map);
        System.out.println(map.get("attributes"));
    }

    private Map failMap(){

        Map returnMap = new HashMap();
        Map dataMap = new HashMap();

        returnMap.put("code", 400);
        returnMap.put("message", "fail");
        dataMap.put("info", "fail");
        returnMap.put("data", dataMap);
        return returnMap;
    }

    private Map successMap(){

        Map returnMap = new HashMap();
        Map dataMap = new HashMap();

        returnMap.put("code", 200);
        returnMap.put("message", "success");
        dataMap.put("info", "success");
        returnMap.put("data", dataMap);
        return returnMap;
    }
}
