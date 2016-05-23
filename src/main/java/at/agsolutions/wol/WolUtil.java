package at.agsolutions.wol;

import lombok.extern.slf4j.Slf4j;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
public class WolUtil {

	private WolUtil() {
		// prevent initialization
	}

	public static <T> Optional<T> unmarshalXml(Unmarshaller unmarshaller, String filePath, Class<T> clazz) {
		File file = new File(filePath);

		try (InputStream is = new FileInputStream(file)) {
			return Optional.of(clazz.cast(unmarshaller.unmarshal(new StreamSource(is))));
		} catch (IOException | XmlMappingException e) {
			log.error("Cannot unmarshal file {}. Maybe there is a XML syntax error?", file.getAbsolutePath(), e);
		}

		return Optional.empty();
	}
}
