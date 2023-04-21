package com.platform.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.LoginInfo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserService;
import com.platform.service.TokenService;
import com.platform.util.ApiBaseAction;
import com.platform.util.MD5Util;
import com.platform.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.platform.config.ConstantConfig.*;

/**
 * 登录授权接口
 */
@Api(tags = "登录授权接口")
@RestController
@RequestMapping("/api/user")
public class ApiAuthController extends ApiBaseAction {

    @Autowired
    private ApiUserService userService;
    @Autowired
    private TokenService tokenService;

    /**
     * 用户扫码授权接口
     * @param loginInfo
     * @return
     */
    @IgnoreAuth
    @PostMapping("auth")
    @ApiOperation(value = "用户扫码授权接口")
    public Object storeAuth(@RequestBody LoginInfo loginInfo) {

        try {

            if(loginInfo.getLogonCredentials() != null && loginInfo.getLogonCredentials() != ""){
                Integer nonce = (int)((Math.random() * 9 + 1) * 1000);
                Map<String,Object> form = new HashMap<>();
                form.put("code", loginInfo.getLogonCredentials());
                form.put("nonce", nonce);
                form.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                String sign = MD5Util.encodeSign(form, key);
                form.put("sign", sign);
                HttpResponse res = HttpRequest.post(authUrl).form(form).header("Authorization", authorization).timeout(10000).execute();
                String body = res.body();
                System.out.println("基地返回消息：" + body);
                JSONObject result = JSON.parseObject(body);
                if (result.getInteger("code") == 200 && result.getString("message").equals("success")){
                    JSONObject data = result.getJSONObject("data");

//                    if (!data.getString("openid").equals("RDBXZmNaS1RDS0Y5djJXZw==")) return toResponsFail("稍等片刻，停服更新中....");
                    // 查询用户信息
                    UserVo userVo = userService.queryByOpenId(data.getString("openid"));
                    // if (userVo != null && userVo.getStatus() == 2) return toResponsFail("该账号涉嫌违规操作，如有疑问请联系管理员");

                    if (null == userVo) {
                        Date nowTime = new Date();
                        userVo = new UserVo();
                        userVo.setRegisterTime(nowTime);
                        userVo.setRegisterIp(this.getClientIp());
                        userVo.setUserName("狗狗" + data.getInteger("role_nft"));
                        userVo.setTotalMuscleNum(30);
                        userVo.setResidueMuscleNum(30);
                        userVo.setLogonCredentials(loginInfo.getLogonCredentials());
                        userVo.setLastLoginIp(this.getClientIp());
                        userVo.setLastLoginTime(nowTime);
                        userVo.setOpenId(data.getString("openid"));
                        userVo.setAddress(data.getString("address"));
                        userVo.setRole(data.getInteger("role"));
                        userVo.setRoleNft(data.getInteger("role_nft"));
                        userService.save(userVo);
                    } else {
                        // 更新最后登录时间，登录Ip
                        Date nowTime = new Date();
                        Map map = new HashMap();
                        map.put("userId", loginInfo.getUserId());
                        map.put("lastLoginTime", nowTime);
                        map.put("lastLoginIp", this.getClientIp());
                        userService.update(map);
                    }

                    Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
                    String token = MapUtils.getString(tokenMap, "token");
                    if (StringUtils.isNullOrEmpty(token)) return toResponsFail("登录失败");

                    Map resultMap = new HashMap();
                    resultMap.put("token", token);
                    resultMap.put("openId", data.getString("openid"));

                    return toResponsSuccess(resultMap);
                }
            }
            return toResponsFail("登录凭证信息错误");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsFail("网络异常，请重试");
    }

    /**
     * 用户快速登录
     * @param loginInfo
     * @return
     */
    @IgnoreAuth
    @PostMapping("login")
    @ApiOperation(value="用户快速登录")
    public Object storeLogin(@RequestBody LoginInfo loginInfo){

        try {

//            if (!loginInfo.getOpenId().equals("RDBXZmNaS1RDS0Y5djJXZw==")) return toResponsFail("稍等片刻，停服更新中....");

            if(loginInfo.getOpenId() != null && loginInfo.getOpenId() != ""){
                // 查询用户信息
                UserVo userVo = userService.queryByOpenId(loginInfo.getOpenId());
                // if (userVo != null && userVo.getStatus() == 2) return toResponsFail("该账号涉嫌违规操作，如有疑问请联系管理员");

                // 更新最后登录时间，登录Ip
                Date nowTime = new Date();
                Map map = new HashMap();
                map.put("userId", loginInfo.getUserId());
                map.put("lastLoginTime", nowTime);
                map.put("lastLoginIp", this.getClientIp());
                userService.update(map);

                Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
                String token = MapUtils.getString(tokenMap, "token");
                if (StringUtils.isNullOrEmpty(token)) return toResponsFail("登录失败");

                Map resultMap = new HashMap();
                resultMap.put("token", token);
                resultMap.put("openId", loginInfo.getOpenId());
                return toResponsSuccess(token);
            }
            return toResponsFail("登录已过期，请重新扫码登录");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsFail("登录已过期，请重新扫码登录");
    }
}
