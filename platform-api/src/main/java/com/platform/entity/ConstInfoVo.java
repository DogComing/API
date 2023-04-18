package com.platform.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConstInfoVo implements Serializable {

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
     * 属性描述
     */
    private String remark;
}
