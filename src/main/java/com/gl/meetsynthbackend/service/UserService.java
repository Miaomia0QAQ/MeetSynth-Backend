package com.gl.meetsynthbackend.service;

import com.gl.meetsynthbackend.DTO.UserLoginResponse;
import com.gl.meetsynthbackend.DTO.UserRegisterRequest;
import com.gl.meetsynthbackend.DTO.UserRegisterResponse;

public interface UserService {

    UserRegisterResponse register(UserRegisterRequest request);

    UserLoginResponse login(String email, String password);
}
