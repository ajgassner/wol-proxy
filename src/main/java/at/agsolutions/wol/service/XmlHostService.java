package at.agsolutions.wol.service;

import at.agsolutions.wol.WolUtil;
import at.agsolutions.wol.domain.Host;
import at.agsolutions.wol.domain.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
public class XmlHostService implements IHostService {

	private static final int PORT = 9;
	private static final int PING_TIMEOUT = 1000;

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

	@Override
	public void wakeOnLan(final Host host) throws IOException {
		byte[] macBytes = getMacBytes(host.getMac());
		byte[] bytes = new byte[6 + 16 * macBytes.length];
		int port = host.getPort() != 0 ? host.getPort() : PORT;

		for (int i = 0; i < 6; i++) {
			bytes[i] = (byte) 0xff;
		}

		for (int i = 6; i < bytes.length; i += macBytes.length) {
			System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
		}

		InetAddress address = InetAddress.getByName(host.getBroadcastIp());
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, host.getPort() != 0 ? host.getPort() : PORT);
		DatagramSocket socket = new DatagramSocket();
		socket.send(packet);
		socket.close();

		log.info("WOL packet sent to {} and port {}", host, port);
	}

	@Override
	public boolean ping(final Host host) throws IOException {
		InetAddress address = InetAddress.getByName(host.getIp());
		return address.isReachable(PING_TIMEOUT);
	}

	private byte[] getMacBytes(String macStr) {
		byte[] bytes = new byte[6];
		String[] hex = macStr.split("(:|\\-)");

		if (hex.length != 6) {
			throw new IllegalArgumentException("Invalid MAC address");
		}

		try {
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) Integer.parseInt(hex[i], 16);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid hex digit in MAC address");
		}

		return bytes;
	}
}
