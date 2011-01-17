package interfaz.ventanas;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class VentanaBase extends JFrame {

	private static final long serialVersionUID = 1L;

	// tamaño de nuestra ventana
	final int ancho = 400;
	final int alto = 400;
	// variables para el default toolkit y para el tamaño de ventana
	Toolkit pantalla;
	Dimension dimension;
	double tamanyoX, tamanyoY;
	double centroX, centroY;

	// panel al que se aplicará un borderlayout

	public VentanaBase() {
		super();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(ancho, alto);

		// obtenemos el tamaño de la pantalla
		pantalla = Toolkit.getDefaultToolkit();
		dimension = pantalla.getScreenSize();
		tamanyoX = dimension.getWidth();
		tamanyoY = dimension.getHeight();

		// calculamos el centro de la pantalla en X y en Y
		centroX = tamanyoX / 2;
		centroY = tamanyoY / 2;

		// colocamos la ventana en el centro
		this.setLocation((int) centroX - ancho / 2, (int) centroY - alto / 2);

	}

	public VentanaBase(String name) {
		this();
		this.setTitle(name);
	}

	public static void main(String[] args) {
		new VentanaBase();
	}
}
