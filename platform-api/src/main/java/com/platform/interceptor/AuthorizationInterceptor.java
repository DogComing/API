package com.platform.interceptor;

import com.platform.annotation.IgnoreAuth;
import com.platform.entity.TokenVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserService;
import com.platform.service.TokenService;
import com.platform.utils.ApiRRException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限(Token)验证
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ApiUserService userService;

    public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";
    public static final String LOGIN_TOKEN_KEY = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        //response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        IgnoreAuth annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
        } else {
            return true;
        }

        // 从header中获取token
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        // 如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(LOGIN_TOKEN_KEY);
        }

        // token为空
        if (StringUtils.isBlank(token)) {
            // 如果有@IgnoreAuth注解，则不验证token
            if (annotation != null) {
                return true;
            } else {
                throw new ApiRRException("请先登录", 401);
            }
        }

        if (request.getAttribute(LOGIN_USER_KEY) == null) {

            TokenVo tokenVo = tokenService.queryByToken(token);

            if (tokenVo == null && annotation == null) throw new ApiRRException("token已过期", 401);

            UserVo userVo = userService.queryObject(tokenVo.getUserId());

            if (userVo == null && annotation == null) {
                throw new ApiRRException("请先登录", 401);
            }
            //设置userId到request里，后续根据userId，获取用户信息
            request.setAttribute(LOGIN_USER_KEY, userVo);
        }
        return true;
    }
}