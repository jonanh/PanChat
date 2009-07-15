package panchat.addressing;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public class ListaCanales extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private LinkedList<Canal> listaCanales;

	public ListaCanales() {
		listaCanales = new LinkedList<Canal>();
	}

	public Iterator<Canal> getIterator() {
		return listaCanales.iterator();
	}

	public void añadirCanal(Canal canal) {
		listaCanales.add(canal);
		Collections.sort(listaCanales);
	}

	public void eliminarUsuario(Usuario usuario) {
		for (Canal canal : listaCanales)
			canal.eliminarUsuario(usuario);
	}

	public boolean contains(Canal canal) {
		return listaCanales.contains(canal);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ListaCanales)
			return listaCanales.equals(((ListaCanales) obj).listaCanales);
		else
			return false;
	}

	/*
	 * Métodos del AbstractTableModel
	 */

	// Por si quisieramos hacer los nombres de los canales editables 0:-)
	// @Override
	// public boolean isCellEditable(int row, int col) {
	// return col == 0;
	// }
	//
	// @Override
	// public void setValueAt(Object value, int rowIndex, int columnIndex) {
	// if (columnIndex == 0) {
	// listaCanales.get(rowIndex).nombreCanal = (String) value;
	// }
	// super.setValueAt(value, rowIndex, columnIndex);
	// }
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
		return listaCanales.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return listaCanales.get(rowIndex).getNombreCanal();
		default:
			return listaCanales.get(rowIndex).getListadoUsuariosConectados()
					.size();
		}
	}
}
