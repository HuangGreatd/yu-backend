package com.yupi.yupao.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.weixin.Users;
import com.yupi.yupao.model.dto.WeChatCodeDTO;
import com.yupi.yupao.model.enums.UserRoleEnum;
import com.yupi.yupao.model.vo.UserVO;
import com.yupi.yupao.model.vo.UsersLoginVO;
import com.yupi.yupao.service.UserService;
import com.yupi.yupao.mapper.UserMapper;
import com.yupi.yupao.utils.HttpClientUtil;
import com.yupi.yupao.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.yupi.yupao.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 73782
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-12-05 20:19:14
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Value("${weixin.open.appId}")
    private String appId;

    @Value("${weixin.open.secret}")
    private String secret;
    /**
     * 微信 auth.code2Session 接口
     */
    private static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public UserVO authWechat(WeChatCodeDTO code, HttpServletRequest request) {
        // 获取 openid
        String openId = getOpenId(code.getCode());
        // 查询用户是否已存在
        synchronized (openId.intern()) {
            Long userId = userMapper.selectByOpenId(openId);
            // 生成JWT
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("openId", openId);
            String token = JwtUtil.genToken(claims);
            if (userId == null) {
                // 如果用户不存在，则创建用户
                User user = new User();
                Users users = Users.builder().openId(openId).createTime(LocalDateTime.now()).build();
                user.setMpOpenId(openId);
                user.setUserAvatar(code.getAvatar());
                user.setUserName(code.getNickname());
                user.setUserAccount(code.getNickname());
                System.out.println("user = " + user);
                userMapper.insert(user);
                log.info("users:" + users.toString());
            }
            // 用户已存在,返回用户信息
            User user = userMapper.selectById(userId);
            // 记录用户的登录态
            request.getSession().setAttribute(USER_LOGIN_STATE, user);
            UserVO userVO = new UserVO();
            userVO.setId(userId);
            userVO.setWxOpenId(token);
            userVO.setUserAccount(user.getUserAccount());
            userVO.setUsername(user.getUserName());
            userVO.setAvatarUrl(user.getUserAvatar());
            return userVO;
        }

    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return currentUser;
    }


    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    @Override
    public int updateUser(User user, User loginUser) {
        long userId = user.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 补充校验，如果用户没有传任何要更新的值，就直接报错，不用执行 update 语句
        // 如果是管理员，允许更新任意用户
        // 如果不是管理员，只允许更新当前（自己的）信息
        if (!isAdmin(loginUser) && userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 通过微信登录code获取openId
     *
     * @param code 微信登录code
     * @return openId
     */
    public String getOpenId(String code) {
        // 添加参数
        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("secret", secret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        // 使用封装好的HttpClientUtil工具类发送请求
        String json = HttpClientUtil.doGet(WECHAT_LOGIN_URL, map);
        // 将JSON字符串转换为JSONObject对象
        JSONObject jsonObject = JSON.parseObject(json);
        // 从JSONObject中获取openid并返回
        return jsonObject.getString("openid");
    }
}




