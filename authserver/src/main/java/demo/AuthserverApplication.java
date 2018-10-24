package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableAuthorizationServer
public class AuthserverApplication  implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
//		registry.addViewController("/oauth/authorize").setViewName("authorize");
		registry.addViewController("/oauth/confirm_access").setViewName("confirm_access");
		registry.addViewController("/").setViewName("index");
	}


}
