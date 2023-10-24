package pie.tomato.tomatomarket.presentation.support;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AuthenticationContext {

	private Principal principal;

	public Optional<Principal> getPrincipal() {
		return Optional.ofNullable(principal);
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}
}
