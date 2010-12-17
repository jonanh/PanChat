package panchat.clocks.models;

import javax.swing.table.AbstractTableModel;

import panchat.clocks.VectorClock;
import panchat.data.User;
import panchat.simulation.model.SimulationModel;

public class VectorClockModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private SimulationModel simulationModel;

	private VectorClock sendClock = null;

	private VectorClock receiveClock = null;

	public VectorClockModel(SimulationModel simulationModel) {
		this.simulationModel = simulationModel;
	}

	public void setVectors(VectorClock sendClock, VectorClock receiveClock) {
		this.sendClock = sendClock;
		this.receiveClock = receiveClock;
		this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return simulationModel.getNumProcesses();
	}

	@Override
	public int getRowCount() {
		if (sendClock != null)
			return 2;
		else
			return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		User columnUser = simulationModel.getUser(columnIndex);

		if (rowIndex == 0)
			return sendClock.getValue(columnUser);
		else
			return receiveClock.getValue(columnUser);
	}
}
