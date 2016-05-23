package at.agsolutions.wol;

import at.agsolutions.wol.domain.User;
import at.agsolutions.wol.service.IUserService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.VaadinSessionScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@VaadinSessionScope
public class AccessControl implements Serializable {

	private static final String SESSION_ATTRIBUTE_IDENTIFIER = "current-user";

	@Autowired
	private transient IUserService userService;

	public User login(final String userName, final String password) throws IUserService.UserNotFoundException, IUserService
			.WrongPasswordException {

		User user = userService.checkCredentials(userName, password);
		updateCurrentUser(user);
		return user;
	}

	public void logout() {
		getSession().close();
	}

	public void updateCurrentUser(User user) {
		getSession().setAttribute(SESSION_ATTRIBUTE_IDENTIFIER, user);
	}

	public User findCurrentUser() {
		return (User) getSession().getAttribute(SESSION_ATTRIBUTE_IDENTIFIER);
	}

	public boolean isCurrentUserAuthenticated() {
		return findCurrentUser() != null;
	}

	private VaadinSession getSession() {
		VaadinSession session = VaadinSession.getCurrent();
		if (session != null) {
			return session;
		}
		throw new IllegalStateException("Unable to locate VaadinSession");
	}
}
