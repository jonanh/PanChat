package interfaz.conversaciones;

import interfaz.ventanas.VentanaBase;
import interfaz.elementos.Editor;
import interfaz.paneles.MiPanel;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Vector;

import javax.swing.JScrollPane;

public class Conversacion extends MiPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	Editor escritura;
	Editor log;

	
	Vector<Character> lectura;

	public Conversacion() {
		super();
		construir();
	}

	public Conversacion(String ruta) {
		super(ruta);
		construir();
	}

	private void construir() {

		
		String ruta="/interfaz/imagenes/";

		escritura = new Editor(ruta+"cuadro.jpg");

		log = new Editor(ruta+"pizarra.png");
		log.setEditable(false);

		JScrollPane arriba = new JScrollPane(log);
		JScrollPane abajo = new JScrollPane(escritura);

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridheight = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.8;
		c.weighty = 0.8;
		this.add(arriba, c);

		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0.2;
		c.weighty = 0.2;
		this.add(abajo, c);

		añadirEscuchas();

		this.setOpaque(false);

	}

	// si no redefino el m�todo poniendo escritura.paint no me pinta la imagen
	// deescritura

	public void paint(Graphics g) {
		escritura.paint(g);
		super.paint(g);

	}

	private void añadirEscuchas() {
		escritura.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getKeyChar() == '\n') {
			// coger mensaje, ponerlo en log y enviarlo
			lectura = escritura.obtenerContenido();
			escritura.setText(null);
			log.escribirVector(lectura);
			lectura.removeAllElements();
		}

	}

	public static void main(String[] args) {
		VentanaBase in = new VentanaBase();
	

		Conversacion edit = new Conversacion();
		in.add(edit);
		in.setTitle("conversacion con el jonan");
		in.setVisible(true);

	}

}
