package simulation.order_static;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import simulation.model.SimulationArrowModel;
import simulation.view.SimulationView;

@SuppressWarnings("serial")
public class UIStaticSimulation extends JPanel {

	private UIToolbarPanel menu;
	private SimulationView view;
	private SimulationModel model;

	public UIStaticSimulation() {

		model = new SimulationModel();
		view = new SimulationView(model);

		view.addSimulator(model);
		menu = new UIToolbarPanel(this);

		this.setLayout(new BorderLayout());
		this.add(menu, BorderLayout.NORTH);
		this.add(new JScrollPane(view), BorderLayout.CENTER);
		this.add(new JScrollPane(view), BorderLayout.CENTER);
	}

	public SimulationView getSimulationView() {
		return view;
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

		UIStaticSimulation simulation = new UIStaticSimulation();
		JFrame window = new JFrame("Simulación paso de mensajes");
		window.getContentPane().add(simulation);
		window.setSize(1200, 500);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}
