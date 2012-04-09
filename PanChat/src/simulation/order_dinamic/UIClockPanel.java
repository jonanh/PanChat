package simulation.order_dinamic;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;


import simulation.model.CausalMatrixModel;
import simulation.model.VectorClockModel;
import simulation.view.CellPosition;
import simulation.view.IPositionObserver;
import simulation.view.IPosition;

public class UIClockPanel extends JPanel implements Observer, IPositionObserver {

	private static final long serialVersionUID = 1L;

	private VectorClockModel vectorClock;
	private CausalMatrixModel matrixClock;
	private SimulationOrderModel simulationOrderModel;
	private CellPosition position;

	private JTable jtableVector;
	private JTable jtableMatrix;
	private JTextArea textArea;

	public UIClockPanel(SimulationOrderModel simulation) {

		simulationOrderModel = simulation;
		simulation.addObserver(this);

		vectorClock = new VectorClockModel(simulation);
		matrixClock = new CausalMatrixModel(simulation);

		jtableVector = new JTable(vectorClock);
		jtableMatrix = new JTable(matrixClock);

		textArea = new JTextArea();
		textArea.setEditable(false);

		this.setLayout(new GridLayout(3, 1));
		this.add(new JScrollPane(jtableVector));
		this.add(new JScrollPane(jtableMatrix));
		this.add(new JScrollPane(textArea));

		setPreferredSize(new Dimension(120, 0));

	}

	/**
	 * Establece el reloj que visualizaremos
	 * 
	 * @param clock
	 */
	@Override
	public void setPosition(IPosition pos, IPositionObserver.Mode mode) {
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
			textArea.setText(clock.total);
		}
	}
}
