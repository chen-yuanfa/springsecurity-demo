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

    // ?????????????????????????????????
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/user/login"
        );
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                //??????????????????   ???????????? 123
//                .withUser("admin").password("$2a$10$KmLAmT.TYixbnlUwPpLNEO0VaCd2Oo/rYC1ETC5Idzg/J.29r4zdi").roles("ADMIN");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//             http
//                .cors()
//                .disable()
//                .authorizeRequests().antMatchers("/hello").hasRole("ADMIN"); //???????????????????????????
//        //?????????????????????????????????
//        http.formLogin()
//                .successHandler(successHandler) // ???????????????????????????
//                .failureHandler(failureHandler);//???????????????????????????
//
//        http.logout()
//                .logoutSuccessHandler(logoutSuccessHandler);//???????????????????????????

            http
                //??????csrf
                .csrf().disable()
                //?????????Session??????SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // ?????????????????? ??????????????????
//                .antMatchers("/user/login").anonymous()
//        .antMatchers("/hello").anonymous()
                // ???????????????????????????????????????????????????
                .anyRequest()
                .authenticated()
                    // ??????????????????
                    .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                        @Override
                        public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                            object.setAccessDecisionManager(myAccessDecisionManager);
                            object.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
                            return object;
                        }
                    })
                    .and()
                    // ????????????
                    .headers()
                    .cacheControl();

        //???????????????
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //?????????????????????
        http.exceptionHandling()
                //???????????????????????????
                .authenticationEntryPoint(authenticationEntryPoint)
                //??????
                .accessDeniedHandler(accessDeniedHandler);

        //????????????
        http.cors();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //??????BCryptPasswordEncoder????????????
    @Bean
    public PasswordEncoder passwordEncoder(){
        //????????????????????????????????????
        return new BCryptPasswordEncoder();
    }
}
