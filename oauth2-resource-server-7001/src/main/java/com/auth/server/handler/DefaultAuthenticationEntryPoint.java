package com.auth.server.handler;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 认证失败处理器
 *
 * @author youzhengjie
 * @date 2023/04/25 00:27:45
 */
@Slf4j
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {

        if (response.isCommitted()){
            return;
        }
        Throwable throwable = authException.fillInStackTrace();

        HashMap<String, String> map = new HashMap<>(2);
        map.put("uri", request.getRequestURI());
        map.put("msg", "认证失败");

        if (throwable instanceof BadCredentialsException){
            map.put("msg", "错误的客户端信息");
        }else {
            Throwable cause = authException.getCause();
            if (cause instanceof JwtValidationException) {
                map.put("msg", "jwtToken已过期,无效的token信息");
            } else if (cause instanceof BadJwtException){
                map.put("msg", "JWT签名异常,无效的token信息");
            } else if (cause instanceof AccountExpiredException){
                map.put("msg", "账户已过期");
            } else if (cause instanceof LockedException){
                map.put("msg", "账户已被锁定");
            } else if (throwable instanceof InsufficientAuthenticationException) {
                String message = throwable.getMessage();
                if (message.contains("Invalid token does not contain resource id")){
                    map.put("msg", "未经授权的资源服务器");
                }else if (message.contains("Full authentication is required to access this resource")){
                    map.put("msg", "缺少验证信息");
                }
            }else {
                map.put("msg", "验证异常");
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(map);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resBody);
        printWriter.flush();
        printWriter.close();
    }
}
