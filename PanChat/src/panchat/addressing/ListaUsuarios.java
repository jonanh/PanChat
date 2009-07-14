package panchat.addressing;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public class ListaUsuarios extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static ListaUsuarios conexiones = new ListaUsuarios();

	private LinkedList<Usuario> listaConexiones;

	private ListaUsuarios() {
		listaConexiones = new LinkedList<Usuario>();
	}

	public static ListaUsuarios getInstanceOf() {
		return conexiones;
	}

	public Iterator<Usuario> getIterator() {
		return listaConexiones.iterator();
	}

	public void añadirUsuario(Usuario address) {
		listaConexiones.add(address);
		Collections.sort(listaConexiones);
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
		return listaConexiones.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return listaConexiones.get(rowIndex).nickName;
		case 1:
			return listaConexiones.get(rowIndex).ip;
		case 2:
			return listaConexiones.get(rowIndex).port;
		default:
			return listaConexiones.get(rowIndex).uuid;
		}
	}

}
