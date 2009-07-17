package panchat.channels;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Observable;

import panchat.users.Usuario;

public class ListaCanales extends Observable {

	private static final long serialVersionUID = 1L;

	private LinkedList<Canal> listaCanales;

	private Object mutex = new Object();

	/**
	 * Nueva lista de canales
	 */
	public ListaCanales() {
		listaCanales = new LinkedList<Canal>();
	}

	/**
	 * Registra un nuevo canal en la lista de canales
	 * 
	 * @param canal
	 */
	public void añadirCanal(Canal canal) {
		synchronized (mutex) {
			listaCanales.add(canal);
			Collections.sort(listaCanales);
			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Añade el nuevo usuario a todos los canales como nuevo usuario
	 * desconectado
	 * 
	 * @param usuario
	 */
	public void anyadirUsuario(Usuario usuario) {
		synchronized (mutex) {
			for (Canal canal : listaCanales)
				canal.anyadirUsuario(usuario);

			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Elimina el usuario de todos los canales
	 * 
	 * @param usuario
	 */
	public void eliminarUsuario(Usuario usuario) {
		synchronized (mutex) {
			for (Canal canal : listaCanales)
				canal.eliminarUsuario(usuario);

			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Devuelve el canal según la posición index
	 * 
	 * @param index
	 * @return
	 */
	public Canal getCanal(int index) {
		return listaCanales.get(index);
	}

	/**
	 * Devuelve el número de canales que posee la lista de canales
	 * 
	 * @return
	 */
	public int getNumCanales() {
		return listaCanales.size();
	}

	/*
	 * Equals
	 */

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ListaCanales)
			return listaCanales.equals(((ListaCanales) obj).listaCanales);
		else
			return false;
	}
}
