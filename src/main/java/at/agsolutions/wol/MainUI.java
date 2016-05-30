package at.agsolutions.wol;

import at.agsolutions.wol.ui.LoginView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@PreserveOnRefresh
@Push
@SpringUI
public class MainUI extends UI {

	private static final String THEME = "valo";

	@Value("${app.name}")
	private String appName;

	static {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.install();
		Logger.getLogger("global").setLevel(Level.FINEST);
	}

	@Autowired
	private LoginView loginView;

	@Override
	protected void init(final VaadinRequest vaadinRequest) {
		setTheme(THEME);
		setLocale(Locale.ENGLISH);
		getPage().setTitle(appName);
		setContent(loginView);
	}
}
