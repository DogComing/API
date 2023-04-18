package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.ConfigEffectVo;
import com.platform.entity.UserCatchEquipVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiConfigEffectService;
import com.platform.service.ApiUserCatchEquipService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.utils.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: platform
 * @description: 用户捕捉道具接口
 * @author: Yuan
 * @create: 2020-08-13 17:34
 **/
@Api(tags = "用户捕捉道具接口")
@RestController
@RequestMapping("/api/catch/equip")
public class ApiCatchEquipController extends ApiBaseAction {

    @Autowired
    private ApiUserCatchEquipService catchEquipService;
    @Autowired
    private ApiConfigEffectService configEffectService;

    /**
     * 捕捉道具列表【每次请求10条】
     */
    @IgnoreAuth
    @ApiOperation(value = "捕捉道具列表")
    @PostMapping(value = "all")
    public Object group(@LoginUser UserVo loginUser, @RequestParam(value = "catchType") Integer catchType, @RequestParam(value = "page") Integer page, @RequestParam(value = "size") Integer size) {

        try {
            //查询列表数据
            Map params = new HashMap();
            params.put("page", page);
            params.put("limit", size);
            params.put("sidx", "equip_id");
            params.put("order", "asc");
            params.put("catchType", catchType);
            params.put("userId", loginUser.getUserId());

            Query query = new Query(params);
            int total = catchEquipService.queryTotal(query);
            List<UserCatchEquipVo> groupList = catchEquipService.queryList(params);

            ConfigEffectVo one = null;
            ConfigEffectVo two = null;
            for (int i = 0; i < groupList.size(); i++){
                if(groupList.get(i).getExtraOne() > 0){
                    one = configEffectService.queryObject(groupList.get(i).getExtraOne());
                    groupList.get(i).setExtraOneDesc(one.getEffectDesc());
                } else {
                    groupList.get(i).setExtraOneDesc("无");
                }

                if (groupList.get(i).getExtraTwo() > 0){
                    two = configEffectService.queryObject(groupList.get(i).getExtraTwo());
                    groupList.get(i).setExtraTwoDesc(two.getEffectDesc());
                } else {
                    groupList.get(i).setExtraTwoDesc("无");
                }
            }
            ApiPageUtils goodsData = new ApiPageUtils(groupList, total, query.getLimit(), query.getPage());

            return toResponsSuccess(goodsData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 捕捉道具详情
     */
    @IgnoreAuth
    @ApiOperation(value = " 捕捉道具详情")
    @GetMapping(value = "detail/{id}")
    public Object detail(@LoginUser UserVo loginUser, @PathVariable("id") Integer id) {

        try {
            UserCatchEquipVo userCatchEquipVo = catchEquipService.queryObjectByUser(loginUser.getUserId(), id);
            if (userCatchEquipVo == null) return toResponsObject(400, "未查询到道具信息", null);
            return toResponsSuccess(userCatchEquipVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 使用捕捉道具
     */
    @IgnoreAuth
    @ApiOperation(value = " 使用捕捉道具")
    @GetMapping(value = "use/{id}")
    public Object useCatchEquip(@LoginUser UserVo loginUser, @PathVariable("id") Integer id){

        try {
            UserCatchEquipVo userCatchEquipVo = catchEquipService.queryObjectByUser(loginUser.getUserId(), id);
            if (userCatchEquipVo == null) return toResponsObject(400, "未查询到道具信息", null);

            catchEquipService.updateBatchIsUse(loginUser.getUserId(), userCatchEquipVo.getCatchType());
            Map map = new HashMap();
            map.put("id", id);
            map.put("isUse", 1);
            map.put("userId", loginUser.getUserId());
            catchEquipService.update(map);

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 卸下捕捉道具
     */
    @IgnoreAuth
    @ApiOperation(value = " 卸下捕捉道具")
    @GetMapping(value = "unsnatch/{id}")
    public Object unsnatchCatchEquip(@LoginUser UserVo loginUser, @PathVariable("id") Integer id){

        try {
            UserCatchEquipVo userCatchEquipVo = catchEquipService.queryObjectByUser(loginUser.getUserId(), id);
            if (userCatchEquipVo == null) return toResponsObject(400, "未查询到道具信息", null);

            Map map = new HashMap();
            map.put("id", id);
            map.put("isUse", 0);
            map.put("userId", loginUser.getUserId());
            catchEquipService.update(map);

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 获取当前使用中的道具
     */
    @IgnoreAuth
    @ApiOperation(value = " 获取当前使用中的道具")
    @GetMapping(value = "current")
    public Object currentUse(@LoginUser UserVo loginUser) {

        try {
            // 判断是否使用捕捉装备
            Map params = new HashMap();
            params.put("isUse", 1);
            params.put("userId", loginUser.getUserId());
            List<UserCatchEquipVo> catchEquipVoList = catchEquipService.queryList(params);
            return toResponsSuccess(catchEquipVoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

}
