package interfaz;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class PanelInicio extends MiPanel{
	
	
	public PanelInicio(String ruta){
		super(ruta);
	}
	//¿¿¿¿¿¿sirve??????


	public static void main(String[] args){
		JFrame frame=new JFrame();
		PanelInicio pan=new PanelInicio("puerta7.jpg");
		pan.add(new JTextField());
		frame.add(pan);
		frame.setDefaultCloseOperation(2);
		frame.setVisible(true);
		frame.setSize(400,400);
	}
}
