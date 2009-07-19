package panchat.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

public class ListaCanales extends Observable {

	private static final long serialVersionUID = 1L;

	private LinkedList<Canal> listaCanales;

	private HashMap<String, Canal> hashtableCanales;

	private Object mutex = new Object();

	/**
	 * Nueva lista de canales
	 */
	public ListaCanales() {
		listaCanales = new LinkedList<Canal>();
		hashtableCanales = new HashMap<String, Canal>();
	}

	/**
	 * Registra un nuevo canal en la lista de canales
	 * 
	 * @param canal
	 */
	public void añadirCanal(Canal canal) {
		synchronized (mutex) {
			if (!hashtableCanales.containsKey(canal.getNombreCanal())) {

				hashtableCanales.put(canal.getNombreCanal(), canal);

				listaCanales.add(canal);
				Collections.sort(listaCanales);

				super.setChanged();
				super.notifyObservers();
			}
		}
	}

	/**
	 * Registra un nuevo canal en la lista de canales
	 * 
	 * @param canal
	 */
	public void eliminarCanal(Canal canal) {
		synchronized (mutex) {
			if (hashtableCanales.containsKey(canal.getNombreCanal())) {

				hashtableCanales.remove(canal.getNombreCanal());

				listaCanales.remove(canal);

				super.setChanged();
				super.notifyObservers();
			}
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
		// Comprobar límites
		if (index >= 0 && index < getNumCanales())
			return listaCanales.get(index);
		else
			return null;
	}

	/**
	 * Devuelve el número de canales que posee la lista de canales
	 * 
	 * @return
	 */
	public int getNumCanales() {
		return listaCanales.size();
	}

	/**
	 * Devuelve el canal según la posición index
	 * 
	 * @param index
	 * @return
	 */
	public Canal getCanal(String nombre) {
		return this.hashtableCanales.get(nombre);
	}

	/**
	 * La información de uno de los canales ha sido modificada, actualizar la
	 * vista de canales
	 */
	public void canalModificado() {
		super.setChanged();
		super.notifyObservers();
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
