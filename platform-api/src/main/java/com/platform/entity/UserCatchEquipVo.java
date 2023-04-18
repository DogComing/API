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
 * @description: 用户捕捉道具实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("用户捕捉道具实体类")
public class UserCatchEquipVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增主键Id")
    private Integer id;

    /**
     * 捕捉装备等级
     */
    @ApiModelProperty("捕捉装备等级")
    private Integer grade;

    /**
     * 用户UserId
     */
    @ApiModelProperty("用户UserId")
    private Long userId;

    /**
     * 捕捉道具ID
     */
    @ApiModelProperty("捕捉道具ID")
    private Integer equipId;

    /**
     * 捕捉装备名称
     */
    @ApiModelProperty("捕捉装备名称")
    private String equipName;

    /**
     * 捕捉装备描述
     */
    @ApiModelProperty("捕捉装备描述")
    private String equipDesc;

    /**
     * 捕捉装备图片名称
     */
    @ApiModelProperty("捕捉装备图片名称")
    private String imgName;

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
     * 捕捉类别【1：抛接  2：藏食  3：啃咬  4：发声】
     */
    @ApiModelProperty("捕捉类别【1：抛接  2：藏食  3：啃咬  4：发声】")
    private Integer catchType;

    /**
     * 是否使用中（0:否 1:是）
     */
    @ApiModelProperty("是否使用中（0:否 1:是）")
    private Integer isUse;

    /**
     * 作用类别
     */
    @ApiModelProperty("作用类别")
    private Integer deedType;

    /**
     * 是否有额外效果（0:否 1:是）
     */
    @JsonIgnore
    @ApiModelProperty("是否有额外效果（0:否 1:是）")
    private Integer extraOne;

    /**
     * 是否有额外效果（0:否 1:是）
     */
    @JsonIgnore
    @ApiModelProperty("是否有额外效果（0:否 1:是）")
    private Integer extraTwo;

    /**
     * 是否珍宝【0:否 1:是】
     */
    @ApiModelProperty("是否珍宝【0:否 1:是】")
    private Integer isGem;

    /**
     * 装备数量
     */
    @ApiModelProperty("装备数量")
    private Integer equipNum;

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

    /**
     * 额外效果描述1
     */
    private String extraOneDesc;

    /**
     * 额外效果描述2
     */
    private String extraTwoDesc;
}
