package panchat.addressing;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public class ListaUsuarios extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private LinkedList<Usuario> listaUsuarios;

	public ListaUsuarios() {
		listaUsuarios = new LinkedList<Usuario>();
	}

	public Iterator<Usuario> getIterator() {
		return listaUsuarios.iterator();
	}

	public void añadirUsuario(Usuario address) {
		listaUsuarios.add(address);
		Collections.sort(listaUsuarios);
	}

	/*
	 * Métodos del AbstractTableModel
	 */
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
		return listaUsuarios.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return listaUsuarios.get(rowIndex).nickName;
		case 1:
			return listaUsuarios.get(rowIndex).ip;
		case 2:
			return listaUsuarios.get(rowIndex).port;
		default:
			return listaUsuarios.get(rowIndex).uuid;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ListaUsuarios)
			return listaUsuarios.equals(((ListaUsuarios) obj).listaUsuarios);
		else
			return false;
	}

}
