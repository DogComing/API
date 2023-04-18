package com.platform.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.LogRakeBackVo;
import com.platform.service.ApiLogRakeBackService;
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
    private ApiLogRakeBackService logRakeBackService;
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

        Map returnMap = new HashMap();
        Map dataMap = new HashMap();
        try {
            if (map.get("nft_type").toString().equals("1.0")){
                transformNotify(map.get("address").toString(), map.get("nft_id").toString());
            } else if (map.get("nft_type").toString().equals("2.0")){

            } else {
                returnMap.put("code", 400);
                returnMap.put("message", "fail");
                dataMap.put("info", "fail");
                returnMap.put("data", dataMap);
                return returnMap;
            }
            returnMap.put("code", 200);
            returnMap.put("message", "success");
            dataMap.put("info", "success");
            returnMap.put("data", dataMap);
            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        returnMap.put("code", 400);
        returnMap.put("message", "fail");
        dataMap.put("info", "fail");
        returnMap.put("data", dataMap);
        return returnMap;
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

    private static void jacksonMethod(File file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.readValue(file, Map.class));
    }
}
