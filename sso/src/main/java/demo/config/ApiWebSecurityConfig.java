package demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
    @Order(1)
    public   class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().and()
                    .antMatcher("/actuator/**")
                    .authorizeRequests()
                        .anyRequest().hasAnyRole("ADMIN", "API")
                        .and()
                    .httpBasic().realmName("Spring");
        }

    }