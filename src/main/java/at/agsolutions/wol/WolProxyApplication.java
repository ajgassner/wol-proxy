package at.agsolutions.wol;

import at.agsolutions.wol.domain.HostHolder;
import at.agsolutions.wol.domain.UserHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@SpringBootApplication
public class WolProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(WolProxyApplication.class, args);
	}

	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(HostHolder.class, UserHolder.class);
		return marshaller;
	}
}
