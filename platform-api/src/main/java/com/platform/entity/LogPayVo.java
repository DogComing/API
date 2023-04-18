package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户支付记录实体类
 */
@Data
@ApiModel("用户支付记录实体类")
public class LogPayVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键id
     */
    @ApiModelProperty("自增主键id")
    private Integer id;

    /**
     * 支付用户ID
     */
    @ApiModelProperty("支付用户ID")
    private Long userId;

    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal money;

    /**
     * 支付类型 【1：购买30天签到、2：购买精力、3：购买捕捉装备】
     */
    @ApiModelProperty("支付类型")
    private Integer type;

    /**
     * 货币类型【1：ASG 2：USDT】
     */
    @ApiModelProperty("货币类型")
    private Integer currencyType;

    /**
     * 支付文字描述
     */
    @ApiModelProperty("支付文字描述")
    private String typeTxt;

    /**
     * 货币文字描述
     */
    @ApiModelProperty("货币文字描述")
    private String currencyTxt;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remarks;

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
