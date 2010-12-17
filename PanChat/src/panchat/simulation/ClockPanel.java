package panchat.simulation;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import panchat.clocks.SavedClocks;
import panchat.clocks.models.CausalMatrixModel;
import panchat.clocks.models.VectorClockModel;
import panchat.simulation.model.SimulationModel;

public class ClockPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private VectorClockModel vectorClock;
	private CausalMatrixModel matrixClock;

	private JTable jtableVector;
	private JTable jtableMatrix;

	public ClockPanel(SimulationModel simulationModel) {

		vectorClock = new VectorClockModel(simulationModel);
		matrixClock = new CausalMatrixModel(simulationModel);

		jtableVector = new JTable(vectorClock);
		jtableMatrix = new JTable(matrixClock);

		this.setLayout(new GridLayout(2, 1));
		this.add(new JScrollPane(jtableVector));
		this.add(new JScrollPane(jtableMatrix));

		setPreferredSize(new Dimension(120, 0));

	}

	/**
	 * Establece el reloj que visualizaremos
	 * 
	 * @param clock
	 */
	public void setClock(SavedClocks clock) {
		if (clock != null) {
			vectorClock.setVectors(clock.sendClock, clock.receiveClock);
			matrixClock.setCausalMatrix(clock.causal);
		}
	}
}
