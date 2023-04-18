package com.platform.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: platform
 * @description: 平台配置实体类
 * @author: Yuan
 * @create: 2020-09-08 12:23
 **/
@Data
public class ConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 键【属性名字】
     */
    private String key;

    /**
     * 值【属性值】
     */
    private String value;

    /**
     * 属性状态
     */
    private Integer status;

    /**
     * 属性描述
     */
    private String remark;
}
