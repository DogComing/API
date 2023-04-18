package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: platform
 * @description: 地图实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("地图实体类")
public class MapVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("地图Id")
    private Integer id;

    @JsonIgnore
    @ApiModelProperty("地图等级")
    private Integer mapGrade;

    @ApiModelProperty("地图名称")
    private String mapName;

    @ApiModelProperty("地图图片名称")
    private String imgName;

    @ApiModelProperty("宠物出现比例")
    private BigDecimal petRatio;

    /**
     * 装备出现比例
     */
    @ApiModelProperty("装备出现比例")
    private BigDecimal equipRatio;

    /**
     * 珍宝出现比例
     */
    @ApiModelProperty("珍宝出现比例")
    private BigDecimal gemRatio;

    /**
     * 饲料出现比例
     */
    @ApiModelProperty("饲料出现比例")
    private BigDecimal forageRatio;

    /**
     *
     */
    @ApiModelProperty("野生出现比例")
    private BigDecimal wildRatio;

    /**
     * 宠物速度、心情、耐力、幸运总值
     */
    @JsonIgnore
    @ApiModelProperty("宠物速度、心情、耐力、幸运总值")
    private Integer attributeNum;

    @ApiModelProperty("消耗体力")
    private Integer useBrawn;

    @ApiModelProperty("消耗AGS")
    private Integer useAgs;

    @JsonIgnore
    @ApiModelProperty("地图解锁所需捕捉装备等级")
    private Integer lockLevel;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否已开启地图")
    private Boolean isOpen;

    @ApiModelProperty("地图宠物产出概率")
    private List<Map> petRatioMap;

    @ApiModelProperty("装备产出概率")
    private List<Map> equipRatioMap;

    @ApiModelProperty("地图饲料产出概率")
    private List<Map> forageRatioMap;

    @ApiModelProperty("地图珍宝产出概率")
    private List<Map> gemRatioMap;

    @ApiModelProperty("地图野生产出概率")
    private List<Map> wildRatioMap;

    @ApiModelProperty("捕捉道具")
    private List<Map> catchPropMap;

    public Boolean getIsOpen() {

        if(isOpen == null) return false;
        return isOpen;
    }
}
