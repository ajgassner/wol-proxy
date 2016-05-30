package at.agsolutions.wol.ui;

import at.agsolutions.wol.AccessControl;
import at.agsolutions.wol.domain.Host;
import at.agsolutions.wol.service.IHostService;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringComponent
@UIScope
public class MainView extends VerticalLayout {

	@Autowired
	private IHostService hostService;

	@Autowired
	private AccessControl accessControl;

	private Host selectedHost;

	@PostConstruct
	private void buildLayout() {
		setSizeFull();
		setSpacing(true);
		setMargin(true);

		// TODO: lazy init

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSpacing(true);
		addComponent(toolbar);
		setExpandRatio(toolbar, 0);

		Button wolButton = new Button("Wake On Lan", e -> {
			if (selectedHost != null) {
				try {
					hostService.wakeOnLan(selectedHost);
				} catch (IOException e1) {
					e1.printStackTrace(); // TODO!
				}
			}
		});
		wolButton.setEnabled(false);
		toolbar.addComponent(wolButton);

		Button logoutButton = new Button("Logout", e -> {
			accessControl.logout();
			UI.getCurrent().close();
			Page.getCurrent().reload();
		});
		toolbar.addComponent(logoutButton);

		BeanContainer<String, Host> beans = new BeanContainer<>(Host.class);

		beans.setBeanIdProperty("mac");
		hostService.findAll().forEach(beans::addBean);

		Table table = new Table("Beans of All Sorts", beans);
		table.setSelectable(true);
		table.setColumnReorderingAllowed(true);
		table.setNullSelectionAllowed(false);
		table.setSizeFull();
		table.setVisibleColumns("name", "mac", "broadcastIp", "port");
		table.setColumnHeaders("Name", "MAC adress", "Broadcast IP", "Port");
		table.setColumnCollapsingAllowed(true);
		table.setColumnCollapsed("port", true);
		table.addItemClickListener(e -> {
			selectedHost = beans.getItem(e.getItemId()).getBean();
			wolButton.setEnabled(true);
		});

		addComponent(table);
		setExpandRatio(table, 1);
	}
}
