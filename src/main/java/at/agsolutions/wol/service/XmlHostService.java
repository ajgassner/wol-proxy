package at.agsolutions.wol.service;

import at.agsolutions.wol.WolUtil;
import at.agsolutions.wol.domain.Host;
import at.agsolutions.wol.domain.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class XmlHostService implements IHostService {

	@Autowired
	private Unmarshaller unmarshaller;

	@Value("${hosts.file}")
	private String hostsFile;

	private Collection<Host> hosts = new ArrayList<>();

	@PostConstruct
	private void init() {
		WolUtil.unmarshalXml(unmarshaller, hostsFile, HostHolder.class).ifPresent(v -> hosts = v.getHosts());
	}

	@Override
	public Collection<Host> findAll() {
		return hosts;
	}
}
