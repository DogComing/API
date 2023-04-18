package com.platform.service;

import com.platform.dao.ApiNftMapper;
import com.platform.entity.NftVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiNftService {

    @Autowired
    private ApiNftMapper nftMapper;

    public int queryTotal(Integer type) { return nftMapper.queryTotal(type); }

    /**
     * 查询NFT详情
     * @param userId
     * @param nftId
     * @return
     */
    public NftVo queryObjectByUser(Long userId, Long nftId) { return nftMapper.queryObjectByUser(userId, nftId); }

    /**
     * 查询所有NFT
     * @return
     */
    public List<NftVo> allNft() { return nftMapper.allNft(); }

    /**
     * 新增NFT
     * @param nftVo
     */
    public void save(NftVo nftVo) { nftMapper.save(nftVo); }

    /**
     * 更新NFT
     * @param nftVo
     */
    public int update(NftVo nftVo) { return nftMapper.update(nftVo); }

    /**
     * 删除NFT
     * @param id
     */
    public int delete(Integer id) { return nftMapper.delete(id); }

    /**
     * 根据nftId删除NFT
     * @return
     */
    public int deleteNftByNftId(Long nftId) { return nftMapper.deleteNftByNftId(nftId); }
}
