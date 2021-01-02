package com.md.sda.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
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
        //other variances here may include mvcMatchers and hasAuthority() method
        http.httpBasic();
        http.csrf().disable().authorizeRequests()
                .antMatchers("/v1/services/reload").permitAll()
                .antMatchers("/v1/services/**")
                .authenticated();
    }

    //Using In memory user authentication which values might be stored in some eternal properties file
    //However other types of authentication can be used such as JDBC and LDAP
    //To create a customer Authentication Provider, you can also create a Custom Authentication Provider implementing AuthenticationProvider
    @Bean
    public InitializingBean initializer(UserDetailsManager manager) {
        return () -> {
            UserDetails defaultUser = User.withDefaultPasswordEncoder()
                    .username("defaultUser")
                    .password("defaultPassword")
                    .roles("USER").build();
            manager.createUser(defaultUser);
        };
    }

    //Enable audit events
    @Bean
    public InMemoryAuditEventRepository repository(){
        return new InMemoryAuditEventRepository();
    }

}
