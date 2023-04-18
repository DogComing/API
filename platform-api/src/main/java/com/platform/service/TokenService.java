package com.platform.service;

import com.platform.dao.ApiTokenMapper;
import com.platform.entity.TokenVo;
import com.platform.utils.CharUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: platform
 * @description: 用户端创建 Token 到 tb_token 表中
 * @author: Yuan
 * @create: 2020-09-04 09:49
 **/
@Service
public class TokenService {

    @Autowired
    private ApiTokenMapper tokenMapper;
    @Autowired
    private RestTemplate restTemplate;

    // 12小时后过期
    private final static int EXPIRE = 3600 * 12;
    // 2小时后过期
    private final static int EXPIRE2 = 3600 * 2;

    public TokenVo queryByUserId(Long userId) { return tokenMapper.queryByUserId(userId); }

    public TokenVo queryByToken(String token) {
        return tokenMapper.queryByToken(token);
    }

    public void save(TokenVo token) {
        tokenMapper.save(token);
    }

    public void update(TokenVo token) {
        tokenMapper.update(token);
    }

    public Map<String, Object> createToken(long userId) {

        //生成一个token
        String token = CharUtil.getRandomString(32);
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        TokenVo tokenVo = queryByUserId(userId);
        if (tokenVo == null) {
            tokenVo = new TokenVo();
            tokenVo.setUserId(userId);
            tokenVo.setToken(token);
            tokenVo.setUpdateTime(now);
            tokenVo.setExpireTime(expireTime);
            save(tokenVo); //保存token
        } else {
            tokenVo.setToken(token);
            tokenVo.setUpdateTime(now);
            tokenVo.setExpireTime(expireTime);
            update(tokenVo); //更新token
        }

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", EXPIRE);

        return map;
    }

}
