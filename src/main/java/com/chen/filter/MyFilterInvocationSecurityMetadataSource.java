package com.chen.filter;

import com.alibaba.fastjson.JSON;
import com.chen.entity.LoginUser;
import com.chen.entity.Menu;
import com.chen.entity.Role;
import com.chen.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** 权限控制
 //用于设置判断当前用户是否可以访问被保护资源的逻辑
 */
@Component
public class MyFilterInvocationSecurityMetadataSource  implements FilterInvocationSecurityMetadataSource {

    @Resource
    private MenuMapper menuMapper;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private StringRedisTemplate   redisTemplate;

    @Override
    /*
     * @param 请求该保护资源的用户对象
     * @param 被调用的保护资源
     * @param 有权限调用该资源的集合
     */
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 获取springsecutity需要认证的请求 url(放行的路径不会经过这里)
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        // 获取每个菜单拥有的所有角色
        List<Menu> menus = null;
        String menuListJson = redisTemplate.opsForValue().get("meun");
        if(menuListJson!=null&&menuListJson.length()>0){
            menus = JSON.parseArray(menuListJson,Menu.class);
            redisTemplate.expire("meun",30,TimeUnit.MINUTES);
        }else{
            menus = menuMapper.getAllMenus();
            redisTemplate.opsForValue().set("meun",JSON.toJSONString(menus),30, TimeUnit.MINUTES);
        }
        // 判断请求 url 与菜单路径进行匹配，匹配成功，获取这个菜单能够访问的所有角色
        for (Menu menu : menus) {
            if (antPathMatcher.match(menu.getUrl(),requestUrl)) {
                if(!menu.isRequireAuth()){ //不需要权限认证，支持外部访问，直接放行
                    //返回null则不会进入之后的decide方法
                    return null;
                }
                if(menu.getRoles()!=null&&menu.getRoles().size()>0){
                    //获取该url配置的角色，若没有配置角色，那么登录即可访问
                    String[] str = menu.getRoles().stream().map(Role::getRoleCode).toArray(String[]::new);
                    return SecurityConfig.createList(str);  //返回给AccessDecisionManager，进行匹配
                }
            }
        }
        // 没匹配的 url或者匹配的url没有配置角色 默认登录即可访问 : ROLE_LOGIN 登录即可拥有的角色
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
