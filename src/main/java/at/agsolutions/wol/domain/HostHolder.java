package at.agsolutions.wol.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@Data
@XmlRootElement(name = "hosts")
@XmlAccessorType(XmlAccessType.FIELD)
public class HostHolder {

	@XmlElement(name="host")
	private Collection<Host> hosts;
}