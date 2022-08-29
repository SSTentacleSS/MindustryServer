package mindustry.server.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Bundler {
	public static ResourceBundle bundle = ResourceBundle.getBundle("resources");

	public static String getLocalized(Locale locale, String key) {
		return ResourceBundle.getBundle("resources", locale).getString(key);
	}

	public static String getLocalized(String key) {
		return bundle.getString(key);
	}

	public static String getLocalized(String key, Object... formatObjects) {
		return getLocalized(key).formatted(formatObjects);
	}
}
