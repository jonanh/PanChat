package simulation;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import simulation.chandy_lamport.ChandyLamport;
import simulation.order_dinamic.DinamicSimulation;
import simulation.order_static.StaticSimulation;

public class Simulation extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String title = "Simulador";

	ChandyLamport chandyLamport = new ChandyLamport();
	StaticSimulation staticSimulation = new StaticSimulation();
	DinamicSimulation dinamicSimulation = new DinamicSimulation();

	JTabbedPane pane = new JTabbedPane();

	public Simulation() {
		super(title);

		pane.addTab("Simulación estática", staticSimulation);
		pane.addTab("Simulación dinámica", dinamicSimulation);
		pane.addTab("Simulación Chandy-Lamport", chandyLamport);
		
		this.getContentPane().add(pane);
		this.setSize(1200, 500);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {

		// Look & feel nativo en Java
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		new Simulation();
	}
}
