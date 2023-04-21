package com.platform.dao;

import com.platform.entity.ConfigVo;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @program: platform
 * @description: 平台配置信息相关
 * @author: Yuan
 * @create: 2020-09-08 12:26
 **/
@Mapper
public interface ApiConfigMapper extends BaseDao<ConfigVo>{

    /**
     * 查询某个String值
     * @param key
     * @return
     */
    String queryByStringKey(String key);

    /**
     * 查询某个Int值
     * @param key
     * @return
     */
    Integer queryByIntKey(String key);

    /**
     * 查询某个BigDecimal值
     * @param key
     * @return
     */
    BigDecimal queryByBigDecimalKey(String key);

    /**
     * 更新某个字段值
     * @param map
     */
    void updateValueByKey(Map<String, Object> map);
}
