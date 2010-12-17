package panchat.clocks.models;

import javax.swing.table.AbstractTableModel;

import panchat.clocks.CausalMatrix;
import panchat.data.User;
import panchat.simulation.model.SimulationModel;

public class CausalMatrixModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private CausalMatrix cm = null;
	private SimulationModel simulationModel;

	public CausalMatrixModel(SimulationModel simulationModel) {
		this.simulationModel = simulationModel;
	}

	public void setCausalMatrix(CausalMatrix cm) {
		this.cm = cm;
		this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		if (cm != null)
			return cm.HashMatrix.size();
		else
			return 0;
	}

	@Override
	public int getRowCount() {
		if (cm != null)
			return cm.HashMatrix.size();
		else
			return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		User rowUser = simulationModel.getUser(rowIndex);
		User columnUser = simulationModel.getUser(columnIndex);

		return cm.getValue(rowUser, columnUser);
	}
}
