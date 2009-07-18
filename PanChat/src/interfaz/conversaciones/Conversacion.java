package interfaz.conversaciones;

import interfaz.ventanas.VentanaBase;
import interfaz.elementos.Editor;
import interfaz.paneles.MiPanel;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JScrollPane;

public class Conversacion extends MiPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	Editor escritura;
	Editor log;

	HashMap<String, String> hash;
	Vector<Character> lectura;

	public Conversacion(HashMap<String, String> tabla) {
		super();
		construir(tabla);
	}

	public Conversacion(String ruta, HashMap<String, String> tabla) {
		super(ruta);
		construir(tabla);
	}

	private void construir(HashMap<String, String> tabla) {

		hash = tabla;

		escritura = new Editor("cuadro.jpg", hash);

		log = new Editor("pizarra.png", hash);
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
		HashMap<String, String> e = new HashMap<String, String>(100);
		String f = "xd";
		e.put(f, "xd.gif");
		e.put("nubes", "nubes.jpg");
		e.put("pizarra", "pizarra.png");

		Conversacion edit = new Conversacion(e);
		in.add(edit);
		in.setTitle("conversaci�n con el jonan");
		in.setVisible(true);

	}

}
