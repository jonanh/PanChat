package simulation3.static_order;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;

public class FontServer implements Serializable {

	private static final long serialVersionUID = 1L;

	static Font BOLD_FONT = new Font("TimesRoman", Font.BOLD, 15);

	static Font oldFont;

	static void boldFont(Graphics2D g2) {
		oldFont = g2.getFont();
		BOLD_FONT = new Font(oldFont.getName(), Font.BOLD, 12);
		g2.setFont(BOLD_FONT);
	}

	static void restoreFont(Graphics2D g2) {
		g2.setFont(oldFont);
	}
}
