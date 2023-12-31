package com.petal.oauth2.client.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * index控制器
 *
 * @author youzhengjie
 * @date 2023/04/27 17:15:52
 */
@Controller
public class IndexController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/")
    public String index(Model model) {
        // 从安全上下文中获取登录信息，返回给model
        Map<String, Object> map = new HashMap<>(2);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        map.put("name", auth.getName());
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.stream().iterator();
        ArrayList<Object> authList = new ArrayList<>();
        while (iterator.hasNext()) {
            authList.add(iterator.next().getAuthority());
        }

        map.put("authorities", authList);
        model.addAttribute("user", JSON.toJSONString(map));
        return "index";
    }

    /**
     * 退出登录
     * <p>
     * 由于Spring Security默认退出路径是/logout,但是访问/logout会自动跳转到自带的退出界面（加载十分慢）
     * 所以我们就自定义了一个退出登录为/user/logout。
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/user/logout")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) {
        // ========== 清理客户端 ===========
        // 清理客户端session
        request.getSession().invalidate();
        // 清理客户端安全上下文
        SecurityContextHolder.clearContext();
        // ========== 清理认证中心 ===========
        try {
            // 跳转至认证中心退出页面
            response.sendRedirect("http:///petal.oauth2.com:9090/logout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
