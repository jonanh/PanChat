package panchat.clocks;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import panchat.data.User;

/**
 * Implementación de un vector de relojes lógicos de Lamport.
 * 
 * @author Jon Ander Hernández & Javier Mediavilla
 */
public class VectorClock implements Serializable, IClock<VectorClock> {

	private static final long serialVersionUID = 1L;

	private HashMap<User, Integer> clock;

	private User user;

	private boolean origin;

	/**
	 * Inicialización y construcción de la clase VectorClock
	 * 
	 * @param user
	 *            Identificador de nuestro proceso
	 * 
	 * @param origin
	 *            Si el vector es de origen o destino
	 * 
	 */
	public VectorClock(User user, boolean origin) {

		this.user = user;
		this.origin = origin;

		// Inicializamos la hashmap
		clock = new HashMap<User, Integer>();

		// Añadimos un primer LamportClock asociado a nuestro identificador.
		clock.put(user, 0);
	}

	/**
	 * Incrementamos el valor del vector lógico
	 */
	public void tick() {
		clock.put(user, clock.get(user) + 1);
	}

	/**
	 * Evento al enviar
	 */
	public void send(User user) {
		clock.put(user, clock.get(user) + 1);
	}

	/**
	 * Evento al recibir
	 */
	public void receiveAction(VectorClock receivedClock) {

		Entry<User, Integer> entry;

		Iterator<Entry<User, Integer>> iter = receivedClock.clock.entrySet()
				.iterator();

		// Para cada elemento del reloj recivido
		while (iter.hasNext()) {
			entry = iter.next();

			// Obtenemos el reloj asociado a la dirección procesada actualmente
			if (clock.get(entry.getKey()) != null) {
				int lClock = clock.get(entry.getKey());

				/*
				 * Si existía un reloj para dicha dirección, actualizamos el
				 * reloj de nuestro vector.
				 */
				int nClock = entry.getValue();
				if (lClock < nClock)
					clock.put(entry.getKey(), nClock);

			} else {
				/*
				 * Si no existía, añadimos el reloj a nuestro vector.
				 */
				clock.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Devuelve el valor de un elemento del vector lógico.
	 * 
	 * @param i
	 *            Identificador del proceso
	 * 
	 * @return
	 */
	public int getValue(User i) {
		return clock.get(i);
	}

	/**
	 * Añadir usuario
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		clock.put(user, 0);
	}

	/**
	 * Eliminar usuario
	 * 
	 * @param user
	 */
	public void removeUser(User user) {
		clock.remove(user);
	}

	@Override
	public String toString() {
		return "VectorClock" + clock.toString();
	}

	/**
	 * Clona el vector logico.
	 */
	@Override
	public VectorClock clone() {
		VectorClock clock = new VectorClock(user, origin);
		clock.receiveAction(this);
		return clock;
	}
}
