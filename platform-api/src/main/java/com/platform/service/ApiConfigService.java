package com.platform.service;

import com.platform.dao.ApiConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @program: platform
 * @description: 平台配置信息实现类
 * @author: Yuan
 * @create: 2020-09-08 12:29
 **/
@Service
public class ApiConfigService {

    @Autowired
    private ApiConfigMapper configMapper;

    public String queryByStringKey(String key) { return configMapper.queryByStringKey(key); }

    public Integer queryByIntKey(String key) { return configMapper.queryByIntKey(key); }

    public BigDecimal queryByBigDecimalKey(String key) { return configMapper.queryByBigDecimalKey(key); }

    public void updateValueByKey(Map<String, Object> map) { configMapper.updateValueByKey(map); }

}
