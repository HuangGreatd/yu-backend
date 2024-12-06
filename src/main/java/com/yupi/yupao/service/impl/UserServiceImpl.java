package com.yupi.yupao.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.weixin.Users;
import com.yupi.yupao.model.dto.WeChatCodeDTO;
import com.yupi.yupao.model.vo.UsersLoginVO;
import com.yupi.yupao.service.UserService;
import com.yupi.yupao.mapper.UserMapper;
import com.yupi.yupao.utils.HttpClientUtil;
import com.yupi.yupao.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 73782
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-12-05 20:19:14
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 微信 auth.code2Session 接口
     */
    private final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Resource
    private UserMapper userMapper;

    @Override
    public UsersLoginVO authWechat(WeChatCodeDTO code) {
        // 获取 openid
        String openId = getOpenId(code.getCode());

        // 查询用户是否已存在
        Long userId = userMapper.selectByOpenId(openId);

        // 生成JWT
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("openId", openId);
        String token = JwtUtil.genToken(claims);

        if (userId == null) {
            // 如果用户不存在，则创建用户
            User user = new User();
            Users users = Users.builder().openId(openId).createTime(LocalDateTime.now()).build();
            Long id = users.getId();
            String username = users.getUsername();
            String avatarUrl = users.getAvatarUrl();

            user.setMpOpenId(openId);
            user.setUserAvatar(avatarUrl);
            user.setUserName("默认用户");
            user.setUserAccount("默认");
            user.setUserPassword("123456");

            System.out.println("user = " + user);
            userMapper.insert(user);
            log.info("users:" + users.toString());
            return UsersLoginVO.builder().id(users.getId()).token(token).build();
        }
        log.info("已存在");
        // 用户已存在
        return UsersLoginVO.builder().id(userId).token(token).build();


    }

    /**
     * 通过微信登录code获取openId
     *
     * @param code 微信登录code
     * @return openId
     */
    public  String getOpenId(String code) {
        // 添加参数
        Map<String, String> map = new HashMap<>();
        map.put("appid", "wx930cf99a815ecd10");
        map.put("secret", "5dab66d76e82eb043bc690c996b85ffd");
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




