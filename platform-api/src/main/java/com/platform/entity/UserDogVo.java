package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("用户狗狗实体类")
public class UserDogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Integer id;

    /**
     * 用户userId
     */
    @ApiModelProperty("用户userId")
    private Long userId;

    /**
     * 狗狗ID
     */
    @ApiModelProperty("狗狗ID")
    private Integer dogId;

    /**
     * 狗狗品质
     */
    @ApiModelProperty("狗狗品质")
    private Integer dogGrade;

    /**
     * 狗狗名称
     */
    @ApiModelProperty("狗狗名称")
    private String dogName;

    /**
     * 狗狗描述
     */
    @ApiModelProperty("狗狗描述")
    private String dogDesc;

    /**
     * 狗狗图片
     */
    @ApiModelProperty("狗狗图片")
    private String imgName;

    /**
     * 狗狗动画
     */
    @ApiModelProperty("狗狗动画")
    private String animationName;

    /**
     * 狗狗品种
     */
    @ApiModelProperty("狗狗品种")
    private String dogBreed;

    /**
     * 狗狗战力值
     */
    @ApiModelProperty("狗狗战力值")
    private BigDecimal fightingNum;

    /**
     * 速度
     */
    @ApiModelProperty("狗狗速度")
    private Integer speed;

    /**
     * 心情
     */
    @ApiModelProperty("狗狗心情")
    private Integer mood;

    /**
     * 耐力
     */
    @ApiModelProperty("狗狗耐力")
    private Integer endurance;

    /**
     * 幸运
     */
    @ApiModelProperty("狗狗幸运")
    private Integer luck;

    /**
     * 狗狗最大成长次数
     */
    @ApiModelProperty("狗狗最大成长次数")
    private Integer maxGrowUpNum;

    /**
     * 狗狗当前成长次数
     */
    @ApiModelProperty("狗狗当前成长次数")
    private Integer currentGrowUpNum;

    /**
     * 狗狗天赋系数
     */
    @ApiModelProperty("狗狗天赋系数")
    private BigDecimal inbornNum;

    /**
     * 是否使用中【0:否 1:是】
     */
    @ApiModelProperty("是否使用中【0:否 1:是】")
    private Integer isUse;

    /**
     * 是否比赛中【0:否 1:是】
     */
    @ApiModelProperty("是否比赛中【0:否 1:是】")
    private Integer isGame;

    /**
     * 是否冷却中【0:否 1:是】
     */
    @ApiModelProperty("是否冷却中【0:否 1:是】")
    private Integer isCool;

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
     * 冷却结束时间
     */
    @ApiModelProperty("冷却结束时间")
    private Date coolTime;

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

    /*********************参赛属性*********************/

    /**
     * 冲刺次数
     */
    private Integer spurtNum;

    /**
     * 跑完秒数
     */
    private BigDecimal finishSec;

    /**
     * 第几名
     */
    private Integer ranking;


    /**
     * 士气
     */
    private Integer morale;

    /**
     * 狗狗主人名字
     */
    private String masterName;

    /**
     * 权值
     */
    @JsonIgnore
    private BigDecimal weight;

    /**
     * 绳子长度
     */
    @JsonIgnore
    private BigDecimal cord;

    public Integer getMorale() {

        if (morale == null) return 0;
        return morale;
    }

    public Integer getNftType() {
        if (nftType == null) return 1;
        return nftType;
    }

    public Integer getDiscardType() {
        if (discardType == null) return 1;
        return discardType;
    }
}
