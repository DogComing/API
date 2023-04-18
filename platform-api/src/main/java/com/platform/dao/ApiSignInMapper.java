package com.platform.dao;

import com.platform.entity.SignInVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApiSignInMapper extends BaseDao<SignInVo>{

    /**
     * 获取签到表信息
     */
    List<SignInVo> queryAllList();
}
