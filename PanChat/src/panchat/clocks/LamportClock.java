package panchat.clocks;

/**
 * Implementación de un reloj lógico de Lamport.
 * 
 */
public class LamportClock {
	private int c;

	/**
	 * Inicialización.
	 * 
	 * El reloj se iniciará a 1.
	 */
	public LamportClock() {
		c = 1;
	}

	/**
	 * @return devuelve el valor del reloj lógico.
	 */
	public int getValue() {
		return c;
	}

	/**
	 * Incrementamos el valor del reloj lógico
	 */
	public void tick() {
		c += 1;
	}

	/**
	 * Evento al enviar
	 */
	public void send() {
		tick();
	}

	/**
	 * Evento al recibir
	 */
	public void receiveAction(LamportClock receivedClock) {
		/*
		 * Actualizamos el reloj lógico asignando el máximo valor entre el valor
		 * de este reloj y el reloj recibido.
		 */
		if (c < receivedClock.c)
			c = receivedClock.c;

		tick();
	}
}