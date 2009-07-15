package panchat.addressing;

import java.util.LinkedList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

public class Canal extends AbstractTableModel implements Comparable<Canal>,
		ComboBoxModel {

	private static final long serialVersionUID = 1L;

	private String nombreCanal;

	private LinkedList<Usuario> listadoUsuariosConectados;
	private ListaUsuarios listadoUsuarios;

	public Canal(String nombreCanal, ListaUsuarios listadoUsuarios) {
		this.nombreCanal = nombreCanal;
		this.listadoUsuariosConectados = new LinkedList<Usuario>();
		this.listadoUsuarios = listadoUsuarios;
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
	 * Método para añadir un nuevo usuario a la conversación.
	 * 
	 * @param usuario
	 */
	public void anyadirUsuarioConectado(Usuario usuario) {
		listadoUsuariosConectados.add(usuario);
	}

	/**
	 * Método para eleminiar un nuevo usuario a la conversación.
	 * 
	 * @param usuario
	 */
	public void EliminarUsuario(Usuario usuario) {
		listadoUsuariosConectados.remove(usuario);
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
		return listadoUsuariosConectados.get(rowIndex).nickName;
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
		LinkedList<Usuario> usuariosSinConectar;
		usuariosSinConectar = listadoUsuarios.diferenciaUsuarios(listadoUsuariosConectados);
		return usuariosSinConectar.get(index).nickName;
	}

	@Override
	public int getSize() {
		return listadoUsuarios.getLenght() - listadoUsuariosConectados.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}

}
