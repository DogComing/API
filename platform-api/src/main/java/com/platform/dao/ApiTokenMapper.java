package com.platform.dao;

import com.platform.entity.TokenVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @program: platform
 * @description: 用户Token
 * @author: Yuan
 * @create: 2020-09-09 10:20
 **/
@Mapper
public interface ApiTokenMapper extends BaseDao<TokenVo> {

    TokenVo queryByUserId(@Param("userId") Long userId);

    TokenVo queryByToken(@Param("token") String token);
}
