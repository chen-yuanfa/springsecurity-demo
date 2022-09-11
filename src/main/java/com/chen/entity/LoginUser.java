package com.chen.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private User user;

    private List<String> roles;

    public LoginUser(User user) {
        this.user = user;
    }

    public LoginUser(User user, List<String> roles) {
        this.user = user;
        this.roles = roles;
    }
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    //账户是否过期,过期无法验证
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    //指定用户是否被锁定或者解锁,锁定的用户无法进行身份验证
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    //指示是否已过期的用户的凭据(密码),过期的凭据防止认证
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    //是否被禁用,禁用的用户不能身份验证
    public boolean isEnabled() {
        return true;
    }
}
