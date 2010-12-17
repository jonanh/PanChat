package panchat.simulation;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import panchat.simulation.model.SimulationModel;
import panchat.simulation.order.SimulationOrderLayer;
import panchat.simulation.view.SimulationView;

@SuppressWarnings("serial")
public class Simulation extends JPanel {

	private SimulationModel model;
	private SimulationView view;
	private ToolbarPanel menu;
	private SimulationOrderLayer simul;
	private ClockPanel clockPanel;

	public Simulation() {
		model = new SimulationModel();
		clockPanel = new ClockPanel(model);
		view = new SimulationView(model, clockPanel);
		menu = new ToolbarPanel(this);
		simul = new SimulationOrderLayer(view);

		this.setLayout(new BorderLayout());
		this.add(menu, BorderLayout.NORTH);
		this.add(new JScrollPane(view), BorderLayout.CENTER);
		this.add(new JScrollPane(view), BorderLayout.CENTER);
		this.add(clockPanel, BorderLayout.EAST);
	}

	public SimulationView getSimulationView() {
		return view;
	}

	public SimulationOrderLayer getSimulationOrderLayer() {
		return simul;
	}

	public void setSimulationModel(SimulationModel simulationModel) {
		view.setSimulationModel(simulationModel);
		simul.setSimulationModel(simulationModel);
	}

	public static void main(String[] args) {
		// Look & feel nativo en Java
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		Simulation simulation = new Simulation();
		JFrame window = new JFrame("Simulación paso de mensajes");
		window.getContentPane().add(simulation);
		window.setSize(1200, 500);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}
