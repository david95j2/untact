package com.sbs.untact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	// CORS 허용
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	
	
	// beforeActionInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("beforeActionInterceptor")
	HandlerInterceptor beforeActionInterceptor;
	
	// needAdminInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needAdminInterceptor")
	HandlerInterceptor needAdminInterceptor;
	
	// needLoginInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needLoginInterceptor")
	HandlerInterceptor needLoginInterceptor;

	// needLogoutInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needLogoutInterceptor")
	HandlerInterceptor needLogoutInterceptor;

	// 이 함수는 인터셉터를 적용하는 역할을 합니다.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// beforeActionInterceptor 인터셉터가 모든 액션 실행전에 실행되도록 처리
		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**");

		// admin 필요
		registry.addInterceptor(needAdminInterceptor)
			.addPathPatterns("/adm/**")
			.excludePathPatterns("/adm/member/login")
			.excludePathPatterns("/adm/member/doLogin");				
		
		// 로그인 필요
		registry.addInterceptor(needLoginInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/")
			.excludePathPatterns("/adm/**")
			.excludePathPatterns("/resource/**")
			.excludePathPatterns("/usr/home/main")
			.excludePathPatterns("/usr/member/authKey")
			.excludePathPatterns("/usr/member/login")
			.excludePathPatterns("/usr/member/doLogin")
			.excludePathPatterns("/usr/member/join")
			.excludePathPatterns("/usr/member/doJoin")
			.excludePathPatterns("/usr/article/list")
			.excludePathPatterns("/usr/article/detail")
			.excludePathPatterns("/usr/reply/list")
			.excludePathPatterns("/usr/member/findLoginId")
			.excludePathPatterns("/usr/member/doFindLoginId")
			.excludePathPatterns("/usr/member/findLoginInPw")
			.excludePathPatterns("/usr/member/doFindLoginPw")
			.excludePathPatterns("/usr/file/test*")
			.excludePathPatterns("/usr/file/doTest*")
			.excludePathPatterns("/test/**")
			.excludePathPatterns("/error");

		// 로그아웃 필요
		registry.addInterceptor(needLogoutInterceptor)
			.addPathPatterns("/adm/member/login")
			.addPathPatterns("/adm/member/doLogin")
			.addPathPatterns("/usr/member/login")
			.addPathPatterns("/usr/member/doLogin")
			.addPathPatterns("/usr/member/join")
			.addPathPatterns("/usr/member/doJoin");
	}
}
