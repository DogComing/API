package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: platform
 * @description: 用户野生实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("用户野生实体类")
public class UserWildVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增主键Id")
    private Integer id;

    @ApiModelProperty("野生等级")
    private Integer grade;

    /**
     * 用户UserId
     */
    @ApiModelProperty("用户UserId")
    private Long userId;

    /**
     * 野生Id
     */
    @ApiModelProperty("野生Id")
    private Integer wildId;

    /**
     * 野生名称
     */
    @ApiModelProperty("野生名称")
    private String wildName;

    /**
     * 野生描述
     */
    @ApiModelProperty("野生描述")
    private String wildDesc;

    /**
     * 野生图片名称
     */
    @ApiModelProperty("野生图片名称")
    private String imgName;

    /**
     * 增加战力点数
     */
    @ApiModelProperty("增加战力点数")
    private BigDecimal fightingNum;

    /**
     * 是否可战斗使用【0:否 1:是】
     */
    @ApiModelProperty("是否可战斗使用【0:否 1:是】")
    private Integer isFight;

    /**
     * 野生类型（1:破绳子 2:破鞋子 3:破抹布 4:其他）
     */
    @ApiModelProperty("野生类型（1:破绳子 2:破鞋子 3:破抹布 4:其他）")
    private Integer wildType;

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
     * 是否忽略天赋（0:否 1:是）
     */
    @ApiModelProperty("是否忽略天赋（0:否 1:是）")
    private Integer isIgnoreTalent;

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
     * 野生数量
     */
    @ApiModelProperty("野生数量")
    private Integer wildNum;

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
