package com.behavox.task.scriptengine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
            authenticationMgr.jdbcAuthentication().dataSource(dataSource)
                    .withDefaultSchema()
                    .withUser("user").password(passwordEncoder().encode("pass")).authorities("ROLE_USER")
                    .and()
                    .withUser("admin").password(passwordEncoder().encode("pass")).authorities("ROLE_USER","ROLE_ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/api/v1/engine/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/swagger-ui.html").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/h2-console/**").access("hasRole('ROLE_ADMIN')")
                    .and()
                    .formLogin()
                    .defaultSuccessUrl("/swagger-ui.html")
                    .usernameParameter("username").passwordParameter("password")
                    .and()
                    .logout().logoutSuccessUrl("/login");

            //NEED for DEMO pupouse
            http.csrf()
                    .ignoringAntMatchers("/h2-console/**");
            http.headers()
                    .frameOptions()
                    .sameOrigin();
            //NEED for DEMO pupouse
            http.csrf()
                    .ignoringAntMatchers("/api/v1/engine/**");
            http.headers()
                    .frameOptions()
                    .sameOrigin();

        }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
