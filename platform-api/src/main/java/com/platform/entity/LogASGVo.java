package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户ASG记录实体类
 */
@Data
@ApiModel("用户ASG记录实体类")
public class LogASGVo implements Serializable {

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
     * 交易数量
     */
    @ApiModelProperty("交易数量")
    private BigDecimal num;

    /**
     * 记录类型【1：充值、2：提现、3：捕捉消耗、4：参赛消耗 5：签到获取 6：参赛获取 7：赞助获取 8：看广告获得 9：邮件获得】
     */
    @ApiModelProperty("记录类型【1：充值、2：提现、3：捕捉消耗、4：参赛消耗 5：签到获取 6：参赛获取 7：赞助获取 8：看广告获得 9：邮件获得】")
    private Integer logType;

    /**
     * ASG类型【1：消耗 2：获得】
     */
    @ApiModelProperty("ASG类型【1：消耗 2：获得】")
    private Integer asgType;

    /**
     * 记录类型文字描述
     */
    @ApiModelProperty("记录类型文字描述")
    private String logTypeTxt;

    /**
     * ASG类型文字描述
     */
    @ApiModelProperty("ASG类型文字描述")
    private String agsTypeTxt;

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
