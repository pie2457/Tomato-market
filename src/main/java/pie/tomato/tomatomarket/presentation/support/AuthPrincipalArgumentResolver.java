package pie.tomato.tomatomarket.presentation.support;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

	private final AuthenticationContext authenticationContext;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthPrincipal.class)
			&& parameter.getParameterType().equals(Principal.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		return authenticationContext.getPrincipal();
	}
}
