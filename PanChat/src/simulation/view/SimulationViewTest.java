package simulation.view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import simulation.model.SimulationModel;

/**
 * Interfaz grafica de una aplicacion
 */
public class SimulationViewTest extends JFrame {
	private static final long serialVersionUID = 1L;

	/*
	 * un panel es un componente en el cual se colocan objetos graficos
	 */
	private JPanel panelNorte = new JPanel();

	private JButton operarButton = new JButton("Do it");
	private JButton finButton = new JButton("Fin");

	private SimulationView tablero = new SimulationView(new SimulationModel());

	/**
	 * Construye una nueva ventana
	 */
	public SimulationViewTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().setLayout(new BorderLayout());

		panelNorte.add(operarButton);
		panelNorte.add(finButton);

		this.getContentPane().add(panelNorte, BorderLayout.PAGE_START);
		this.getContentPane()
				.add(new JScrollPane(tablero), BorderLayout.CENTER);

		this.pack();
		this.setVisible(true);
	}

	/*
	 * Puesta en marcha...
	 */
	public static void main(String args[]) {
		SimulationViewTest game = new SimulationViewTest();
		game.setVisible(true);
	}
}