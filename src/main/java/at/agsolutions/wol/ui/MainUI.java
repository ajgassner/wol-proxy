package at.agsolutions.wol.ui;

import at.agsolutions.wol.ui.view.LoginView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
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
@Theme(MainUI.THEME)
public class MainUI extends UI {

	public static final String THEME = "wol";

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
		setLocale(Locale.ENGLISH);
		getPage().setTitle(appName);
		setContent(loginView);
	}
}
