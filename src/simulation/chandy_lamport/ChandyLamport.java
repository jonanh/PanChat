package simulation.chandy_lamport;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChandyLamport extends JPanel {

	private static final long serialVersionUID = 1L;

	InformationCanvas canvas;
	OptionsMenu menu;

	public ChandyLamport() {
		canvas = new InformationCanvas();
		menu = new OptionsMenu(canvas);

		this.setLayout(new BorderLayout());
		this.add(menu, BorderLayout.NORTH);
		this.add(canvas, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		JFrame ventana = new JFrame("Simulación paso de mensajes");
		ventana.add(new ChandyLamport());

		ventana.setVisible(true);
		ventana.setSize(1200, 500);
		ventana.setDefaultCloseOperation(2);
	}
}
