package pie.tomato.tomatomarket.infrastructure.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import pie.tomato.tomatomarket.infrastructure.config.jwt.JwtProvider;
import pie.tomato.tomatomarket.presentation.filter.AuthExceptionHandlerFilter;
import pie.tomato.tomatomarket.presentation.filter.JwtFilter;
import pie.tomato.tomatomarket.presentation.support.AuthenticationContext;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

	private final JwtProvider jwtProvider;
	private final AuthenticationContext authenticationContext;

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		FilterRegistrationBean<JwtFilter> jwtFilter = new FilterRegistrationBean<>();
		jwtFilter.setFilter(new JwtFilter(jwtProvider, authenticationContext));
		jwtFilter.addUrlPatterns("/api/*");
		jwtFilter.setOrder(2);
		return jwtFilter;
	}

	@Bean
	public FilterRegistrationBean<AuthExceptionHandlerFilter> authExceptionHandlerFilter() {
		FilterRegistrationBean<AuthExceptionHandlerFilter> authExceptionHandlerFilter = new FilterRegistrationBean<>();
		authExceptionHandlerFilter.setFilter(new AuthExceptionHandlerFilter());
		authExceptionHandlerFilter.addUrlPatterns("/api/*");
		authExceptionHandlerFilter.setOrder(1);
		return authExceptionHandlerFilter;
	}
}
