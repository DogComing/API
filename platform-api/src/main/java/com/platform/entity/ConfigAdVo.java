package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: platform
 * @description: 广告配置表实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("广告配置表实体类")
public class ConfigAdVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("广告ID")
    private Integer id;

    @ApiModelProperty("广告地址")
    private String adUrl;

    @ApiModelProperty("点击广告跳转地址")
    private String jumpUrl;

    @ApiModelProperty("奖励名称")
    private String awardName;

    @ApiModelProperty("奖励图片名称")
    private String imgName;

    @ApiModelProperty("奖励类型【1:代币/AGS  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备】")
    private Integer awardType;

    @ApiModelProperty("奖励数量")
    private Integer awardNum;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 今日是否已观看
     */
    private Boolean isLook;

    public Boolean getIsLook() {

        if(isLook == null) return false;
        return isLook;
    }
}
