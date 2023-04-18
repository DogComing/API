package com.platform.dao;

import com.platform.entity.NftVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiNftMapper extends BaseDao<NftVo>{

    int queryTotal(@Param("type") Integer type);

    List<NftVo> allNft();

    NftVo queryObjectByUser(@Param("userId") Long userId, @Param("nftId") Long nftId);

    int deleteNftByNftId(@Param("nftId") Long nftId);
}
