package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: platform
 * @description: NFT实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("NFT实体类")
public class NftVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ApiModelProperty("自增主键")
    private Integer id;

    /**
     * 用户userId
     */
    @ApiModelProperty("用户userId")
    private Long userId;

    /**
     * NFT的ID，唯一序号
     */
    @ApiModelProperty("NFT的ID，唯一序号")
    private Long nftId;

    /**
     * NFT的名称
     */
    @ApiModelProperty("NFT的名称")
    private String name;

    /**
     * NFT的描述
     */
    @ApiModelProperty("NFT的描述")
    private String description;

    /**
     * NFT的远程图片地址
     */
    @ApiModelProperty("NFT的远程图片地址")
    private String image;

    /**
     * NFT的json文件所在位置
     */
    @ApiModelProperty("NFT的json文件所在位置")
    private String jsonUrl;

    /**
     * 是否冻结【0否 1:是】
     */
    @ApiModelProperty("是否冻结【0否 1:是】")
    private Integer isFreeze;

    /**
     * 是否已提取到基地【0:否 1:是】
     */
    @ApiModelProperty("是否已提取到基地【0:否 1:是】")
    private Integer isDraw;

    /**
     * 是否删除【0:否 1:是】
     */
    @ApiModelProperty("是否删除【0:否 1:是】")
    private Integer isDelete;

    /**
     * NFT的属性，已数组形式赋值，每个属性有两个参数（trait_type属性名称，value属性数值）
     */
    @ApiModelProperty("NFT的属性，已数组形式赋值，每个属性有两个参数（trait_type属性名称，value属性数值）")
    private String attributes;

    /**
     * 铸造NFT 【1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备 7：珍宝】
     */
    @ApiModelProperty("铸造NFT 【1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备 7：珍宝】")
    private Integer type;

    /**
     * 珍宝类型 【1：饲料 2：捕捉 3：战斗】
     */
    @ApiModelProperty("珍宝类型 【1：饲料 2：捕捉 3：战斗】")
    private Integer gemType;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
