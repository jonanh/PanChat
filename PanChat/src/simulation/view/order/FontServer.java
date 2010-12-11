package simulation.view.order;

import java.awt.Font;
import java.awt.Graphics2D;

public class FontServer {
	static Font BOLD_FONT = new Font("TimesRoman",Font.BOLD,15);
	
	static Font oldFont;
	
	static void boldFont(Graphics2D g2){
		oldFont = g2.getFont();
		BOLD_FONT = new Font(oldFont.getName(),Font.BOLD,12);
		g2.setFont(BOLD_FONT);
	}
	
	static void restoreFont(Graphics2D g2){
		g2.setFont(oldFont);
	}
}
