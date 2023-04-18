package com.platform.service;

import com.platform.dao.ApiMailMapper;
import com.platform.entity.MailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiMailService {

    @Autowired
    private ApiMailMapper mailMapper;

    public List<MailVo> queryList(Map<String, Object> map){ return mailMapper.queryList(map); }

    public int queryTotal(Map<String, Object> map) { return mailMapper.queryTotal(map); }

    public MailVo queryObject(Integer id) { return mailMapper.queryObject(id); }

    public void save(MailVo mailVo) { mailMapper.save(mailVo); }

    public int update(Map<String, Object> map) { return mailMapper.update(map); }

    public List<MailVo> allList(Long userId){ return mailMapper.allList(userId); }

    public List<MailVo> allListByMap(Map<String, Object> map){ return mailMapper.allListByMap(map); }

    public int deleteByUser(Map<String, Object> map){ return mailMapper.deleteByUser(map); }

    public int updateByUser(Map<String, Object> map) { return mailMapper.updateByUser(map); }
}
