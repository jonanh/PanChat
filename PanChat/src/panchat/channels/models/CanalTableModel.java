package panchat.channels.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import panchat.channels.Canal;

public class CanalTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;

	private Canal canal;

	public CanalTableModel(Canal canal) {
		this.canal = canal;
		this.canal.addObserver(this);
	}

	@Override
	public String getColumnName(int col) {
		return "Usuarios conectados";
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return canal.getNumUsuariosConectados();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return canal.getUsuarioConectado(rowIndex);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.fireTableDataChanged();
	}
}
