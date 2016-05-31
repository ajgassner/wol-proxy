package at.agsolutions.wol.ui.view;

import at.agsolutions.wol.ui.MessageProvider;
import at.agsolutions.wol.ui.MessageProvider.MessageType;
import at.agsolutions.wol.domain.User;
import at.agsolutions.wol.service.IUserService;
import at.agsolutions.wol.ui.AccessControl;
import com.vaadin.data.Validator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
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
		loginButton.addClickListener(e -> loginClicked());
		container.addComponent(loginButton);

		ShortcutListener shortcutListener = new Button.ClickShortcut(loginButton, ShortcutAction.KeyCode.ENTER);
		addShortcutListener(shortcutListener);
	}

	private void loginClicked() {
		if (!validateInput()) {
			return;
		}

		User currentUser = null;

		try {
			currentUser = accessControl.login(userNameField.getValue(), passwordField.getValue());
		} catch (IUserService.WrongPasswordException | IUserService.UserNotFoundException e) {
			MessageProvider.showMessage("Login failed", "Username and password are case sensitive!", MessageType.ERROR);
		}

		if (currentUser != null) {
			MessageProvider.showMessage("Welcome " + currentUser.getName(), "You are now logged in");
			mainView.init();
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
}