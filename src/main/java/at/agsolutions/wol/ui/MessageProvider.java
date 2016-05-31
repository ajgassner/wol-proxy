package at.agsolutions.wol.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class MessageProvider {

	private static final int DELAY = 3000;

	private MessageProvider() {}

	public enum MessageType {
		INFO, WARNING, ERROR
	}

	public static void showMessage(String title) {
		showMessage(title, "");
	}

	public static void showMessage(String title, String body) {
		showMessage(title, body, MessageType.INFO);
	}

	public static void showMessage(String title, String body, MessageType type) {
		showMessage(title, body, type, Position.TOP_CENTER);
	}

	public static void showMessage(String title, String body, MessageType type, Position position) {
		Notification notification = new Notification(title, body);
		notification.setDelayMsec(DELAY);
		notification.setPosition(position);

		switch (type) {
			case INFO:
				notification.setStyleName(Notification.Type.HUMANIZED_MESSAGE.getStyle());
				notification.setIcon(FontAwesome.INFO);
				break;
			case WARNING:
				notification.setStyleName(Notification.Type.WARNING_MESSAGE.getStyle());
				notification.setIcon(FontAwesome.WARNING);
				break;
			case ERROR:
				notification.setStyleName(Notification.Type.ERROR_MESSAGE.getStyle());
				notification.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
				break;
			default:
				throw new UnsupportedOperationException("Notification type " + type + "not supported");
		}

		notification.show(Page.getCurrent());
	}
}
