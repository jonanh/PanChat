package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaBase extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//tama�o de nuestra ventana
	final int ancho=400;
	final int alto=400;
	//variables para el default toolkit y para el tama�o de ventana
	Toolkit pantalla;
	Dimension dimension;
	double tama�oX,tama�oY;
	double centroX,centroY;
	
	//panel al que se aplicar� un borderlayout
	
	
	
	
	public VentanaBase(){
		super();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(ancho,alto);

		//obtenemos el tama�o de la pantalla
		pantalla=Toolkit.getDefaultToolkit();
		dimension=pantalla.getScreenSize();
		tama�oX=dimension.getWidth();
		tama�oY=dimension.getHeight();
		
		//calculamos el centro de la pantalla en X y en Y
		centroX=tama�oX/2;
		centroY=tama�oY/2;
		
		//colocamos la ventana en el centro
		this.setLocation((int)centroX-ancho/2,(int)centroY-alto/2);
		
		

		
	}
	
	public VentanaBase(String name){
		this();
		this.setTitle(name);
	}
	

	public static void main(String[] args){
		new VentanaBase();
	}
}
