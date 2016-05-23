package at.agsolutions.wol.service;

import at.agsolutions.wol.domain.User;

import java.util.Collection;

public interface IUserService {
	User checkCredentials(String userName, String password) throws UserNotFoundException, WrongPasswordException;

	Collection<User> findAll();

	class UserNotFoundException extends Exception {
		public UserNotFoundException(final String message) {
			super(message);
		}
	}

	class WrongPasswordException extends Exception {
		public WrongPasswordException(final String message) {
			super(message);
		}
	}
}
