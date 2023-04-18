package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 首次登录对象
 */
@Data
@ApiModel("登录实体类")
public class LoginInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录凭证
     */
    @ApiModelProperty("登录凭证")
    private String logonCredentials;

    /**
     * 用户userId
     */
    @ApiModelProperty("用户userId")
    private Integer userId;

    /**
     * 基地openId
     */
    @ApiModelProperty("基地openId")
    private String openId;
}
