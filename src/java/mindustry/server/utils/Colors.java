package mindustry.server.utils;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public final class Colors {

	public static String applyStyle(String text, AttributedStyle style) {
		return new AttributedStringBuilder().style(style).append(text).toAnsi();
	}
}
