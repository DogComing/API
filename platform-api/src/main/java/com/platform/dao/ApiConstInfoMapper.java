package com.platform.dao;

import com.platform.entity.ConstInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ApiConstInfoMapper extends BaseDao<ConstInfoVo>{

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
     * 更新某个字段值
     * @param map
     */
    void updateValueByKey(Map<String, Object> map);
}
