package com.platform.api;

import com.platform.annotation.LoginUser;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: platform
 * @description: 地图配置表
 * @author: Yuan
 * @create: 2020-08-13 17:34
 **/
@Api(tags = "地图配置表")
@RestController
@RequestMapping("/api/map")
public class ApiMapController extends ApiBaseAction {

    @Autowired
    private ApiMapService mapService;
    @Autowired
    private ApiUserService userService;

    @Autowired
    private ApiConfigMapCatchRatioService mapCatchRatioService;
    @Autowired
    private ApiConfigMapFightRatioService mapFightRatioService;
    @Autowired
    private ApiConfigMapForageRatioService mapForageRatioService;
    @Autowired
    private ApiConfigMapGemRatioService mapGemRatioService;
    @Autowired
    private ApiConfigMapPetRatioService mapPetRatioService;
    @Autowired
    private ApiConfigMapWildRatioService mapWildRatioService;

    @Autowired
    private ApiConfigCatchEquipService configCatchEquipMapper;
    @Autowired
    private ApiUserCatchEquipService catchEquipService;

    /**
     * 地图列表
     */
    @ApiOperation(value = "地图列表")
    @GetMapping(value = "all")
    public Object mapList(@LoginUser UserVo loginUser) {

        try {
            List<MapVo> groupList = mapService.allMap();
            for (int i = 0; i < loginUser.getOpenMapNum(); i++){
                groupList.get(i).setIsOpen(true);
            }

            List<Map> listTemp = null;
            for (int i = 0; i < groupList.size(); i++){

                if (groupList.get(i).getMapGrade() == 1) continue;

                ConfigCatchEquipVo paoJie = configCatchEquipMapper.queryObjectByType(groupList.get(i).getLockLevel(), 1);
                ConfigCatchEquipVo cangShi = configCatchEquipMapper.queryObjectByType(groupList.get(i).getLockLevel(), 2);
                ConfigCatchEquipVo kenYao = configCatchEquipMapper.queryObjectByType(groupList.get(i).getLockLevel(), 3);
                ConfigCatchEquipVo faSheng = configCatchEquipMapper.queryObjectByType(groupList.get(i).getLockLevel(), 4);

                List<UserCatchEquipVo> userPaoJieList = catchEquipService.queryObjectByEquipId(loginUser.getUserId(), paoJie.getId());
                List<UserCatchEquipVo> userCangShiList = catchEquipService.queryObjectByEquipId(loginUser.getUserId(), cangShi.getId());
                List<UserCatchEquipVo> userKenYaoList = catchEquipService.queryObjectByEquipId(loginUser.getUserId(), kenYao.getId());
                List<UserCatchEquipVo> userFaShengList = catchEquipService.queryObjectByEquipId(loginUser.getUserId(), faSheng.getId());

                listTemp = new ArrayList<>();
                Map mapPaoJie = new HashMap();
                mapPaoJie.put("id", paoJie.getId());
                mapPaoJie.put("name", paoJie.getEquipName());
                mapPaoJie.put("image", paoJie.getImgName());
                mapPaoJie.put("Unum", paoJie.getUnum());
                mapPaoJie.put("type", paoJie.getCatchType());
                if(userPaoJieList.size() > 0){
                    mapPaoJie.put("isHave", true);
                } else {
                    mapPaoJie.put("isHave", false);
                }

                Map mapCangShi = new HashMap();
                mapCangShi.put("id", cangShi.getId());
                mapCangShi.put("name", cangShi.getEquipName());
                mapCangShi.put("image", cangShi.getImgName());
                mapCangShi.put("Unum", cangShi.getUnum());
                mapCangShi.put("type", cangShi.getCatchType());
                if(userCangShiList.size() > 0){
                    mapCangShi.put("isHave", true);
                } else {
                    mapCangShi.put("isHave", false);
                }

                Map mapKenYao = new HashMap();
                mapKenYao.put("id", kenYao.getId());
                mapKenYao.put("name", kenYao.getEquipName());
                mapKenYao.put("image", kenYao.getImgName());
                mapKenYao.put("Unum", kenYao.getUnum());
                mapKenYao.put("type", kenYao.getCatchType());
                if(userKenYaoList.size() > 0){
                    mapKenYao.put("isHave", true);
                } else {
                    mapKenYao.put("isHave", false);
                }

                Map mapFaSheng = new HashMap();
                mapFaSheng.put("id", faSheng.getId());
                mapFaSheng.put("name", faSheng.getEquipName());
                mapFaSheng.put("image", faSheng.getImgName());
                mapFaSheng.put("Unum", faSheng.getUnum());
                mapFaSheng.put("type", faSheng.getCatchType());
                if(userFaShengList.size() > 0){
                    mapFaSheng.put("isHave", true);
                } else {
                    mapFaSheng.put("isHave", false);
                }

                listTemp.add(mapPaoJie);
                listTemp.add(mapCangShi);
                listTemp.add(mapKenYao);
                listTemp.add(mapFaSheng);

                groupList.get(i).setCatchPropMap(listTemp);
            }

            return toResponsSuccess(groupList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 地图详情
     */
    @ApiOperation(value = " 地图详情")
    @GetMapping(value = "detail/{id}")
    public Object detail(@LoginUser UserVo loginUser, @PathVariable("id") Integer id) {

        try {
            MapVo mapVo = mapService.queryObject(id);
            ConfigMapPetRatioVo mapPetRatioVo = mapPetRatioService.queryByMapId(id);
            ConfigMapCatchRatioVo mapCatchRatioVo = mapCatchRatioService.queryByMapId(id);
            ConfigMapFightRatioVo mapFightRatioVo = mapFightRatioService.queryByMapId(id);
            ConfigMapForageRatioVo mapForageRatioVo = mapForageRatioService.queryByMapId(id);
            ConfigMapGemRatioVo mapGemRatioVo = mapGemRatioService.queryByMapId(id);
            ConfigMapWildRatioVo mapWildRatioVo = mapWildRatioService.queryByMapId(id);

            Object temp = null;
            Field[] fields = null;
            BigDecimal bigDecimal = null;
            DecimalFormat df = new DecimalFormat("0.00%");

            List<Map> listPet = new ArrayList<>();
            // 地图产出狗狗概率
            fields = ConfigMapPetRatioVo.class.getDeclaredFields();
            for (int i = 3; i < fields.length; i++){

                temp = getFieldValueByName(fields[i].getName(), mapPetRatioVo);
                bigDecimal = new BigDecimal(temp.toString());
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 1) {
                    Map map = new HashMap();
                    map.put("grade", i - 2);
//                    map.put("odds", percentFormat.format(bigDecimal));
                    map.put("odds", bigDecimal);
                    listPet.add(map);
                }
            }

            List<Map> listEquip = new ArrayList<>();
            // 地图产出装备概率（捕捉装备）
            fields = ConfigMapCatchRatioVo.class.getDeclaredFields();
            for (int i = 3; i < fields.length; i++){

                temp = getFieldValueByName(fields[i].getName(), mapCatchRatioVo);
                bigDecimal = new BigDecimal(temp.toString());
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 1) {
                    Map map = new HashMap();
                    map.put("grade", i - 2);
                    map.put("type", 1);
                    map.put("typeDesc", "捕捉装备");
//                    map.put("odds", percentFormat.format(bigDecimal));
                    map.put("odds", bigDecimal);
                    listEquip.add(map);
                }
            }

            // 地图产出装备概率（对战装备）
            fields = ConfigMapFightRatioVo.class.getDeclaredFields();
            for (int i = 3; i < fields.length; i++){

                temp = getFieldValueByName(fields[i].getName(), mapFightRatioVo);
                bigDecimal = new BigDecimal(temp.toString());
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 1) {
                    Map map = new HashMap();
                    map.put("grade", i - 2);
                    map.put("type", 2);
                    map.put("typeDesc", "对战装备");
//                    map.put("odds", percentFormat.format(bigDecimal));
                    map.put("odds", bigDecimal);
                    listEquip.add(map);
                }
            }

            List<Map> listForage = new ArrayList<>();
            // 地图产出饲料概率
            fields = ConfigMapForageRatioVo.class.getDeclaredFields();
            for (int i = 3; i < fields.length; i++){

                temp = getFieldValueByName(fields[i].getName(), mapForageRatioVo);
                bigDecimal = new BigDecimal(temp.toString());
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 1) {
                    Map map = new HashMap();
                    map.put("grade", i - 2);
//                    map.put("odds", percentFormat.format(bigDecimal));
                    map.put("odds", bigDecimal);
                    listForage.add(map);
                }
            }

            List<Map> listGem = new ArrayList<>();
            // 地图产出珍宝概率
            fields = ConfigMapGemRatioVo.class.getDeclaredFields();
            for (int i = 3; i < fields.length; i++){

                temp = getFieldValueByName(fields[i].getName(), mapGemRatioVo);
                bigDecimal = new BigDecimal(temp.toString());
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 1) {
                    Map map = new HashMap();
//                    map.put("odds", percentFormat.format(bigDecimal));
                    map.put("odds", bigDecimal);
                    listGem.add(map);
                }
            }

