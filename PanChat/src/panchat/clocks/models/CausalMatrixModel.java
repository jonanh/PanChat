package panchat.clocks.models;

import javax.swing.table.AbstractTableModel;

import panchat.clocks.CausalMatrix;
import panchat.data.User;
import simulation.model.SimulationModel;

public class CausalMatrixModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private CausalMatrix cm;
	private SimulationModel simulationModel;

	public CausalMatrixModel(CausalMatrix cm, SimulationModel simulationModel) {
		this.cm = cm;
		this.simulationModel = simulationModel;
	}

	@Override
	public int getColumnCount() {
		return cm.HashMatrix.size();
	}

	@Override
	public int getRowCount() {
		return cm.HashMatrix.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		User rowUser = simulationModel.getUser(rowIndex);
		User columnUser = simulationModel.getUser(columnIndex);

		return cm.getValue(rowUser, columnUser);
	}
}
