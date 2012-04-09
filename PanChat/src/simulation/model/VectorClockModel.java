package simulation.model;

import javax.swing.table.AbstractTableModel;

import order.clocks.VectorClock;

import panchat.data.User;
import simulation.order_dinamic.SimulationOrderModel;

public class VectorClockModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private SimulationOrderModel simulationOrderModel;

	private VectorClock sendClock = null;

	private VectorClock receiveClock = null;

	public VectorClockModel(SimulationOrderModel simulationOrderModel) {
		this.simulationOrderModel = simulationOrderModel;
	}

	public void setVectors(VectorClock sendClock, VectorClock receiveClock) {
		this.sendClock = sendClock;
		this.receiveClock = receiveClock;
		this.fireTableStructureChanged();
		this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return simulationOrderModel.getNumProcesses();
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
		User columnUser = simulationOrderModel.getUser(columnIndex);

		if (rowIndex == 0)
			return sendClock.getValue(columnUser);
		else
			return receiveClock.getValue(columnUser);
	}
}
