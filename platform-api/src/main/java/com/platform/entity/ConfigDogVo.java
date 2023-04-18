package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 狗狗配置表实体类
 */
@Data
@ApiModel("狗狗配置表实体类")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigDogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Integer id;

    @ApiModelProperty("狗狗品质")
    private Integer dogGrade;

    @ApiModelProperty("狗狗名称")
    private String dogName;

    @ApiModelProperty("狗狗描述")
    private String dogDesc;

    @ApiModelProperty("狗狗图片")
    private String imgName;

    @ApiModelProperty("狗狗动画")
    private String animationName;

    @ApiModelProperty("狗狗品种")
    private String dogBreed;

    @ApiModelProperty("狗狗战力值")
    private BigDecimal initialFightingNum;

    @ApiModelProperty("狗狗最大成长次数")
    private Integer growUpNum;

    @ApiModelProperty("狗狗天赋系数")
    private BigDecimal inbornNum;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
