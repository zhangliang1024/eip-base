package com.eip.ability.gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Levin
 * @since 2019-04-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDetails implements UserDetails {

    private static final long serialVersionUID = 666236878598344789L;

    private Long userId;
    private String email;
    private Integer sex;
    private String mobile;
    private Long tenantId;
    private String tenantCode;
    private String companyName;
    private String nickName;
    private String realName;
    private String username;
    private String password;
    private String avatar;
    private Boolean enabled;
    private String description;
    private Collection<String> permissions = new ArrayList<>();
    private Collection<String> roles = new ArrayList<>();
    private Collection<SimpleGrantedAuthority> authorities;



    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleGrantedAuthority implements GrantedAuthority {

        private String authority;

        @Override
        public String getAuthority() {
            return this.authority;
        }
    }

}
