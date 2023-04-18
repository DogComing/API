package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户基地返利实体类
 */
@Data
@ApiModel("用户基地返利实体类")
public class LogRakeBackVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键id
     */
    @ApiModelProperty("自增主键id")
    private Integer id;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private BigDecimal num;

    /**
     * 币种【asg,usdt】，默认asg
     */
    @ApiModelProperty("币种【asg,usdt】，默认asg")
    private String coinCode;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderSn;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remarks;

    /**
     * 钱包地址
     */
    @ApiModelProperty("钱包地址")
    private String address;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
