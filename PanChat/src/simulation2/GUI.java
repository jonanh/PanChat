package simulation2;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GUI extends JPanel{
	InformationCanvas canvas;
	OptionsMenu menu;
	
	public GUI(){
		canvas = new InformationCanvas();
		menu = new OptionsMenu(canvas);
		
		this.setLayout(new BorderLayout());
		this.add(menu,BorderLayout.NORTH);
		this.add(canvas,BorderLayout.CENTER);
	}
	
	public static void main (String[] args){
		JFrame ventana = new JFrame ("Simulaci�n paso de mensajes");
		ventana.add(new GUI());
		
		ventana.setVisible(true);
		ventana.setSize(1200,500);
		ventana.setDefaultCloseOperation(2);
	}
}
