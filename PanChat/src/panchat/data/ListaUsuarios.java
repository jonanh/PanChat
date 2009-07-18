package panchat.data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Observable;


public class ListaUsuarios extends Observable {

	private static final long serialVersionUID = 1L;

	private LinkedList<Usuario> listaUsuarios;
	private ListaCanales listaCanales;

	private Object mutex = new Object();

	/**
	 * Crea una nueva lista de usuarios
	 * 
	 * @param listaCanales
	 */
	public ListaUsuarios(ListaCanales listaCanales) {
		this.listaCanales = listaCanales;
		this.listaUsuarios = new LinkedList<Usuario>();
	}

	/**
	 * Añade un usuario a la lista de usuarios
	 * 
	 * @param usuario
	 */
	public void añadirUsuario(Usuario usuario) {
		synchronized (mutex) {
			if (!contains(usuario)) {
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
	 * Contiene el usuario en la lista de usuarios
	 * 
	 * @param usuario
	 * @return
	 */
	public boolean contains(Usuario usuario) {
		return listaUsuarios.contains(usuario);
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
	 * Nos indica si somos el usuario con el UUID más bajo registrado
	 * 
	 * @param usuario
	 * @return
	 */
	public boolean soyElPrimero(Usuario usuario) {
		return listaUsuarios.get(0).equals(usuario);
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
}
