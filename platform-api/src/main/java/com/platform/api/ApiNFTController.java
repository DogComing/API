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
import com.platform.util.CreateNftUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.platform.config.ConstantConfig.*;

/**
 * @program: platform
 * @description: 铸造、消耗nft接口
 * @author: Yuan
 * @create: 2020-09-04 11:50
 **/
@Api(tags = "铸造、销毁nft接口")
@RestController
@RequestMapping("/api/nft")
public class ApiNFTController extends ApiBaseAction {

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
    private ApiNftService nftService;

    /***
     * @Description: 铸造NFT
     * @Param: [loginUser, type, id]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "铸造NFT 【1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备】")
    @PostMapping("cast")
    public Object castNFT(@LoginUser UserVo loginUser, @RequestParam(value = "type", defaultValue = "1") Integer type, @RequestParam(value = "id", defaultValue = "0") Integer id) {

        try {

            NftVo nftVo = new NftVo();
            String nftUrl = "";
            Long nftId = null;

            if(type == 1){
                UserDogVo userDogVo = userDogService.queryObjectByUser(loginUser.getUserId(), id);
                if (userDogVo == null) return toResponsObject(400,"宠物查询失败，请重试！",null);
                if (userDogVo.getIsNft() == 1) return toResponsObject(400,"该宠物已是NFT！",null);

                nftVo = returnNFTVo(loginUser.getUserId(), type, userDogVo.getImgName());
                Map nftMap = CreateNftUtil.createDogNftJson(userDogVo, nftVo);
//                nftUrl = CreateNftUtil.createDogNftJson(userDogVo, nftVo);
                nftUrl = nftMap.get("jsonUrl").toString();
                nftId = nftVo.getNftId();
                nftVo.setJsonUrl(nftUrl);
                nftVo.setAttributes(nftMap.get("attributes").toString());

            } else if(type == 2){
                UserPropVo userPropVo = userPropService.queryObjectByUser(loginUser.getUserId(), id);
                if (userPropVo == null) return toResponsObject(400,"道具查询失败，请重试！",null);
                if (userPropVo.getIsNft() == 1) return toResponsObject(400,"该道具已是NFT！",null);

            } else if(type == 3){
                UserForageVo userForageVo = userForageService.queryObjectByUser(loginUser.getUserId(), id);
                if (userForageVo == null) return toResponsObject(400,"饲料查询失败，请重试！",null);
                if (userForageVo.getIsNft() == 1) return toResponsObject(400,"该饲料已是NFT！",null);

            } else if(type == 4){
                UserWildVo userWildVo = userWildService.queryObjectByUser(loginUser.getUserId(), id);
                if (userWildVo == null) return toResponsObject(400,"装备查询失败，请重试！",null);
                if (userWildVo.getIsNft() == 1) return toResponsObject(400,"该装备已是NFT！",null);

            } else if(type == 5){
                UserCatchEquipVo userCatchEquipVo = userCatchEquipService.queryObjectByUser(loginUser.getUserId(), id);
                if (userCatchEquipVo == null) return toResponsObject(400,"捕捉装备查询失败，请重试！",null);
                if (userCatchEquipVo.getIsNft() == 1) return toResponsObject(400,"该捕捉装备已是NFT！",null);

            } else if(type == 6){
                UserFightEquipVo userFightEquipVo = userFightEquipService.queryObjectByUser(loginUser.getUserId(), id);
                if (userFightEquipVo == null) return toResponsObject(400,"装备查询失败，请重试！",null);
                if (userFightEquipVo.getIsNft() == 1) return toResponsObject(400,"该装备已是NFT！",null);

            }

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
            System.out.println("支付USDT结果：" + body);
            JSONObject result = JSON.parseObject(body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                nonce = (int)((Math.random() * 9 + 1) * 1000);
                form = new HashMap<>();
                form.put("address", loginUser.getAddress());
                form.put("nonce", nonce);
                form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                form.put("nft_id", nftVo.getNftId());
                form.put("url", nftUrl);
                sign = MD5Util.encodeSign(form, key);
                form.put("sign", sign);
                System.out.println("请求参数：" + form);
                res = HttpRequest.post(createNFT).form(form).header("Authorization", authorization).timeout(10000).execute();
                body = res.body();
                System.out.println("创建NFT结果：" + body);
                result = JSON.parseObject(body);
                if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                    Map map = new HashMap();
                    map.put("isNft", 1);
                    map.put("id", id);
                    map.put("nftId", nftId);
                    map.put("updateTime", new Date());
                    map.put("userId", loginUser.getUserId());

                    if (type == 1){
                        userDogService.updateNFT(map);
                    } else if(type == 2){
                        userPropService.updateNFT(map);
                    } else if(type == 3){
                        userForageService.updateNFT(map);
                    } else if(type == 4){
                        userWildService.updateNFT(map);
                    } else if(type == 5){
                        userCatchEquipService.updateNFT(map);
                    } else if(type == 6){
                        userFightEquipService.updateNFT(map);
                    }
                    nftService.save(nftVo);
                    return toResponsSuccess(true);
                }
            }

            // 调用基地扣钱失败
            return toResponsObject(400,"铸造NFT失败,请检查钱包余额是否充足", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 导出NFT
     * @Param: [loginUser, type, id]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "导出NFT【1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备】")
    @PostMapping("out")
    public Object outNft(@LoginUser UserVo loginUser, @RequestParam(value = "type", defaultValue = "1") Integer type,@RequestParam(value = "id", defaultValue = "0") Integer id){

        Long nftId = null;
        if(type == 1){
            UserDogVo userDogVo = userDogService.queryObjectByUser(loginUser.getUserId(), id);
            if (userDogVo == null) return toResponsObject(400,"宠物查询失败，请重试！",null);
            if (userDogVo.getIsNft() == 0) return toResponsObject(400,"该宠物不是NFT！",null);
            if (userDogVo.getIsDraw() == 1) return toResponsObject(400,"该NFT已被提取！",null);
            if (userDogVo.getIsFreeze() == 1) return toResponsObject(400,"该NFT已被冻结！",null);
            nftId = userDogVo.getNftId();
        } else if(type == 2){
            UserPropVo userPropVo = userPropService.queryObjectByUser(loginUser.getUserId(), id);
            if (userPropVo == null) return toResponsObject(400,"道具查询失败，请重试！",null);
            if (userPropVo.getIsNft() == 0) return toResponsObject(400,"该道具不是NFT！",null);
            if (userPropVo.getIsDraw() == 1) return toResponsObject(400,"该NFT已被提取！",null);
            if (userPropVo.getIsFreeze() == 1) return toResponsObject(400,"该NFT已被冻结！",null);
            nftId = userPropVo.getNftId();
        } else if(type == 3){
            UserForageVo userForageVo = userForageService.queryObjectByUser(loginUser.getUserId(), id);
            if (userForageVo == null) return toResponsObject(400,"饲料查询失败，请重试！",null);
            if (userForageVo.getIsNft() == 0) return toResponsObject(400,"该饲料不是NFT！",null);
            if (userForageVo.getIsDraw() == 1) return toResponsObject(400,"该NFT已被提取！",null);
            if (userForageVo.getIsFreeze() == 1) return toResponsObject(400,"该NFT已被冻结！",null);
            nftId = userForageVo.getNftId();
        } else if(type == 4){
            UserWildVo userWildVo = userWildService.queryObjectByUser(loginUser.getUserId(), id);
            if (userWildVo == null) return toResponsObject(400,"装备查询失败，请重试！",null);
            if (userWildVo.getIsNft() == 0) return toResponsObject(400,"该装备不是NFT！",null);
            if (userWildVo.getIsDraw() == 1) return toResponsObject(400,"该NFT已被提取！",null);
            if (userWildVo.getIsFreeze() == 1) return toResponsObject(400,"该NFT已被冻结！",null);
            nftId = userWildVo.getNftId();
        } else if(type == 5){
            UserCatchEquipVo userCatchEquipVo = userCatchEquipService.queryObjectByUser(loginUser.getUserId(), id);
            if (userCatchEquipVo == null) return toResponsObject(400,"捕捉装备查询失败，请重试！",null);
            if (userCatchEquipVo.getIsNft() == 0) return toResponsObject(400,"该捕捉装备不是NFT！",null);
            if (userCatchEquipVo.getIsDraw() == 1) return toResponsObject(400,"该NFT已被提取！",null);
            if (userCatchEquipVo.getIsFreeze() == 1) return toResponsObject(400,"该NFT已被冻结！",null);
            nftId = userCatchEquipVo.getNftId();
        } else if(type == 6){
            UserFightEquipVo userFightEquipVo = userFightEquipService.queryObjectByUser(loginUser.getUserId(), id);
            if (userFightEquipVo == null) return toResponsObject(400,"装备查询失败，请重试！",null);
            if (userFightEquipVo.getIsNft() == 0) return toResponsObject(400,"该装备不是NFT！",null);
            if (userFightEquipVo.getIsDraw() == 1) return toResponsObject(400,"该NFT已被提取！",null);
            if (userFightEquipVo.getIsFreeze() == 1) return toResponsObject(400,"该NFT已被冻结！",null);
            nftId = userFightEquipVo.getNftId();
        }

        Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
        Map<String,Object> form = new HashMap<>();
        form.put("address", loginUser.getAddress());
        form.put("nonce", nonce);
        form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        form.put("nft_id", nftId);
        String sign = MD5Util.encodeSign(form, key);
        form.put("sign", sign);
        HttpResponse res = HttpRequest.post(outNFT).form(form).header("Authorization", authorization).timeout(10000).execute();
        String body = res.body();
        System.out.println("冻结NFT结果：" + body);
        JSONObject result = JSON.parseObject(body);
        if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

            Map map = new HashMap();
            map.put("id", id);
            map.put("isDraw", 1);
            map.put("updateTime", new Date());
            map.put("userId", loginUser.getUserId());

            if (type == 1){
                userDogService.updateNFT(map);
            } else if(type == 2){
                userPropService.updateNFT(map);
            } else if(type == 3){
                userForageService.updateNFT(map);
            } else if(type == 4){
                userWildService.updateNFT(map);
            } else if(type == 5){
                userCatchEquipService.updateNFT(map);
            } else if(type == 6){
                userFightEquipService.updateNFT(map);
            }

            return toResponsSuccess(true);
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: 销毁NFT
     * @Param: [loginUser, order_status, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "销毁NFT")
    @PostMapping("destroy")
    public Object destroyNFT(@LoginUser UserVo loginUser, @RequestParam(value = "type", defaultValue = "1") Integer type,@RequestParam(value = "id", defaultValue = "0") Integer id) {

        try {

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /***
     * @Description: NFT列表
     * @Param: [loginUser, order_status, page, size]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @ApiOperation(value = "NFT列表")
    @GetMapping("logList")
    public Object NFTLogList(@LoginUser UserVo loginUser, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {

        try {

            Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
            Map<String,Object> form = new HashMap<>();
            form.put("address", loginUser.getAddress());
            form.put("nonce", nonce);
            form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            form.put("p", page);
            form.put("pagesize", size);
            String sign = MD5Util.encodeSign(form, key);
            form.put("sign", sign);
            HttpResponse res = HttpRequest.post(listNFT).form(form).header("Authorization", authorization).timeout(10000).execute();
            String body = res.body();
            System.out.println("nft列表：" + body);
            JSONObject result = JSON.parseObject(body);
            if (result.getInteger("code") == 200 && result.getString("message").equals("success")){

                return toResponsSuccess(true);
            }

            // 调用基地扣钱失败
            return toResponsObject(400,"补充体力失败,请检查钱包是否有钱", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    public NftVo returnNFTVo(Long userId, Integer type, String imgName){

        Integer num = nftService.queryTotal(type);
        Long numL = 10000L + Long.parseLong(num.toString());

        NftVo nftVo = new NftVo();
        nftVo.setType(type);
        nftVo.setNftId(numL);
        nftVo.setUserId(userId);
        nftVo.setDescription("NFT的描述");
        nftVo.setCreateTime(new Date());

        if(type == 1){
            nftVo.setName("DOG_NFT");
            nftVo.setImage(nftImageBaseUrl + "gou_gou/" + imgName + ".png");
        } else if(type == 2){

        } else if(type == 3){

        } else if(type == 4){

        } else if(type == 5){

        } else if(type == 6){

        }

        return nftVo;
    }
}
