package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.LogASGVo;
import com.platform.entity.MailVo;
import com.platform.entity.SignInVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiLogASGService;
import com.platform.service.ApiMailService;
import com.platform.service.ApiSignInService;
import com.platform.service.ApiUserService;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日签到配置表
 *
 * @author xingGuangTeam
 * @email 249893127@qq.com
 * @date 2019-03-23 15:31
 */
@Api(tags = "每日签到配置表")
@RestController
@RequestMapping("/api/signIn")
public class ApiSignInController extends ApiBaseAction {

    @Autowired
    private ApiSignInService signInService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiMailService mailService;

    @Autowired
    private ApiLogASGService logASGService;

    /***
     * @Description: 获取签到配置表
     * @Param: [loginUser]
     * @return: java.lang.Object
     * @Author: Yuan
     * @Date: 2020/8/6
     */
    @IgnoreAuth
    @ApiOperation(value = "获取签到配置表")
    @GetMapping("all")
    public Object list(@LoginUser UserVo loginUser) {

        try {
            UserVo userVo = userService.queryObject(loginUser.getUserId()); // 查询用户信息
            if (null == userVo) return toResponsObject(400, "未查询到该用户", null);
            List<SignInVo> signInVoList = signInService.queryAllList();
            for (int i = 0; i < userVo.getSignInDayNum(); i++){
                signInVoList.get(i).setIsCheck(true);
            }

            if(!userVo.getIsTodayCheck().equals(1)){
                signInVoList.get(userVo.getSignInDayNum()).setIsToday(true);
            } else if(userVo.getSignInDayNum() >= 31) {
                signInVoList.get(userVo.getSignInDayNum() - 1).setIsToday(true);
            } else {
                signInVoList.get(userVo.getSignInDayNum()).setIsToday(true);
            }

            return toResponsSuccess(signInVoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("网络异常，请重试");
    }

    /**
     * 用户签到接口
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "用户签到")
    @PutMapping("check")
    public Object checkSignIn(@LoginUser UserVo loginUser){

        try {
            UserVo userVo = userService.queryObject(loginUser.getUserId()); // 查询用户信息
            if (null == userVo) return toResponsObject(400, "未查询到该用户", null);
            if (userVo.getIsTodayCheck().equals(1)) return toResponsObject(400, "今日已签到", null);

            List<SignInVo> signInVoList = signInService.queryAllList();
            SignInVo signInVo = signInVoList.get(loginUser.getSignInDayNum());
            Integer awardType = signInVo.getAwardType(); // 签到奖励类型【1:代币  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备】
            Integer kindId = signInVo.getKindId(); // 种类ID【例如：饲料表 一级饲料对应的ID；道具表 某个道具对应的ID】

            MailVo mailVo = new MailVo();
            mailVo.setMailTitle("每日签到奖励");
            mailVo.setMailContent("恭喜您获得：" + signInVo.getAwardNum() + " " + signInVo.getContent());
            mailVo.setAwardNum(signInVo.getAwardNum());
            if (awardType.equals(1) || awardType.equals(2)){
                mailVo.setIsAttribute(0);
            } else {
                mailVo.setIsAttribute(1);
                mailVo.setKindId(kindId);
            }
            mailVo.setImgName(signInVo.getImgName());
            mailVo.setAwardType(awardType);
            mailVo.setUserId(loginUser.getUserId());
            mailVo.setCreateTime(new Date());
            mailVo.setType(1);
            mailVo.setIsReceive(0);
            mailService.save(mailVo);

            Map map = new HashMap();
            map.put("userId", loginUser.getUserId());
            map.put("isTodayCheck", 1);
            map.put("signInDayNum", 1);
            map.put("isHaveUnread", 1);
            userService.update(map);

            if (awardType.equals(1)){
                LogASGVo logASGVo = new LogASGVo();
                logASGVo.setUserId(loginUser.getUserId());
                logASGVo.setNum(new BigDecimal(signInVo.getAwardNum()));
                logASGVo.setLogType(6);
                logASGVo.setLogTypeTxt("签到获取");
                logASGVo.setAsgType(2);
                logASGVo.setAgsTypeTxt("获得");
                logASGVo.setCreateTime(new Date());
                logASGVo.setRemarks("每日签到获取" + signInVo.getAwardNum() + "ASG");
                logASGVo.setAddress(loginUser.getAddress());
                logASGService.save(logASGVo);
            }

            return toResponsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsSuccess(false);
    }
}
