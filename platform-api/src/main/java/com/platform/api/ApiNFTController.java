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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.platform.config.ConstantConfig.*;

/**
 * 铸造、消耗nft接口
 */
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
    @Autowired
    private ApiLogPayService logPayService;

    /**
     * 铸造NFT
     * @param loginUser
     * @param type 铸造NFT类型 【1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备】
     * @param id 物品id
     * @return
     */
    @ApiOperation(value = "铸造NFT 【1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备】")
    @PostMapping("cast")
    public Object castNFT(@LoginUser UserVo loginUser, @RequestParam(value = "type", defaultValue = "1") Integer type, @RequestParam(value = "id", defaultValue = "0") Integer id) {

        try {

            Map nftMap = new HashMap();
            NftVo nftVo = new NftVo();
            String nftUrl = "";
            Long nftId = null;

            if(type == 1){
                UserDogVo userDogVo = userDogService.queryObjectByUser(loginUser.getUserId(), id);
                if (userDogVo == null) return toResponsObject(400,"宠物查询失败，请重试！",null);
                if (userDogVo.getIsNft() == 1) return toResponsObject(400,"该宠物已是NFT！",null);

                nftVo = returnNFTVo(loginUser.getUserId(), type, userDogVo.getImgName(), 10000L, false);
                nftMap = CreateNftUtil.createDogNftJson(nftVo, userDogVo);

            } else if(type == 2){
                UserPropVo userPropVo = userPropService.queryObjectByUser(loginUser.getUserId(), id);
                if (userPropVo == null) return toResponsObject(400,"道具查询失败，请重试！",null);
                if (userPropVo.getIsNft() == 1) return toResponsObject(400,"该道具已是NFT！",null);

                nftVo = returnNFTVo(loginUser.getUserId(), type, userPropVo.getImgName(), 300000000L, false);
                nftMap = CreateNftUtil.createPropNftJson(nftVo, userPropVo);

            } else if(type == 3){
                UserForageVo userForageVo = userForageService.queryObjectByUser(loginUser.getUserId(), id);
                if (userForageVo == null) return toResponsObject(400,"饲料查询失败，请重试！",null);
                if (userForageVo.getIsNft() == 1) return toResponsObject(400,"该饲料已是NFT！",null);

                if (userForageVo.getForageType() == 2){
                    nftVo = returnNFTVo(loginUser.getUserId(), type, userForageVo.getImgName(), 200000000L, true);
                    nftVo.setType(7);
                } else {
                    nftVo = returnNFTVo(loginUser.getUserId(), type, userForageVo.getImgName(), 500000000L, false);
                }

                nftMap = CreateNftUtil.createFeedNftJson(nftVo, userForageVo);

            } else if(type == 4){
                UserWildVo userWildVo = userWildService.queryObjectByUser(loginUser.getUserId(), id);
                if (userWildVo == null) return toResponsObject(400,"野生查询失败，请重试！",null);
                if (userWildVo.getIsNft() == 1) return toResponsObject(400,"该野生已是NFT！",null);

                nftVo = returnNFTVo(loginUser.getUserId(), type, userWildVo.getImgName(), 400000000L, false);
                nftMap = CreateNftUtil.createWildNftJson(nftVo, userWildVo);

            } else if(type == 5){
                UserCatchEquipVo userCatchEquipVo = userCatchEquipService.queryObjectByUser(loginUser.getUserId(), id);
                if (userCatchEquipVo == null) return toResponsObject(400,"捕捉装备查询失败，请重试！",null);
                if (userCatchEquipVo.getIsNft() == 1) return toResponsObject(400,"该捕捉装备已是NFT！",null);

                if (userCatchEquipVo.getIsGem() == 1){
                    nftVo = returnNFTVo(loginUser.getUserId(), type, userCatchEquipVo.getImgName(), 200000000L, true);
                    nftVo.setType(7);
                } else {
                    nftVo = returnNFTVo(loginUser.getUserId(), type, userCatchEquipVo.getImgName(), 100000000L, false);
                }
                nftMap = CreateNftUtil.createCatchNftJson(nftVo, userCatchEquipVo);

            } else if(type == 6){
                UserFightEquipVo userFightEquipVo = userFightEquipService.queryObjectByUser(loginUser.getUserId(), id);
                if (userFightEquipVo == null) return toResponsObject(400,"装备查询失败，请重试！",null);
                if (userFightEquipVo.getIsNft() == 1) return toResponsObject(400,"该装备已是NFT！",null);

                if (userFightEquipVo.getIsGem() == 1){
                    nftVo = returnNFTVo(loginUser.getUserId(), type, userFightEquipVo.getImgName(), 200000000L, true);
                    nftVo.setType(7);
                } else {
                    nftVo = returnNFTVo(loginUser.getUserId(), type, userFightEquipVo.getImgName(), 100000000L, false);
                }
                nftMap = CreateNftUtil.createFightNftJson(nftVo, userFightEquipVo);
            }

            nftUrl = nftMap.get("jsonUrl").toString();
            nftId = nftVo.getNftId();
            nftVo.setJsonUrl(nftUrl);
            nftVo.setAttributes(nftMap.get("attributes").toString());

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

                LogPayVo logPayVo = new LogPayVo();
                logPayVo.setUserId(loginUser.getUserId());
                logPayVo.setMoney(new BigDecimal(1));
                logPayVo.setType(4);
                logPayVo.setTypeTxt("铸造NFT");
                logPayVo.setCurrencyType(2);
                logPayVo.setCurrencyTxt("USDT");
                logPayVo.setCreateTime(new Date());
                logPayVo.setRemarks("成功支付" + 1 + "USDT，铸造" + nftVo.getName());
                logPayVo.setAddress(loginUser.getAddress());
                logPayService.save(logPayVo);

                nonce = (int)((Math.random() * 9 + 1) * 1000);
                form = new HashMap<>();
                form.put("address", loginUser.getAddress());
                form.put("nonce", nonce);
                form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                form.put("nft_id", nftVo.getNftId());
                form.put("url", nftUrl);
                sign = MD5Util.encodeSign(form, key);
                form.put("sign", sign);
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
                } else {
                    return toResponsObject(400,result.getString("msg"), null);
                }
            }

            // 调用基地扣钱失败
            return toResponsObject(400,"铸造NFT失败,请检查钱包余额是否充足", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 导出NFT
     * @param loginUser
     * @param type 导出NFT类型【1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备】
     * @param id 物品id
     * @return
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
        System.out.println("提取NFT结果：" + body);
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

    /**
     * 销毁NFT
     * @param loginUser
     * @param type
     * @param id
     * @return
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

    /**
     * NFT列表
     * @param loginUser
     * @param page
     * @param size
     * @return
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

    /**
     * 狗狗：1万~1亿-1
     * 装备：1亿-2亿-1
     * 珍宝：2亿-3亿-1
     * 道具：3亿-4亿-1
     * 野生：4亿-5亿-1
     * 饲料：5亿到6亿-1
     * @param userId
     * @param type 1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备
     * @param imgName
     * @return
     */
    public NftVo returnNFTVo(Long userId, Integer type, String imgName, Long minNum, Boolean isGem){

        Integer num = 0;

        if (isGem){
            num = nftService.queryTotal(7);
        } else {
            if (type == 5 || type == 6){
                num = nftService.queryTotal(5) + nftService.queryTotal(6);
            } else {
                num = nftService.queryTotal(type);
            }
        }

        Long numL = minNum + Long.parseLong(num.toString());

        NftVo nftVo = new NftVo();
        nftVo.setType(type);
        nftVo.setNftId(numL);
        nftVo.setUserId(userId);
        nftVo.setDescription("NFT的描述");
        nftVo.setCreateTime(new Date());
        if (type == 3){
            nftVo.setGemType(1);
        } else if (type == 5){
            nftVo.setGemType(2);
        } else if(type == 6){
            nftVo.setGemType(3);
        }

        if(type == 1){
            nftVo.setName("DOG_NFT");
            nftVo.setImage(nftImageBaseUrl + "gou_gou/" + imgName + ".png");
        } else if(type == 2){
            nftVo.setName("PROP_NFT");
            nftVo.setImage(nftImageBaseUrl + "dao_ju/" + imgName + ".png");
        } else if(type == 3){
            nftVo.setName("FEED_NFT");
            nftVo.setImage(nftImageBaseUrl + "si_liao/" + imgName + ".png");
        } else if(type == 4){
            nftVo.setName("WILD_NFT");
            nftVo.setImage(nftImageBaseUrl + "ye_sheng/" + imgName + ".png");
        } else if(type == 5){
            nftVo.setName("CATCH_NFT");
            nftVo.setImage(nftImageBaseUrl + "bu_zhuo/" + imgName + ".png");
        } else if(type == 6){
            nftVo.setName("FIGHT_NFT");
            nftVo.setImage(nftImageBaseUrl + "zhan_dou/" + imgName + ".png");
        }

        return nftVo;
    }
}
