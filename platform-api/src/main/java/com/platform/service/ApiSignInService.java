package com.platform.service;

import com.platform.dao.ApiSignInMapper;
import com.platform.entity.SignInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiSignInService {

    @Autowired
    private ApiSignInMapper signInMapper;

    public List<SignInVo> queryAllList() {
        return signInMapper.queryAllList();
    }
}
