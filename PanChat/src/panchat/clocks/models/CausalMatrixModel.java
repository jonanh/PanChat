package panchat.clocks.models;

import javax.swing.table.AbstractTableModel;

import panchat.clocks.CausalMatrix;
import panchat.data.User;
import panchat.simulation.order.SimulationOrderModel;

public class CausalMatrixModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private CausalMatrix cm = null;
	private SimulationOrderModel simulationOrderModel;

	public CausalMatrixModel(SimulationOrderModel simulationModel) {
		this.simulationOrderModel = simulationModel;
	}

	public void setCausalMatrix(CausalMatrix cm) {
		this.cm = cm;
		this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return simulationOrderModel.getNumProcesses();
	}

	@Override
	public int getRowCount() {
		if (cm != null)
			return simulationOrderModel.getNumProcesses();
		else
			return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		User rowUser = simulationOrderModel.getUser(rowIndex);
		User columnUser = simulationOrderModel.getUser(columnIndex);

		return cm.getValue(rowUser, columnUser);
	}
}
