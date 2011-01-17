package simulation3;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import simulation3.dinamic_order.SimulationOrderModel;
import simulation3.model.SimulationArrowModel;
import simulation3.view.SimulationView;

@SuppressWarnings("serial")
public class Simulation extends JPanel {

	private ToolbarPanel menu;
	private SimulationView view;
	private SimulationArrowModel model;
	private SimulationOrderModel simul;
	private ClockPanel panel;

	public Simulation() {
		
		model = new SimulationArrowModel();
		view = new SimulationView(model);
		
		simul = new SimulationOrderModel(model);
		panel = new ClockPanel(simul);

		view.addSimulator(simul);
		view.addPositionObserver(panel);
		
		menu = new ToolbarPanel(this);

		this.setLayout(new BorderLayout());
		this.add(menu, BorderLayout.NORTH);
		this.add(new JScrollPane(view), BorderLayout.CENTER);
		this.add(new JScrollPane(view), BorderLayout.CENTER);
		this.add(panel, BorderLayout.EAST);
	}

	public SimulationView getSimulationView() {
		return view;
	}

	public SimulationOrderModel getSimulationOrderLayer() {
		return simul;
	}

	public void setSimulationModel(SimulationArrowModel simulationModel) {
		view.setSimulationModel(simulationModel);
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
