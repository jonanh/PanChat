package panchat.channels.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import panchat.channels.ListaCanales;

public class ListaCanalesAbstractTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;

	private ListaCanales listaCanales;

	/**
	 * Crea una nueva clase con el modelo de datos de una lista de canales
	 * 
	 * @param listaCanales
	 */
	public ListaCanalesAbstractTableModel(ListaCanales listaCanales) {
		this.listaCanales = listaCanales;
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
		return listaCanales.getNumCanales();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return listaCanales.getCanal(rowIndex).getNombreCanal();
		default:
			return listaCanales.getCanal(rowIndex).getNumUsuariosConectados();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.fireTableDataChanged();
	}
}
