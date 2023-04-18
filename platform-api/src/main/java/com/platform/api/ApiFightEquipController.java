package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.UserFightEquipVo;
import com.platform.entity.UserVo;
import com.platform.entity.UserWildVo;
import com.platform.service.ApiUserFightEquipService;
import com.platform.service.ApiUserWildService;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: platform
 * @description: 用户对战装备接口
 * @author: Yuan
 * @create: 2020-08-13 17:34
 **/
@Api(tags = "用户对战装备接口")
@RestController
@RequestMapping("/api/fight/equip")
public class ApiFightEquipController extends ApiBaseAction {

    @Autowired
    private ApiUserFightEquipService userFightEquipService;
    @Autowired
    private ApiUserWildService userWildService;

    /**
     * 对战装备列表
     */
    @IgnoreAuth
    @ApiOperation(value = "对战装备列表")
    @GetMapping(value = "all")
    public Object group(@LoginUser UserVo loginUser) {

        try {

            List<Map> tempList = new ArrayList<>();
            List<UserFightEquipVo> listf = userFightEquipService.allList(loginUser.getUserId());
            for (int i = 0; i < listf.size(); i++){
                Map map = new HashMap();
                map.put("id", listf.get(i).getId());
                map.put("name", listf.get(i).getFightName());
                map.put("desc", listf.get(i).getFightDesc());
                map.put("img", listf.get(i).getImgName());
                map.put("num", listf.get(i).getFightNum());
                map.put("type", listf.get(i).getFightType());
                map.put("grade", listf.get(i).getGrade());
                map.put("durabilityMax", listf.get(i).getDurabilityMax());
                map.put("durabilityResidue", listf.get(i).getDurabilityResidue());
                map.put("ratio", listf.get(i).getFightingAddition());
                tempList.add(map);
            }

            List<UserWildVo> userWildVoList = userWildService.allListByFight(loginUser.getUserId(), 1);
            for (int i = 0; i < userWildVoList.size(); i++){
                Map map = new HashMap();
                map.put("id", userWildVoList.get(i).getId());
                map.put("name", userWildVoList.get(i).getWildName());
                map.put("desc", userWildVoList.get(i).getWildDesc());
                map.put("img", userWildVoList.get(i).getImgName());
                map.put("num", userWildVoList.get(i).getWildNum());
                map.put("grade", userWildVoList.get(i).getGrade());
                map.put("durabilityMax", userWildVoList.get(i).getDurabilityMax());
                map.put("durabilityResidue", userWildVoList.get(i).getDurabilityResidue());
                map.put("fightingNum", userWildVoList.get(i).getFightingNum());
                map.put("type", userWildVoList.get(i).getWildType());
                tempList.add(map);
            }

            return toResponsSuccess(tempList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

}
