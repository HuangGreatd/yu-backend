package com.yupi.yupao.model.domain.weixin;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName Users
 * @Description:
 * @Author: 橘子皮
 * @CreateDate: 2024/12/6 21:05
 */
@Data
@Builder
public class Users {
    /** 用户id */
    private Long id;
    /** 微信用户唯一标识 */
    private String openId;
    /** 用户名称 */
    private String username;
    /** 头像路径 */
    private String avatarUrl;
    /** 创建时间 */
    private LocalDateTime createTime;
}
