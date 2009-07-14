package panchat.clocks;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import panchat.addressing.Usuario;

/**
 * Implementación de un vector de relojes lógicos de Lamport.
 * 
 * @author Jon Ander Hernández & Javier Mediavilla
 */
public class VectorClock implements Serializable {

	private static final long serialVersionUID = 1L;

	private HashMap<Usuario, LamportClock> clock;

	private Usuario myId;

	/**
	 * Inicialización y construcción de la clase VectorClock
	 * 
	 * @param numProc
	 *            Número de procesos en el vector
	 * @param id
	 *            Identificador de nuestro proceso
	 */
	public VectorClock(int numProc, Usuario id) {

		myId = id;

		// Inicializamos la hashmap
		clock = new HashMap<Usuario, LamportClock>();

		// Añadimos un primer LamportClock asociado a nuestro identificador.
		clock.put(myId, new LamportClock());

		// Lo autoincrementamos para inicializarlo a 1.
		clock.get(id).tick();
	}

	/**
	 * Incrementamos el valor del vector lógico
	 */
	public void tick() {
		clock.get(myId).tick();
	}

	/**
	 * Evento al enviar
	 */
	public void sendAction() {
		tick();
	}

	/**
	 * Evento al recibir
	 */
	public void receiveAction(VectorClock receivedClock) {

		Entry<Usuario, LamportClock> entry;

		Iterator<Entry<Usuario, LamportClock>> iter = receivedClock.clock
				.entrySet().iterator();

		// Para cada elemento del reloj recivido
		while (iter.hasNext()) {
			entry = iter.next();

			// Obtenemos el reloj asociado a la dirección procesada actualmente
			LamportClock lClock = clock.get(entry.getKey());

			// Comprobamos si existe esta direccion en nuestro vector
			if (lClock != null)
				/*
				 * Si existía un reloj para dicha dirección, actualizamos el
				 * reloj de nuestro vector.
				 */
				lClock.receiveAction(entry.getValue());
			else
				/*
				 * Si no existía, añadimos el reloj a nuestro vector.
				 */
				clock.put(entry.getKey(), entry.getValue());

		}

		// Autoincrementamos el valor de nuestro reloj.
		tick();
	}

	/**
	 * Devuelve el valor de un elemento del vector lógico.
	 * 
	 * @param i
	 *            Identificador del proceso
	 * 
	 * @return
	 */
	public int getValue(int i) {
		return clock.get(i).getValue();
	}
}
