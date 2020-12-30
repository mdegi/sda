package com.md.sda.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable().authorizeRequests()
                .anyRequest().authenticated();
    }

    //Using In memory user authentication which values might be stored in some eternal properties file
    //However other types of authentication can be used such as JDBC and LDAP
    @Bean
    public InitializingBean initializer(UserDetailsManager manager) {
        return () -> {
            UserDetails defaultUser = User.withDefaultPasswordEncoder().username("defaultUser").password("defaultPassword").roles("USER").build();
            manager.createUser(defaultUser);
        };
    }

}
