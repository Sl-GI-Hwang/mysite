package kr.co.itcen.mysite.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import kr.co.itcen.config.web.MVCConfig;
import kr.co.itcen.config.web.SecurityConfig;

@Configuration
@EnableWebMvc
@ComponentScan({"kr.co.itcen.mysite.controller"})
@Import({MVCConfig.class, SecurityConfig.class})
public class WebConfig {
	
}
