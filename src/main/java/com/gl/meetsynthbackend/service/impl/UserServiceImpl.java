package com.gl.meetsynthbackend.service.impl;

import com.gl.meetsynthbackend.DTO.UserLoginResponse;
import com.gl.meetsynthbackend.DTO.UserRegisterRequest;
import com.gl.meetsynthbackend.exception.BusinessException;
import com.gl.meetsynthbackend.mapper.UserMapper;
import com.gl.meetsynthbackend.pojo.User;
import com.gl.meetsynthbackend.DTO.UserRegisterResponse;
import com.gl.meetsynthbackend.service.UserService;
import com.gl.meetsynthbackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        // 校验用户名唯一性
        if (userMapper.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 校验邮箱唯一性
        if (userMapper.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已注册");
        }

        // 创建用户对象
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.setAvatarUrl(generateDefaultAvatar());

        // 保存到数据库
        if (userMapper.insert(user) != 1) {
            throw new BusinessException("用户注册失败");
        }

        return convertToResponse(user);
    }

    @Override
    public UserLoginResponse login(String email, String password) {
        User user = userMapper.selectByEmailAndPassword(email, password);
        if (user == null) {
            throw new BusinessException("登录失败，邮箱或密码错误");
        }
        //生成token
        Map<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("username", user.getUsername());
        String token = JwtUtils.generateToken(map);
        //封装response
        UserLoginResponse response = new UserLoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setToken(token);
        return response;
    }

    // 从这里开始是私有方法
    private String generateDefaultAvatar() {
        // 实现默认头像生成逻辑
        return "https://example.com/avatar/default.png";
    }

    private UserRegisterResponse convertToResponse(User user) {
        UserRegisterResponse response = new UserRegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setAvatarUrl(user.getAvatarUrl());
        return response;
    }
}
