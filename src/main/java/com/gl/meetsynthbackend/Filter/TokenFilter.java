package com.gl.meetsynthbackend.Filter;

import com.gl.meetsynthbackend.utils.CurrentHolder;
import com.gl.meetsynthbackend.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "/*", asyncSupported = true)
public class TokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 无须校验
        String requestURI = request.getRequestURI();
        if (!requestURI.contains("/user") || requestURI.contains("/register")) {
            String token = request.getHeader("Authorization");
            if (token != null && !token.isEmpty()) {
                try {
                    Claims claims = JwtUtils.parseToken(token);
                    String uid = claims.get("uid").toString();
                    CurrentHolder.setCurrentId(uid); //存入
                } catch (Exception ignored) {
                }
            }
            filterChain.doFilter(request, response);
            return;
        }
        //无令牌
        String token = request.getHeader("Token");
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            //校验成功
            Claims claims = JwtUtils.parseToken(token);
            String uid = claims.get("uid").toString();
            CurrentHolder.setCurrentId(uid); //存入
        } catch (Exception e) {
            //校验失败
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //放行
        filterChain.doFilter(request, response);

        CurrentHolder.removeCurrentId();
    }
}
