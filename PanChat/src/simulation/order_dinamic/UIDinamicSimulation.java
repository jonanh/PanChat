package simulation.order_dinamic;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import simulation.model.SimulationArrowModel;
import simulation.view.SimulationView;

@SuppressWarnings("serial")
public class UIDinamicSimulation extends JPanel {

	private UIToolbarPanel menu;
	private SimulationView view;
	private SimulationArrowModel model;
	private SimulationOrderModel simul;
	private UIClockPanel panel;
	private UIDebugWindow window;

	public UIDinamicSimulation() {

		model = new SimulationArrowModel();
		view = new SimulationView(model);
		simul = new SimulationOrderModel(view);
		panel = new UIClockPanel(simul);
		window = new UIDebugWindow(simul);

		view.addSimulator(simul);
		view.addPositionObserver(panel);
		view.addPositionObserver(window);

		menu = new UIToolbarPanel(this, simul);

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
		simul.setSimulationModel(simulationModel);
	}

	public static void main(String[] args) {
		// Look & feel nativo en Java
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		UIDinamicSimulation simulation = new UIDinamicSimulation();
		JFrame window = new JFrame("Simulación paso de mensajes");
		window.getContentPane().add(simulation);
		window.setSize(1200, 500);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}
