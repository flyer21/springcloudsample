package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
//	@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
class LoginConfig extends WebSecurityConfigurerAdapter {

//		@Bean
//		public UserDetailsService userDetailsService() {
//			InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//			manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
//			return manager;
//		}

//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/css/**", "/index").permitAll()
//                .antMatchers("/user/**").hasRole("USER")
//                .and()
//                .formLogin()
//                .loginPage("/login").failureUrl("/login-error");
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER") .and() .withUser("admin").password("password").roles("USER", "ADMIN");;
//    }
//
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception  {

        auth
                // enable in memory based authentication with a user named
                // "user" and "admin"
                .inMemoryAuthentication().withUser("user").password("{bcrypt}$2a$10$/jwXQ/jvvxtwqVPb9aMOAOl3JK4KBQDv4C266cenVfdr5nq9BwRSi").roles("USER").and()
                .withUser("admin").password("{bcrypt}$2a$10$/jwXQ/jvvxtwqVPb9aMOAOl3JK4KBQDv4C266cenVfdr5nq9BwRSi").roles("USER", "ADMIN");
    }

    // Expose the UserDetailsService as a Bean
    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }


//		@Autowired
//		private AuthenticationManager authenticationManager;


//		@Bean
//		@Override
//		public AuthenticationManager authenticationManagerBean() throws Exception {
//			return super.authenticationManagerBean();
//		}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").permitAll()
                .and().authorizeRequests()
                .anyRequest().authenticated();
    }

//		@Override
//		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//			auth.parentAuthenticationManager(authenticationManager);
//		}
}
