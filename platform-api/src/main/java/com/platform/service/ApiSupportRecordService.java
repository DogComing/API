package com.platform.service;

import com.platform.dao.ApiSupportRecordMapper;
import com.platform.entity.SupportRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: platform
 * @description:
 * @author: Yuan
 * @create: 2020-08-14 09:40
 **/
@Service
public class ApiSupportRecordService {

    @Autowired
    private ApiSupportRecordMapper supportRecordMapper;

    public SupportRecordVo queryObject(Integer id) { return supportRecordMapper.queryObject(id); }

    public List<SupportRecordVo> queryListByGameNum(String gameNum) { return supportRecordMapper.queryListByGameNum(gameNum); }

    public List<SupportRecordVo> recordByGameNum(Long userId, String gameNum) { return supportRecordMapper.recordByGameNum(userId, gameNum); }

    public void save(SupportRecordVo supportRecordVo) { supportRecordMapper.save(supportRecordVo); }
}
