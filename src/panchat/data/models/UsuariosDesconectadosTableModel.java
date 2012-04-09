package panchat.data.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import panchat.data.ChatRoom;

public class UsuariosDesconectadosTableModel extends AbstractTableModel
		implements Observer {

	private static final long serialVersionUID = 1L;

	private ChatRoom canal;

	public UsuariosDesconectadosTableModel(ChatRoom canal) {
		this.canal = canal;
		this.canal.addObserver(this);
	}

	@Override
	public String getColumnName(int col) {
		return "Usuarios desconectados";
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return canal.getNumUsuariosDesconectados();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= 0 && rowIndex < getRowCount()) {
			return canal.getUsuarioDesconectado(rowIndex);
		} else
			return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.fireTableDataChanged();
	}
}
