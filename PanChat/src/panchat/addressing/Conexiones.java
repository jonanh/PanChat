package panchat.addressing;

import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public class Conexiones extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static Conexiones conexiones = new Conexiones();

	private LinkedList<Address> listaConexiones;

	private Conexiones() {
		listaConexiones = new LinkedList<Address>();
	}

	public static Conexiones getInstanceOf() {
		return conexiones;
	}

	public Iterator<Address> getIterator() {
		return listaConexiones.iterator();
	}

	public void añadirElemento(Address address) {
		listaConexiones.add(address);
	}

	/*
	 * Métodos del AbstractTableModel
	 */
	
	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return listaConexiones.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return listaConexiones.get(rowIndex).nickName;
	}
	
	
}
