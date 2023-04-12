package com.ricoh.jwdaas.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.hutool.json.JSONUtil;
import com.ricoh.jwdaas.dto.JwtUserDto;
import com.ricoh.jwdaas.utils.JwtUtil;
import com.ricoh.jwdaas.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/** 
  * @Title: MyAuthenticationSuccessHandler.java 
  * @Copyright: Copyright (c) 2023 
  * @Company: RICOH
  * @version 1.0  
  * @author jwChen  
  * @date 2023年3月29日  
  */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${trustdomain}")
    private String trustdomain;
//    @Value("${jwt.tokenHead}")
//    private String tokenHead;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

         JwtUserDto userDetails = (JwtUserDto)authentication.getPrincipal();//拿到登录用户信息
         String jwtToken = jwtUtil.generateToken(userDetails,trustdomain);//生成token
        HttpSession session = httpServletRequest.getSession();
        //删除缓存里的验证码信息
//        session.removeAttribute("captcha");
        Result result = Result.ok().message("登录成功").jwt(jwtToken);
        //修改编码格式
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json");
        //输出结果
//        httpServletResponse.getWriter().write(JSON.toJSONString(result));

        httpServletResponse.getWriter().write(JSONUtil.toJsonStr(result));
    }
}

