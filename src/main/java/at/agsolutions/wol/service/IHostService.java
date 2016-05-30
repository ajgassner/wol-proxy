package at.agsolutions.wol.service;

import at.agsolutions.wol.domain.Host;

import java.io.IOException;
import java.util.Collection;

public interface IHostService {
	Collection<Host> findAll();

	void wakeOnLan(Host host) throws IOException;
}
