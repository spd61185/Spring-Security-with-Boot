package com.satish;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan
public class MvcConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer{


	@Bean public ViewResolver viewResolver() { 
		InternalResourceViewResolver resolver = new InternalResourceViewResolver(); 
		resolver.setPrefix("/WEB-INF/views/"); 
		resolver.setSuffix(".jsp"); 
		resolver.setViewClass(JstlView.class); 
		return resolver; 
	} 

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	public void configureGloabal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("satish")
		.password("1234")
		.roles("ADMIN");
		auth.inMemoryAuthentication()
		.withUser("vas").password("vas").roles("STOREKEEPER");
		auth.inMemoryAuthentication()
		.withUser("sri").password("sri").roles("CUSTOMER");
		auth.inMemoryAuthentication()
		.withUser("sd").password("sd").roles("CUSTOMER");
		auth.inMemoryAuthentication()
		.withUser("super").password("{nope}super").roles("CUSTOMER","ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/deleteBook**").access("hasRole('ROLE_ADMIN')")
		.antMatchers("/addBook**").access("hasAnyRole('ROLE_ADMIN','ROLE_STOREKEEPER')")
		.antMatchers("/editBook").access("hasAnyRole('ROLE_CUSTOMER')").and().formLogin()
		.and().exceptionHandling().accessDeniedPage("/WEB-INF/views/invalidAccess.sjp");
	}
}
