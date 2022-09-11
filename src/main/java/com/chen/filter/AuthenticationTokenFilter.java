package com.chen.filter;

import com.alibaba.fastjson.JSON;
import com.chen.entity.LoginUser;
import com.chen.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if(token!=null&&token.length()>0){  //token存在
            //解析jwt
            String userId = null;
            try {
                userId = JwtUtil.getSubject(token);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("请求失败,请重新登录！");
            }
            //从redis中获取用户信息
            String redisKey = "login:" + userId;
            String loginUserJson = redisTemplate.opsForValue().get(redisKey);
            if(loginUserJson!=null&&loginUserJson.length()>0){
                //把json转换成对象
                LoginUser loginUser = JSON.parseObject(loginUserJson, LoginUser.class);
                // 参数：用户对象 密码 角色
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                // 重新设置用户对象到 springSecurity上下文中去
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                //重置redis过期时间
                redisTemplate.expire(redisKey,30, TimeUnit.MINUTES);
            }
        }
        //放行
        filterChain.doFilter(request, response);
    }
}