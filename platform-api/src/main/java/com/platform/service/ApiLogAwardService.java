package com.platform.service;

import com.platform.dao.ApiLogAwardMapper;
import com.platform.entity.LogAwardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiLogAwardService {

    @Autowired
    private ApiLogAwardMapper logAwardMapper;

    public LogAwardVo queryObject(Integer id) { return logAwardMapper.queryObject(id); }

    public List<LogAwardVo> queryListByGameNum(String gameNum) { return logAwardMapper.queryListByGameNum(gameNum); }

    public List<LogAwardVo> logByGameNum(Long userId, String gameNum) { return logAwardMapper.logByGameNum(userId, gameNum); }

    public void save(LogAwardVo logAwardVo) { logAwardMapper.save(logAwardVo); }
}