            List<Map> listWild = new ArrayList<>();
            // 地图产出野生概率
            fields = ConfigMapWildRatioVo.class.getDeclaredFields();
            for (int i = 3; i < fields.length; i++){

                temp = getFieldValueByName(fields[i].getName(), mapWildRatioVo);
                bigDecimal = new BigDecimal(temp.toString());
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 1) {
                    Map map = new HashMap();
//                    map.put("odds", percentFormat.format(bigDecimal));
                    map.put("odds", bigDecimal);
                    listWild.add(map);
                }
            }

            mapVo.setPetRatioMap(listPet);
            mapVo.setEquipRatioMap(listEquip);
            mapVo.setForageRatioMap(listForage);
            mapVo.setGemRatioMap(listGem);
            mapVo.setWildRatioMap(listWild);

            Map map = new HashMap();
            map.put("mapId", id);
            map.put("userId", loginUser.getUserId());
            userService.update(map);

            return toResponsSuccess(mapVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 解锁地图
     */
    @ApiOperation(value = " 解锁地图")
    @GetMapping(value = "deblock/{id}")
    public Object deblocking(@LoginUser UserVo loginUser, @PathVariable("id") Integer id){

        try {
            MapVo mapVo = mapService.queryObject(id);

            if(loginUser.getOpenMapNum() + 1 != mapVo.getId()) return toResponsObject(400, "请先解锁上一个地图", null);
            List<UserCatchEquipVo> paojieList = catchEquipService.queryObjectByGrade(loginUser.getUserId(), mapVo.getLockLevel(), 1);
            if (paojieList.size() <= 0) return toResponsObject(400, "您还没有装备" + mapVo.getLockLevel() + "级抛接捕捉装备", null);
            List<UserCatchEquipVo> cangshiList = catchEquipService.queryObjectByGrade(loginUser.getUserId(), mapVo.getLockLevel(), 2);
            if (cangshiList.size() <= 0) return toResponsObject(400, "您还没有装备" + mapVo.getLockLevel() + "级藏食捕捉装备", null);
            List<UserCatchEquipVo> kenyaoList = catchEquipService.queryObjectByGrade(loginUser.getUserId(), mapVo.getLockLevel(), 3);
            if (kenyaoList.size() <= 0) return toResponsObject(400, "您还没有装备" + mapVo.getLockLevel() + "级啃咬捕捉装备", null);
            List<UserCatchEquipVo> fashengList = catchEquipService.queryObjectByGrade(loginUser.getUserId(), mapVo.getLockLevel(), 4);
            if (fashengList.size() <= 0) return toResponsObject(400, "您还没有装备" + mapVo.getLockLevel() + "级发声捕捉装备", null);

            // 解锁地图消耗四件装备
            catchEquipService.deleteByUser(loginUser.getUserId(), paojieList.get(0).getId());
            catchEquipService.deleteByUser(loginUser.getUserId(), cangshiList.get(0).getId());
            catchEquipService.deleteByUser(loginUser.getUserId(), kenyaoList.get(0).getId());
            catchEquipService.deleteByUser(loginUser.getUserId(), fashengList.get(0).getId());

            Map map = new HashMap();
            map.put("openMapNum", mapVo.getId());
            map.put("userId", loginUser.getUserId());
            userService.update(map);

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    private static Object getFieldValueByName(String filedName, Object o) {

        try {
            String firstLetter = filedName.substring(0,1).toUpperCase();
            String getter = "get" + firstLetter+filedName.substring(1);
            Method method =o.getClass().getMethod(getter,new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
