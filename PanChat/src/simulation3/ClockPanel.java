package simulation3;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import order.clocks.SavedClocks;
import order.clocks.models.CausalMatrixModel;
import order.clocks.models.VectorClockModel;

import simulation3.dinamic_order.SimulationOrderModel;
import simulation3.view.CellPosition;
import simulation3.view.IPositionObserver;
import simulation3.view.Position;

public class ClockPanel extends JPanel implements Observer, IPositionObserver {

	private static final long serialVersionUID = 1L;

	private VectorClockModel vectorClock;
	private CausalMatrixModel matrixClock;
	private SimulationOrderModel simulationOrderModel;
	private CellPosition position;

	private JTable jtableVector;
	private JTable jtableMatrix;

	public ClockPanel(SimulationOrderModel simulation) {

		simulationOrderModel = simulation;
		simulation.addObserver(this);

		vectorClock = new VectorClockModel(simulation);
		matrixClock = new CausalMatrixModel(simulation);

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
	@Override
	public void setPosition(Position pos) {
		if (pos instanceof CellPosition)
			position = (CellPosition) pos;
		updateClocks();
	}

	@Override
	public void update(Observable o, Object arg) {
		updateClocks();
	}

	private void updateClocks() {
		SavedClocks clock = simulationOrderModel.getClocks(position);
		if (clock != null) {
			vectorClock.setVectors(clock.sendClock, clock.receiveClock);
			matrixClock.setCausalMatrix(clock.causal);
		}
	}
}
