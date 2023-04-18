package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: platform
 * @description: 额外效果配置表实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("额外效果配置表实体类")
public class ConfigEffectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @ApiModelProperty("自增主键ID")
    private Integer id;

    /**
     * 额外描述
     */
    @ApiModelProperty("额外描述")
    private String effectDesc;

    /**
     * 额外增加战力值数量
     */
    @ApiModelProperty("额外增加战力值数量")
    private Integer fightingNum;

    /**
     * 额外恢复体力数量
     */
    @ApiModelProperty("额外恢复体力数量")
    private Integer muscleNum;

    /**
     * 额外获取珍宝概率
     */
    @ApiModelProperty("额外获取珍宝概率")
    private BigDecimal gemOdds;

    /**
     * 额外恢复体力百分比
     */
    @ApiModelProperty("额外恢复体力百分比")
    private BigDecimal addMuscleOdds;

    /**
     * 额外效果获取概率
     */
    @ApiModelProperty("额外效果获取概率")
    private BigDecimal ratio;

    /**
     * 额外获取类型（1:体力 2:珍宝 3:战力）
     */
    @ApiModelProperty("额外获取类型（1:体力 2:珍宝 3:战力）")
    private Integer type;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
