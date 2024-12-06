package com.yupi.yupao.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName UsersLoginVO
 * @Description:
 * @Author: 橘子皮
 * @CreateDate: 2024/12/6 21:06
 */
@Builder
@Data
public class UsersLoginVO {
    /** 用户id */
    private Long id;
    /** token */
    private String token;
    /** 用户名称 */
    private String username;
    /** 头像路径 */
    private String avatarUrl;
}
