package com.platform.api;

import com.platform.annotation.LoginUser;
import com.platform.entity.ConfigAdVo;
import com.platform.entity.LogAdVo;
import com.platform.entity.MailVo;
import com.platform.entity.UserVo;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: platform
 * @description: 广告接口
 * @author: Yuan
 * @create: 2020-08-13 17:34
 **/
@Api(tags = "广告接口")
@RestController
@RequestMapping("/api/ad")
public class ApiConfigAdController extends ApiBaseAction {

    @Autowired
    private ApiConfigAdService apiConfigAdService;
    @Autowired
    private ApiLogAdService apiLogAdService;

    @Autowired
    private ApiMailService mailService;
    @Autowired
    private ApiUserService userService;

    /**
     * 广告列表
     */
    @ApiOperation(value = "广告列表")
    @GetMapping(value = "all")
    public Object mapList(@LoginUser UserVo loginUser) {

        try {
            LogAdVo logAdVo = null;
            List<ConfigAdVo> groupList = apiConfigAdService.allAd();
            for (int i = 0; i < groupList.size(); i++){
                logAdVo = apiLogAdService.queryObjectByUser(loginUser.getUserId(), groupList.get(i).getId());
                if (logAdVo != null) groupList.get(i).setIsLook(true);
            }
            return toResponsSuccess(groupList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 广告详情
     */
    @ApiOperation(value = " 广告详情")
    @GetMapping(value = "detail/{id}")
    public Object detail(@LoginUser UserVo loginUser, @PathVariable("id") Integer id) {

        try {
            if (loginUser.getDaysUseBrawn() < 30) return toResponsObject(400,"当天消耗精力值大于30点可看广告", null);
            ConfigAdVo adVo = apiConfigAdService.queryObject(id);
            if(adVo == null) return toResponsObject(400,"未查询该广告信息", null);

            LogAdVo logAdVo = apiLogAdService.queryObjectByUser(loginUser.getUserId(), adVo.getId());
            if (logAdVo != null) adVo.setIsLook(true);
            return toResponsSuccess(adVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 观看广告完毕
     */
    @ApiOperation(value = " 观看广告完毕")
    @GetMapping(value = "look/{id}")
    public Object lookAd(@LoginUser UserVo loginUser, @PathVariable("id") Integer id){

        try {
            ConfigAdVo adVo = apiConfigAdService.queryObject(id);
            if(adVo == null) return toResponsObject(400,"未查询该广告信息", null);

            LogAdVo logAdVo = new LogAdVo();
            logAdVo.setAdId(id);
            logAdVo.setUserId(loginUser.getUserId());
            logAdVo.setCreateTime(new Date());
            apiLogAdService.save(logAdVo);

            MailVo mailVo = new MailVo();
            mailVo.setType(1);
            mailVo.setMailTitle("观看广告奖励");
            mailVo.setMailContent("广告观看完毕，系统奖励您：" + adVo.getAwardName());
            mailVo.setAwardType(adVo.getAwardType());
            mailVo.setImgName(adVo.getImgName());
            mailVo.setAwardNum(adVo.getAwardNum());
            mailVo.setIsReceive(0);
            mailVo.setIsAttribute(0);
            mailVo.setIsDelete(0);
            mailVo.setUserId(loginUser.getUserId());
            mailVo.setCreateTime(new Date());
            mailService.save(mailVo);

            Map map = new HashMap();
            map.put("userId", loginUser.getUserId());
            map.put("isHaveUnread", 1);
            userService.update(map);

            return toResponsSuccess("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }
}
