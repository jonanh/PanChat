package interfaz.elementos;

import interfaz.ventanas.VentanaBase;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;

public class Datos extends JPanel implements MouseListener, KeyListener,
		FocusListener {

	private static final long serialVersionUID = 1L;

	/*
	 * esta clase consta de un jLabel que contiene un dato y cuando se clica
	 * aparece una caja e texto como el estado personal del msn
	 */
	JLabel label = new JLabel();
	JTextField texto = new JTextField();
	int tamanyo = 7;

	public Datos(String defecto) {

		super();
		this.setOpaque(false);
		/*
		 * ponemos coomo layout el overlaylayout que nos permite superponer
		 * componetnes
		 */
		this.setLayout(new OverlayLayout(this));

		anyadirComponentes(defecto);

		anyadirEscuchas();

	}

	public Datos(String defecto, int tam) {
		this(defecto);
		tamanyo = tam;
	}

	private void anyadirComponentes(String defecto) {
		this.add(label);
		this.add(texto);

		texto.setColumns(tamanyo);
		texto.setVisible(false);
		texto.setText(defecto);

		label.setText(defecto);
	}

	private void anyadirEscuchas() {
		label.addMouseListener(this);
		texto.addMouseListener(this);
		texto.addKeyListener(this);
		texto.addFocusListener(this);
	}

	public void modificarTamanyo(int tam) {
		texto.setColumns(tam);
	}

	public String obtenerTexto() {
		return label.getText();
	}

	public void cambiarColor(Color color) {
		label.setForeground(color);
	}

	public void cambiarTamanyoLabel(int tam) {
		label.setFont(new Font("Times New Roman", Font.BOLD, tam));
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// quien produce el evento
		if (arg0.getSource() == label) {
			label.setVisible(false);
			texto.setVisible(true);
			// pedir el focus
			texto.requestFocus();
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	/*
	 * métodos de keyListener
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyChar() == '\n') {
			label.setText(texto.getText());
			texto.setVisible(false);
			label.setVisible(true);

		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public static void main(String[] args) {
		VentanaBase frame = new VentanaBase();
		Datos ejemplo = new Datos("ejemplo");
		frame.add(ejemplo);
	}

	/*
	 * si no escribimos ningún nombre si no que simplemento clickamos en otro
	 * componente
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		texto.setVisible(false);
		label.setVisible(true);
	}

}
