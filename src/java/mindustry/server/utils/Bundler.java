package mindustry.server.utils;

import java.util.Locale;
import java.util.ResourceBundle;
import mindustry.gen.Player;

public class Bundler {
	public static ResourceBundle bundle = ResourceBundle.getBundle("resources");

	public static String getLocalized(
		Player player,
		String key,
		Object... formatObjects
	) {
		return getLocalized(new Locale(player.locale), key, formatObjects);
	}

	public static String getLocalized(
		Player player,
		Locale locale,
		String key
	) {
		return getLocalized(new Locale(player.locale), key);
	}

	public static String getLocalized(
		Locale locale,
		String key,
		Object... formatObjects
	) {
		return getLocalized(locale, key).formatted(formatObjects);
	}

	public static String getLocalized(Locale locale, String key) {
		return ResourceBundle.getBundle("resources", locale).getString(key);
	}

	// Для системных языков

	public static String getLocalized(String key, Object... formatObjects) {
		return getLocalized(key).formatted(formatObjects);
	}

	public static String getLocalized(String key) {
		return bundle.getString(key);
	}
}
