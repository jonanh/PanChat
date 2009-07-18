package interfaz.elementos;

import interfaz.ventanas.VentanaBase;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class Editor extends JTextPane implements KeyListener, DocumentListener {

	private static final long serialVersionUID = 1L;

	HashMap<String, String> hash;
	Vector<Character> teclasPulsadas;

	/*
	 * el vector contenido se usará para saber qué es lo que se está
	 * introduciendo se usará ~ para indicar el inicio y el fin de una imagen
	 */

	Vector<Character> contenido;
	String path;
	ImageIcon icon;
	Image imagen;

	/*
	 * Al insertar un icono hay un problema y es que la última letra de la
	 * palabra clave, en xd sería d ,se escribe después del icono y no he
	 * encontrado manera de quitarla ni sé cómo se puede quitar el echo del
	 * teclado. ActionMap digamos que le dice al jtextpane qué acción llevar a
	 * cabo ante una pulsación, por ello cuándo vaya a escribir la última letra,
	 * pondremos su ActionMap a null.:P
	 */
	ActionMap mapa;
	// para saber si se quitó el mapa
	boolean quitado = false;

	int posActual, posAnterior;
	boolean delete = true;

	char[] clave;
	String clave2;
	String ruta;

	String camino = "D:\\Java\\PanChat\\src\\interfaz\\";

	StyledDocument documento;

	public Editor(String ruta, HashMap<String, String> hash) {
		super();
		this.setOpaque(false);
		path = ruta;
		this.hash = hash;
		icon = new ImageIcon(this.getClass().getResource(path));
		imagen = icon.getImage();
		this.setForeground(Color.red);
		// this.setText("\n     ");
		posActual = getCaretPosition();
		posAnterior = posActual;
		this.addKeyListener(this);
		this.getDocument().addDocumentListener(this);

		teclasPulsadas = new Vector<Character>();
		contenido = new Vector<Character>();

		mapa = this.getActionMap();
		documento = this.getStyledDocument();

	}

	public Vector<Character> obtenerContenido() {

		Vector<Character> temporal = (Vector<Character>) contenido.clone();
		contenido.removeAllElements();
		return temporal;
	}

	private void normalizarVector(Vector<Character> target, char[] clave) {
		// System.out.println(target.toString());
		int tamVector = target.size();
		int tamClave = clave.length;
		int indice = tamVector - tamClave;

		target.add(tamVector, '~');
		target.add(indice, '~');

	}

	public String vectorAString(Vector<Character> mensaje) {
		char mensajeCar[] = new char[mensaje.size()];
		String mensajeString;

		for (int i = 0; i < mensaje.size(); i++) {
			mensajeCar[i] = mensaje.elementAt(i);
		}

		mensajeString = new String(mensajeCar);
		return mensajeString;
	}

	public String obtenerMensaje() {
		return vectorAString(contenido);
	}

	public void escribirVector(Vector<Character> mensaje) {
		String elemento;
		String mensajeString = vectorAString(mensaje);
		StringTokenizer token = new StringTokenizer(mensajeString, "~");

		try {
			documento.insertString(getCaretPosition(), "\n", null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		while (token.hasMoreTokens()) {

			elemento = token.nextToken();
			// quitamos espacios porque si no no reconoce los emoticonos
			elemento = elemento.trim();

			if (hash.containsKey(elemento)) {
				this.insertIcon(new ImageIcon(new ImageIcon(camino
						+ hash.get(elemento)).getImage().getScaledInstance(25,
						25, 0)));

				try {
					documento.insertString(getCaretPosition(), " ", null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}

			} else {

				try {
					documento.insertString(getCaretPosition(), elemento, null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private char[] obtenerClave(Vector<Character> teclas) {

		char[] clave = new char[teclas.size()];
		int i = 0;
		for (; i <= teclas.size() - 1; i++) {
			char car = teclas.elementAt(i);
			clave[i] = car;
		}
		return clave;
	}

	public void paint(Graphics g) {
		g.drawImage(imagen, this.getX(), this.getY(), this.getWidth(), this
				.getHeight(), this);
		super.paint(g);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

		if (quitado) {
			this.setActionMap(mapa);
			quitado = false;
			contenido.add('~');
		}

		char tecla = arg0.getKeyChar();

		if (' ' <= tecla && tecla <= '}')
			contenido.addElement(tecla);

		if (tecla == '\n') {

			try {
				documento.insertString(getCaretPosition(), "     ", null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			teclasPulsadas.removeAllElements();

		} else {
			posAnterior = posActual;
			posActual = getCaretPosition();

			if (tecla == ' ')
				teclasPulsadas.removeAllElements();
			else
				teclasPulsadas.addElement(tecla);
		}

		clave = obtenerClave(teclasPulsadas);
		clave2 = new String(clave);

		if (hash.containsKey(clave2)) {

			ruta = hash.get(clave2);

			String abs = "D:\\Java\\PanChat\\src\\interfaz\\" + ruta;
			ImageIcon emoticon = new ImageIcon(abs);

			normalizarVector(contenido, clave);

			try {
				// quitar las letras que se quedan antes del emoticon
				documento.remove(
						getCaretPosition() - teclasPulsadas.size() + 1,
						teclasPulsadas.size() - 1);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}

			this.insertIcon(new ImageIcon(emoticon.getImage()
					.getScaledInstance(25, 25, 0)));

			this.setActionMap(null);
			quitado = true;
			teclasPulsadas.removeAllElements();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {

		int tam = teclasPulsadas.size();
		if (tam > 0)
			teclasPulsadas.removeElementAt(tam - 1);

		tam = contenido.size();
		if (tam > 0)
			contenido.removeElementAt(tam - 1);

	}

	public static void main(String[] args) {
		VentanaBase in = new VentanaBase();
		HashMap<String, String> e = new HashMap<String, String>(100);
		String f = "xd";
		e.put(f, "imagenes/xd.gif");
		e.put("nubes", "imagenes/nubes.jpg");
		e.put("pizarra", "imagenes/pizarra.png");

		Editor edit = new Editor("imagenes/pizarra.png", e);
		in.add(edit);
		in.setVisible(true);

	}
}
