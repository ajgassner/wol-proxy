package at.agsolutions.wol;

import lombok.extern.slf4j.Slf4j;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Slf4j
public class WolUtil {

	private static final String HASH_ALGORITHM = "SHA-256";

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

	public static String generateSha256(String input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(HASH_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			log.error("Error generating hash", e);
			return null;
		}

		byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
		StringBuilder hexString = new StringBuilder();

		for (final byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}

		return hexString.toString();
	}
}
