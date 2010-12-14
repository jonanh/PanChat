package panchat.clocks.models;

import javax.swing.table.AbstractTableModel;

import panchat.clocks.VectorClock;
import panchat.data.User;
import simulation.model.SimulationModel;

public class VectorClockModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private VectorClock vc;
	private SimulationModel simulationModel;

	public VectorClockModel(VectorClock cm, SimulationModel simulationModel) {
		this.vc = cm;
		this.simulationModel = simulationModel;
	}

	@Override
	public int getColumnCount() {
		return simulationModel.getNumProcesses();
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		User columnUser = simulationModel.getUser(columnIndex);

		return vc.getValue(columnUser);
	}

}
