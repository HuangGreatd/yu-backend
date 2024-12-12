package com.yupi.yupao.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * team
 *
 * @TableName team
 */
@TableName(value = "team")
@Data
public class Team implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 码头
     */
    @TableField(value = "teamWharf")
    private String teamWharf;

    /**
     * 描述，用户需要寻找什么样式的竹筏
     */
    @TableField(value = "teamDesc")
    private String teamDesc;

    /**
     * 用户简介
     */
    @TableField(value = "userProfile")
    private String userProfile;

    /**
     * 过期时间
     */
    @TableField(value = "expireTime")
    private Date expireTime;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(value = "userId")
    private Long userId;

    @TableField(value = "weight")
    private Double weight;

    @TableField(value = "phone")
    private String phone;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}