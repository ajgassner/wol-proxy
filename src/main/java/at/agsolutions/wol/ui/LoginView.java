package at.agsolutions.wol.ui;

import at.agsolutions.wol.AccessControl;
import at.agsolutions.wol.MessageType;
import at.agsolutions.wol.domain.User;
import at.agsolutions.wol.service.IUserService;
import com.vaadin.data.Validator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class LoginView extends VerticalLayout {
	private static final int NOTIFICATION_DELAY = 3000;

	@Value("${app.name}")
	private String appName;

	@Autowired
	private AccessControl accessControl;

	@Autowired
	private MainView mainView;

	private TextField userNameField;
	private PasswordField passwordField;

	@PostConstruct
	private void buildLayout() {
		setSizeFull();

		VerticalLayout container = new VerticalLayout();
		container.setSizeUndefined();
		container.setSpacing(true);
		addComponent(container);
		setComponentAlignment(container, Alignment.MIDDLE_CENTER);

		Label title = new Label(appName);
		title.setStyleName(ValoTheme.LABEL_H1);
		title.setWidthUndefined();
		container.addComponent(title);
		container.setComponentAlignment(title, Alignment.BOTTOM_CENTER);

		FormLayout form = new FormLayout();
		form.setSizeUndefined();
		form.setMargin(true);

		userNameField = new TextField("Username");
		userNameField.setDescription("Please enter your username");
		userNameField.setRequired(true);
		userNameField.setIcon(FontAwesome.USER);
		userNameField.setWidth(250, Unit.PIXELS);
		userNameField.focus();
		userNameField.setImmediate(true);
		form.addComponent(userNameField);

		passwordField = new PasswordField("Password");
		passwordField.setDescription("Please enter your password");
		passwordField.setRequired(true);
		passwordField.setIcon(FontAwesome.LOCK);
		passwordField.setWidth(250, Unit.PIXELS);
		passwordField.setImmediate(true);
		form.addComponent(passwordField);

		Panel formPanel = new Panel("User login");
		formPanel.setContent(form);
		container.addComponent(formPanel);

		Button loginButton = new Button("Login");
		loginButton.setWidth(100, Unit.PERCENTAGE);
		loginButton.setIcon(FontAwesome.ANGLE_DOUBLE_RIGHT);
		loginButton.addClickListener(e -> loginClick());
		container.addComponent(loginButton);

		ShortcutListener shortcutListener = new Button.ClickShortcut(loginButton, ShortcutAction.KeyCode.ENTER);
		addShortcutListener(shortcutListener);
	}

	private void loginClick() {
		if (!validateInput()) {
			return;
		}

		User currentUser = null;

		try {
			currentUser = accessControl.login(userNameField.getValue(), passwordField.getValue());
		} catch (IUserService.WrongPasswordException | IUserService.UserNotFoundException e) {
			showLoginMessage("Login failed", "Username and password are case sensitive!", MessageType.WARNING);
		}

		if (currentUser != null) {
			showLoginMessage("Welcome " + currentUser.getName(), "You are now logged in", MessageType.INFO);
			UI.getCurrent().setContent(mainView);
		}
	}

	private boolean validateInput() {
		try {
			userNameField.validate();
			passwordField.validate();
			return true;
		} catch (Validator.EmptyValueException e) {
			userNameField.setRequiredError("Username is required");
			passwordField.setRequiredError("Password is required");
			return false;
		}
	}

	private void showLoginMessage(String title, String message, MessageType type) {
		Notification notification = new Notification(title, message);
		notification.setDelayMsec(NOTIFICATION_DELAY);
		notification.setPosition(Position.TOP_CENTER);

		switch (type) {
			case INFO:
				notification.setStyleName(Notification.Type.HUMANIZED_MESSAGE.getStyle());
				notification.setIcon(FontAwesome.INFO);
				break;
			case WARNING:
				notification.setStyleName(Notification.Type.WARNING_MESSAGE.getStyle());
				notification.setIcon(FontAwesome.WARNING);
				break;
			default:
				notification.setStyleName(Notification.Type.ERROR_MESSAGE.getStyle());
				notification.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
		}

		notification.show(Page.getCurrent());
	}
}