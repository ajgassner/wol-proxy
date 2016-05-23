package at.agsolutions.wol;

import at.agsolutions.wol.ui.LoginView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@PreserveOnRefresh
@Push
@SpringUI
@Theme("valo")
@Title("WOL proxy")
@Slf4j
public class MainUI extends UI {

	static {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.install();
		Logger.getLogger("global").setLevel(Level.FINEST);
	}

	@Autowired
	private LoginView loginView;

	@Override
	protected void init(final VaadinRequest vaadinRequest) {
		setContent(loginView);
	}
}
