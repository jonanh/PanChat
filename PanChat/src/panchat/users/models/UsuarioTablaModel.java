package panchat.users.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import panchat.users.ListaUsuarios;

public class UsuarioTablaModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;

	private ListaUsuarios listaUsuarios;

	public UsuarioTablaModel(ListaUsuarios listaUsuario) {
		this.listaUsuarios = listaUsuario;
		this.listaUsuarios.addObserver(this);
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Usuarios conectados";
		case 1:
			return "IP";
		case 2:
			return "Port";
		default:
			return "UUID";
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return listaUsuarios.getNumUsuarios();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return listaUsuarios.getUsuario(rowIndex).nickName;
		case 1:
			return listaUsuarios.getUsuario(rowIndex).ip;
		case 2:
			return listaUsuarios.getUsuario(rowIndex).port;
		default:
			return listaUsuarios.getUsuario(rowIndex).uuid;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.fireTableDataChanged();
		System.out.println("actualizado");
	}
}
