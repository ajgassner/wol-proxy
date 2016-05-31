package at.agsolutions.wol.ui.view;

import at.agsolutions.wol.domain.Host;
import at.agsolutions.wol.service.IHostService;
import at.agsolutions.wol.ui.AccessControl;
import at.agsolutions.wol.ui.MessageProvider;
import at.agsolutions.wol.ui.MessageProvider.MessageType;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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
		wolButton.setIcon(FontAwesome.BOLT);
		toolbar.addComponent(wolButton);

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
		table.setVisibleColumns("name", "mac", "broadcastIp", "port");
		table.setColumnHeaders("Name", "MAC adress", "Broadcast IP", "Port");
		table.setColumnCollapsingAllowed(true);
		table.addItemClickListener(e -> {
			selectedHost = hosts.getItem(e.getItemId()).getBean();
			wolButton.setEnabled(true);
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
}
