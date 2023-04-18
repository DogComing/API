package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("签到配置表主键Id")
    private Integer id;

    @ApiModelProperty("第几天签到")
    private String day;

    @ApiModelProperty("签到领取内容")
    private String content;

    @ApiModelProperty("捕捉装备图片名称")
    private String imgName;

    @ApiModelProperty("签到奖励数量")
    private Integer awardNum;

    @ApiModelProperty("是否道具/饲料/装备【0:否  1:是】")
    private Integer isAttribute;

    @ApiModelProperty("签到奖励类型【1:代币  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备】")
    private Integer awardType;

    @ApiModelProperty("种类ID【例如：饲料表 一级饲料对应的ID；道具表 某个道具对应的ID】")
    private Integer kindId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否已签到")
    private Boolean isCheck;

    @ApiModelProperty("是否今天")
    private Boolean isToday;

    public Boolean getIsCheck() {

        if(isCheck == null) return false;
        return isCheck;
    }
}
