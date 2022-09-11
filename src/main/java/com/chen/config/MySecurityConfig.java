package com.chen.config;

import com.chen.filter.AuthenticationTokenFilter;
import com.chen.filter.MyFilterInvocationSecurityMetadataSource;
import com.chen.handler.AuthenticationEntryPointImpl;
import com.chen.handler.MyAccessDecisionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private AuthenticationTokenFilter authenticationTokenFilter;

    @Autowired
    private MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;

    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;

    // 放行路径（不走拦截链）
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/user/login"
        );
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                //设置账号密码   明文密码 123
//                .withUser("admin").password("$2a$10$KmLAmT.TYixbnlUwPpLNEO0VaCd2Oo/rYC1ETC5Idzg/J.29r4zdi").roles("ADMIN");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//             http
//                .cors()
//                .disable()
//                .authorizeRequests().antMatchers("/hello").hasRole("ADMIN"); //需要的角色才能访问
//        //需要使用原生的登录界面
//        http.formLogin()
//                .successHandler(successHandler) // 配置认证成功处理器
//                .failureHandler(failureHandler);//配置认证失败处理器
//
//        http.logout()
//                .logoutSuccessHandler(logoutSuccessHandler);//配置注销成功处理器

            http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
//                .antMatchers("/user/login").anonymous()
//        .antMatchers("/hello").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated()
                    // 动态权限配置
                    .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                        @Override
                        public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                            object.setAccessDecisionManager(myAccessDecisionManager);
                            object.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
                            return object;
                        }
                    })
                    .and()
                    // 禁用缓存
                    .headers()
                    .cacheControl();

        //添加过滤器
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
        http.exceptionHandling()
                //配置认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                //授权
                .accessDeniedHandler(accessDeniedHandler);

        //允许跨域
        http.cors();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //创建BCryptPasswordEncoder注入容器
    @Bean
    public PasswordEncoder passwordEncoder(){
        //用户登录密码进行加密验证
        return new BCryptPasswordEncoder();
    }
}
