package at.agsolutions.wol.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@Data
@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserHolder {

	@XmlElement(name="user")
	private Collection<User> users;
}