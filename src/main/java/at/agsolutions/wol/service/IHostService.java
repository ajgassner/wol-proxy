package at.agsolutions.wol.service;

import at.agsolutions.wol.domain.Host;

import java.util.Collection;

public interface IHostService {
	Collection<Host> findAll();
}
