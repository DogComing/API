package com.platform.pojo;

import com.platform.service.ApiConfigService;
import com.platform.service.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

import static com.platform.config.ConstantConfig.*;

/**
 * @program: platform
 * @description: 程序自启组件类
 * @author: Yuan
 * @create: 2020-09-16 15:06
 **/
@Component
public class ServerConfig {

    @Autowired
    private ApiConfigService configService;

    @PostConstruct
    public void automatic() {

        String str = appId + ":" + appSecret;
        authorization = "Bearer " + Base64.getEncoder().encodeToString(str.getBytes());

        jackpot = configService.queryByBigDecimalKey("jackpot");
        shareOutBonus = configService.queryByBigDecimalKey("share_out_bonus");

        System.out.println("场外奖金池：" + jackpot);
        System.out.println("平台分红：" + shareOutBonus);
    }
}
