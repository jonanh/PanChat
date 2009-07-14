package panchat.addressing;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

public class Canal extends AbstractTableModel implements Comparable<Canal>,
		ComboBoxModel {

	private static final long serialVersionUID = 1L;

	private String nombreCanal;

	private List<Usuario> listadoUsuariosConectados;
	private List<Usuario> listadoUsuariosSinConectar;

	public Canal(String nombreCanal, List<Usuario> listadoUsuarios) {
		this.nombreCanal = nombreCanal;
		this.listadoUsuariosConectados = listadoUsuarios;
	}
	
	public boolean contains(Usuario usuario) {
		return listadoUsuariosConectados.contains(usuario);
	}

	/*
	 * Getters
	 */

	public String getNombreCanal() {
		return nombreCanal;
	}

	public List<Usuario> getListadoUsuariosConectados() {
		return listadoUsuariosConectados;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Canal) {
			Canal canal = (Canal) obj;
			return nombreCanal.equals(canal.nombreCanal);
		} else
			return false;
	}

	/**
	 * Método para añadir un usuario que ha sido recién registrado en la red.
	 * 
	 * @param usuario
	 */
	public void AnyadirUsuario(Usuario usuario) {
		listadoUsuariosSinConectar.add(usuario);
	}

	/**
	 * Método para añadir un nuevo usuario a la conversación.
	 * 
	 * @param usuario
	 */
	public void AnyadirUsuarioConectado(Usuario usuario) {
		listadoUsuariosSinConectar.remove(usuario);
		listadoUsuariosConectados.add(usuario);
	}

	/**
	 * Método para eleminiar un nuevo usuario a la conversación.
	 * 
	 * @param usuario
	 */
	public void EliminarUsuario(Usuario usuario) {
		listadoUsuariosConectados.remove(usuario);
		listadoUsuariosSinConectar.remove(usuario);
	}

	// Métodos del AbstractTableModel
	@Override
	public int compareTo(Canal o) {
		return nombreCanal.compareTo(o.nombreCanal);
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return listadoUsuariosConectados.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return rowIndex;
	}

	// Métodos del ComboBoxModel
	@Override
	public Object getSelectedItem() {
		return null;
	}

	@Override
	public void setSelectedItem(Object anItem) {
	}

	@Override
	public void addListDataListener(ListDataListener l) {
	}

	@Override
	public Object getElementAt(int index) {
		return listadoUsuariosSinConectar.get(index);
	}

	@Override
	public int getSize() {
		return listadoUsuariosSinConectar.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}

}
