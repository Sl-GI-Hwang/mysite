package kr.co.itcen.config.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import kr.co.itcen.mysite.security.AuthInterceptor;
import kr.co.itcen.mysite.security.AuthUserHandlerMethodArgumentResolver;
import kr.co.itcen.mysite.security.LoginIntercepter;
import kr.co.itcen.mysite.security.LogoutIntercepter;

public class SecurityConfig extends WebMvcConfigurerAdapter {
	// Argument Resolver
		@Bean
		public AuthUserHandlerMethodArgumentResolver authUserHandlerMethodArgumentResolver() {
			return new AuthUserHandlerMethodArgumentResolver();
		}
		

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			// TODO Auto-generated method stub
			super.addArgumentResolvers(argumentResolvers);
		}


		// Interceptors
		@Bean
		public LoginIntercepter loginInterceptor() {
			return new LoginIntercepter();
		}
		
		@Bean
		public LogoutIntercepter logoutInterceptor() {
			return new LogoutIntercepter();
		}
		
		@Bean
		public AuthInterceptor authInterceptor() {
			return new AuthInterceptor();
		}
		
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry
				.addInterceptor(loginInterceptor())
				.addPathPatterns("/user/auth");
			
			registry
				.addInterceptor(logoutInterceptor())
				.addPathPatterns("/user/logout");

			registry
				.addInterceptor(authInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/user/auth")
				.excludePathPatterns("/user/logout")
				.excludePathPatterns("/assets/**");
			
		}

}
