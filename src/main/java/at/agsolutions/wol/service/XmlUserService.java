package at.agsolutions.wol.service;

import at.agsolutions.wol.WolUtil;
import at.agsolutions.wol.domain.User;
import at.agsolutions.wol.domain.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
public class XmlUserService implements IUserService {

	@Autowired
	private Unmarshaller unmarshaller;

	@Value("${users.file}")
	private String usersFile;

	private Collection<User> users = new ArrayList<>();

	@PostConstruct
	private void init() {
		WolUtil.unmarshalXml(unmarshaller, usersFile, UserHolder.class).ifPresent(v -> users = v.getUsers());
	}

	@Override
	public User checkCredentials(final String userName, final String password) throws UserNotFoundException, WrongPasswordException {
		User user = users.stream().filter(u -> u.getName().equals(userName)).findFirst().orElseThrow(() -> new
				UserNotFoundException("User" + " with user name '" + userName + "' not found"));

		if (user.getPassword() == null || !user.getPassword().equals(WolUtil.generateSha256(password))) {
			throw new WrongPasswordException("Given password doesn't match");
		}

		return user;
	}

	@Override
	public Collection<User> findAll() {
		return users;
	}
}
