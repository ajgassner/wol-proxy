package at.agsolutions.wol.ui.view;

import at.agsolutions.wol.domain.Host;
import at.agsolutions.wol.service.IHostService;
import at.agsolutions.wol.ui.AccessControl;
import at.agsolutions.wol.ui.MessageProvider;
import at.agsolutions.wol.ui.MessageProvider.MessageType;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Locale;

@SpringComponent
@UIScope
@Slf4j
public class MainView extends VerticalLayout {

	@Autowired
	private IHostService hostService;

	@Autowired
	private AccessControl accessControl;

	private Host selectedHost;
	private boolean initialized = false;

	public void init() {
		if (initialized) {
			return;
		}
		initialized = true;

		setSizeFull();
		setSpacing(true);
		setMargin(true);

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSpacing(true);
		addComponent(toolbar);
		setExpandRatio(toolbar, 0);

		Button wolButton = new Button("Wake On Lan", e -> wolButtonClicked());
		wolButton.setEnabled(false);
		wolButton.setIcon(FontAwesome.POWER_OFF);
		toolbar.addComponent(wolButton);

		Button pingButton = new Button("Ping", e -> pingButtonClicked());
		pingButton.setEnabled(false);
		pingButton.setIcon(FontAwesome.BOLT);
		toolbar.addComponent(pingButton);

		Button logoutButton = new Button("Logout", e -> logoutButtonClicked());
		logoutButton.setIcon(FontAwesome.TIMES);
		toolbar.addComponent(logoutButton);

		BeanContainer<String, Host> hosts = new BeanContainer<>(Host.class);
		hosts.setBeanIdProperty("mac");
		hostService.findAll().forEach(hosts::addBean);

		Table table = new Table("Hosts", hosts);
		table.setSelectable(true);
		table.setColumnReorderingAllowed(true);
		table.setNullSelectionAllowed(false);
		table.setSizeFull();
		table.setVisibleColumns("name", "mac", "ip", "broadcastIp", "port");
		table.setColumnHeaders("Name", "MAC adress", "IP", "Broadcast IP", "Port");
		table.setConverter("port", new StringToIntegerWithoutZeroConverter());
		table.setColumnCollapsingAllowed(true);
		table.addItemClickListener(e -> {
			selectedHost = hosts.getItem(e.getItemId()).getBean();
			wolButton.setEnabled(true);
			pingButton.setEnabled(true);
		});

		addComponent(table);
		setExpandRatio(table, 1);
	}

	private void logoutButtonClicked() {
		accessControl.logout();
		UI.getCurrent().close();
		Page.getCurrent().reload();
	}

	private void wolButtonClicked() {
		if (selectedHost == null) {
			return;
		}

		try {
			hostService.wakeOnLan(selectedHost);
		} catch (IOException | IllegalArgumentException e) {
			log.error("Unable to send WOL packet to {}", selectedHost, e);
			MessageProvider.showMessage("Unable to send WOL packet to host '" + selectedHost.getName() + "'",
					"Detailed message: " + e.getMessage(), MessageType.ERROR);
		}
	}

	private void pingButtonClicked() {
		if (selectedHost == null) {
			return;
		}

		try {
			if (hostService.ping(selectedHost)) {
				MessageProvider.showMessage("Host '" + selectedHost.getName() + "' is reachable");
			} else {
				MessageProvider.showMessage("Host '" + selectedHost.getName() + "' is unreachable", null, MessageType.WARNING);
			}
		} catch (IOException e) {
			log.error("Unable to ping {}", selectedHost, e);
			MessageProvider.showMessage("Unable to ping host '" + selectedHost.getName() + "'",
					"Detailed message: " + e.getMessage(), MessageType.ERROR);
		}
	}

	private static class StringToIntegerWithoutZeroConverter extends StringToIntegerConverter {
		@Override
		public String convertToPresentation(final Integer value, final Class<? extends String> targetType, final Locale locale) throws
				ConversionException {
			String result = super.convertToPresentation(value, targetType, locale);
			return result.equals("0") ? "" : result;
		}
	}
}
