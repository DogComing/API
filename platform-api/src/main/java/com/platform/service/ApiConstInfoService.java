package com.platform.service;

import com.platform.dao.ApiConstInfoMapper;
import com.platform.entity.ConstInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiConstInfoService {

    @Autowired
    private ApiConstInfoMapper constInfoMapper;

    public String queryByStringKey(String key) { return constInfoMapper.queryByStringKey(key); }

    public Integer queryByIntKey(String key) { return constInfoMapper.queryByIntKey(key); }

    public List<ConstInfoVo> queryList(Map<String, Object> map) { return constInfoMapper.queryList(map); }

    public void updateValueByKey(Map<String, Object> map) { constInfoMapper.updateValueByKey(map); }
}
