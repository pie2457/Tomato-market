package pie.tomato.tomatomarket.presentation.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import pie.tomato.tomatomarket.exception.ErrorCode;
import pie.tomato.tomatomarket.exception.UnAuthorizedException;
import pie.tomato.tomatomarket.infrastructure.config.jwt.JwtProvider;

public class JwtFilter extends OncePerRequestFilter {

	private static final String BEARER = "bearer";

	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final List<String> excludeUrlPatterns = List.of("/api/auth/**");

	private final JwtProvider jwtProvider;

	public JwtFilter(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return excludeUrlPatterns.stream()
			.anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = extractJwt(request)
			.orElseThrow(() -> new UnAuthorizedException(ErrorCode.INVALID_TOKEN));
		jwtProvider.validateToken(token);

		filterChain.doFilter(request, response);
	}

	private Optional<String> extractJwt(HttpServletRequest request) {
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (!StringUtils.hasText(header) || !header.toLowerCase().startsWith(BEARER)) {
			return Optional.empty();
		}
		return Optional.of(header.split(" ")[1]);
	}
}
