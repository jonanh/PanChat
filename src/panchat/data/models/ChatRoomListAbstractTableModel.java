package panchat.data.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import panchat.data.ChatRoomList;

public class ChatRoomListAbstractTableModel extends AbstractTableModel
		implements Observer {

	private static final long serialVersionUID = 1L;

	private ChatRoomList listaCanales;

	/**
	 * Crea una nueva clase con el modelo de datos de una lista de canales
	 * 
	 * @param listaCanales
	 */
	public ChatRoomListAbstractTableModel(ChatRoomList listaCanales) {
		this.listaCanales = listaCanales;
		this.listaCanales.addObserver(this);
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Nombre del canal";
		default:
			return "Número de usuarios";
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return listaCanales.length();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// Comprobamos los "bounds"
		if (rowIndex >= 0 && rowIndex < getRowCount()) {
			switch (columnIndex) {
			case 0:
				return listaCanales.getChannel(rowIndex).getName();
			default:
				return listaCanales.getChannel(rowIndex)
						.getNumUsuariosConectados();
			}
		} else
			return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.fireTableDataChanged();
	}
}
