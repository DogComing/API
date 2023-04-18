package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: platform
 * @description: 地图产出珍宝概率实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("地图产出珍宝概率实体类")
public class ConfigMapGemRatioVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Integer id;

    @ApiModelProperty("地图ID")
    private Integer mapId;

    /**
     * 珍宝饲料产出概率
     */
    @ApiModelProperty("珍宝饲料产出概率")
    private Integer gemForage;

    /**
     * 珍宝装备产出概率
     */
    @ApiModelProperty("珍宝装备产出概率")
    private Integer gemEquip;

    /**
     * 珍宝道具产出概率
     */
    @ApiModelProperty("珍宝道具产出概率")
    private Integer gemProp;
}
