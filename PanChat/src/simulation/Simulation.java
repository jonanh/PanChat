package simulation;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import simulation.model.SimulationModel;
import simulation.view.SimulationView;

@SuppressWarnings("serial")
public class Simulation extends JFrame {

	SimulationModel model;
	SimulationView view;
	ToolbarPanel menu;

	public Simulation() {
		super("Simulación paso de mensajes");
		this.setVisible(true);
		this.setSize(1200, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		model = new SimulationModel();
		view = new SimulationView(model);
		menu = new ToolbarPanel(view);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(menu, BorderLayout.NORTH);
		this.add(new JScrollPane(view), BorderLayout.CENTER);
		this.pack();
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
