package at.agsolutions.wol.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Host {

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String mac;

	@XmlAttribute
	private String ip;
}
