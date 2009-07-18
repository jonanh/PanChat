package interfaz.paneles;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class PanelInicio extends MiPanel {

	private static final long serialVersionUID = 1L;

	public PanelInicio(String ruta) {
		super(ruta);
	}

	// sirve??????

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		PanelInicio pan = new PanelInicio("puerta7.jpg");
		pan.add(new JTextField());
		frame.add(pan);
		frame.setDefaultCloseOperation(2);
		frame.setVisible(true);
		frame.setSize(400, 400);
	}
}
