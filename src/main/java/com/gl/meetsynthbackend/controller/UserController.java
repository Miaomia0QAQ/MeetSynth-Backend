package com.gl.meetsynthbackend.controller;

import com.gl.meetsynthbackend.DTO.UserLoginResponse;
import com.gl.meetsynthbackend.DTO.UserRegisterRequest;
import com.gl.meetsynthbackend.pojo.Result;
import com.gl.meetsynthbackend.DTO.UserRegisterResponse;
import com.gl.meetsynthbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(UserRegisterRequest request) {
        UserRegisterResponse response = userService.register(request);
        return Result.success(response);
    }

    @PostMapping("login")
    public Result login(@RequestParam() String email, @RequestParam() String password) {
        UserLoginResponse response = userService.login(email, password);
        return Result.success(response);
    }

}