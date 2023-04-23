package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户对战装备实体类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("用户对战装备实体类")
public class UserFightEquipVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增主键Id")
    private Integer id;

    /**
     * 对战装备等级
     */
    @ApiModelProperty("对战装备等级")
    private Integer grade;

    /**
     * 用户userId
     */
    @ApiModelProperty("用户userId")
    private Long userId;

    /**
     * 对战装备ID
     */
    @ApiModelProperty("对战装备ID")
    private Integer fightId;

    /**
     * 对战装备名称
     */
    @ApiModelProperty("对战装备名称")
    private String fightName;

    /**
     * 对战装备描述
     */
    @ApiModelProperty("对战装备描述")
    private String fightDesc;

    /**
     * 对战装备图片名称
     */
    @ApiModelProperty("对战装备图片名称")
    private String imgName;

    /**
     * 战力加成百分比
     */
    @ApiModelProperty("战力加成百分比")
    private BigDecimal fightingAddition;

    /**
     * 是否珍宝【0:否 1:是】
     */
    @ApiModelProperty("是否珍宝【0:否 1:是】")
    private Integer isGem;

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

    /**
     * 最大耐久度
     */
    @ApiModelProperty("最大耐久度")
    private Integer durabilityMax;

    /**
     * 剩余耐久度
     */
    @ApiModelProperty("剩余耐久度")
    private Integer durabilityResidue;

    /**
     * 对战装备数量
     */
    @ApiModelProperty("对战装备数量")
    private Integer fightNum;

    /**
     * 对战装备类型（1:狗绳 2:狗铃 3:狗披风）
     */
    @ApiModelProperty("对战装备类型（1:狗绳 2:狗铃 3:狗披风）")
    private Integer fightType;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 1: 宠物 2；珍宝 3：饲料 4：野生 5：捕捉装备 6：对战装备 7：失败
     */
    private Integer type;

    /**
     * NFT类型：1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备
     */
    private Integer nftType;

    /**
     * 丢弃类型 1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备
     */
    private Integer discardType;
}
