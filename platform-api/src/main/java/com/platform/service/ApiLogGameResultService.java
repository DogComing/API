package com.platform.service;

import com.platform.dao.ApiLogGameResultMapper;
import com.platform.entity.LogGameResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiLogGameResultService {

    @Autowired
    private ApiLogGameResultMapper gameResultMapper;

    public List<LogGameResultVo> queryList(Map<String, Object> map) { return gameResultMapper.queryList(map); }

    /**
     * 新增比赛结果记录
     * @param logGameResultVo
     */
    public void save(LogGameResultVo logGameResultVo) { gameResultMapper.save(logGameResultVo); }

    public List<LogGameResultVo> queryListByGameNum(String gameNum) { return gameResultMapper.queryListByGameNum(gameNum); }
}
