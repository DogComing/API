package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: platform
 * @description: 用户道具实体类
 * @author: Yuan
 * @create: 2020-09-09 10:20
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("用户道具实体类")
public class UserPropVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增主键Id")
    private Integer id;

    @ApiModelProperty("道具等级")
    private Integer grade;

    /**
     * 用户userId
     */
    @ApiModelProperty("用户userId")
    private Long userId;

    /**
     * 道具ID
     */
    @ApiModelProperty("道具ID")
    private Integer propId;

    /**
     * 道具名称
     */
    @ApiModelProperty("道具名称")
    private String propName;

    /**
     * 道具描述
     */
    @ApiModelProperty("道具描述")
    private String propDesc;

    /**
     * 道具图片
     */
    @ApiModelProperty("道具图片")
    private String imgName;

    /**
     * 道具类型【1:普通道具 2:珍宝道具】
     */
    @ApiModelProperty("道具类型【1:普通道具 2:珍宝道具】")
    private Integer propType;

    /**
     * 使用场景类型（1:战斗场景 2:其他场景）
     */
    @ApiModelProperty("使用场景类型（1:战斗场景 2:其他场景）")
    private Integer useType;

    /**
     * 道具属性类型【1:强制增加宠物2点成长  2:参赛时免扣5点体力  3:参赛时免交报名费  4:参赛时临时增加战斗力  5:参赛结束后减少宠物冷却时间  6:随机开出1～5级捕捉装备  7:随机开出1～5级品质的狗狗】
     */
    @ApiModelProperty("道具属性类型【1:强制增加宠物2点成长  2:参赛时免扣5点体力  3:参赛时免交报名费  4:参赛时临时增加战斗力  5:参赛结束后减少宠物冷却时间  6:随机开出1～5级捕捉装备  7:随机开出1～5级品质的狗狗】")
    private Integer attributeType;

    /**
     * 道具数量
     */
    @ApiModelProperty("道具数量")
    private Integer propNum;

    /**
     * 是否NFT（0:否 1:是）
     */
    @ApiModelProperty("是否NFT（0:否 1:是）")
    private Integer isNft;

    /**
     * 是否已提取到基地【0:否 1:是】
     */
    @ApiModelProperty("是否已提取到基地【0:否 1:是】")
    private Integer isDraw;

    /**
     * 是否冻结【0否 1:是】
     */
    @JsonIgnore
    @ApiModelProperty("是否冻结【0否 1:是】")
    private Integer isFreeze;

    /**
     * NFT的ID，唯一序号
     */
    @JsonIgnore
    @ApiModelProperty("NFT的ID，唯一序号")
    private Long nftId;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 1: 狗 2；物品 3：失败
     */
    private Integer type;

    /**
     * NFT类型：1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备
     */
    private Integer nftType;
}
