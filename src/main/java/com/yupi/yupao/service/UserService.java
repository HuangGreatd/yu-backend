package com.yupi.yupao.service;

import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupao.model.dto.WeChatCodeDTO;
import com.yupi.yupao.model.vo.UserVO;
import com.yupi.yupao.model.vo.UsersLoginVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 73782
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-12-05 20:19:14
*/
public interface UserService extends IService<User> {

    UserVO authWechat(WeChatCodeDTO code,HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    int updateUser(User user, User loginUser);
}
