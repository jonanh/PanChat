package panchat.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.UUID;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class ListaUsuarios extends Observable implements ListModel {

	private static final long serialVersionUID = 1L;

	private ListaCanales listaCanales;

	private LinkedList<Usuario> listaUsuarios;

	private HashMap<UUID, Usuario> hashtableUsuarios;

	private Object mutex = new Object();

	/**
	 * Crea una nueva lista de usuarios
	 * 
	 * @param listaCanales
	 */
	public ListaUsuarios(ListaCanales listaCanales) {
		this.listaCanales = listaCanales;
		this.listaUsuarios = new LinkedList<Usuario>();
		this.hashtableUsuarios = new HashMap<UUID, Usuario>();
	}

	/**
	 * Añade un usuario a la lista de usuarios
	 * 
	 * @param usuario
	 */
	public void añadirUsuario(Usuario usuario) {
		synchronized (mutex) {
			if (!contains(usuario)) {
				hashtableUsuarios.put(usuario.uuid, usuario);

				listaUsuarios.add(usuario);
				Collections.sort(listaUsuarios);
				listaCanales.anyadirUsuario(usuario);
				super.setChanged();
				super.notifyObservers();
			}
		}
	}

	/**
	 * Elimina un usuario de la lista de usuarios
	 * 
	 * @param usuario
	 */
	public void eliminarUsuario(Usuario usuario) {
		synchronized (mutex) {
			hashtableUsuarios.remove(usuario.uuid);

			listaUsuarios.remove(usuario);
			listaCanales.eliminarUsuario(usuario);
			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Devuelve una copia de la lista de usuarios
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<Usuario> getClonedListaUsuarios() {
		synchronized (mutex) {
			return (LinkedList<Usuario>) listaUsuarios.clone();
		}
	}

	/**
	 * Devuelve el usuario pedido
	 * 
	 * @param Index
	 * @return
	 */
	public Usuario getUsuario(int index) {
		return listaUsuarios.get(index);
	}

	/**
	 * Devuelve el usuario pedido
	 * 
	 * @param Index
	 * @return
	 */
	public Usuario getUsuario(UUID nombre) {
		return hashtableUsuarios.get(nombre);
	}

	/**
	 * Contiene el usuario en la lista de usuarios
	 * 
	 * @param usuario
	 * @return
	 */
	public boolean contains(Usuario usuario) {
		return hashtableUsuarios.containsKey(usuario.uuid);
	}

	/**
	 * Devuelve el número de usuarios
	 * 
	 * @return
	 */
	public int getNumUsuarios() {
		return listaUsuarios.size();
	}

	/**
	 * Devuelve la lista de usuarios
	 * 
	 * @return
	 */
	public LinkedList<Usuario> getListaUsuarios() {
		return this.listaUsuarios;
	}

	/*
	 * compareTo, equals y toString
	 */

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ListaUsuarios)
			return listaUsuarios.equals(((ListaUsuarios) obj).listaUsuarios);
		else
			return false;
	}

	@Override
	public String toString() {
		return listaUsuarios.toString();
	}

	@Override
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getElementAt(int arg0) {
		// TODO Auto-generated method stub
		return (Usuario) listaUsuarios.get(arg0);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return listaUsuarios.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}
}
