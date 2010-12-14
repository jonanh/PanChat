package panchat.clocks;

import panchat.data.User;

/**
 * Implementación de un reloj lógico de Lamport.
 * 
 */
public class LamportClock implements IClock<LamportClock> {

	private int c;

	/**
	 * Inicialización.
	 * 
	 * El reloj se iniciará a 1.
	 */
	public LamportClock() {
		c = 0;
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
	@Override
	public void send(User user) {
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
	}

	@Override
	public String toString() {
		return String.valueOf(c);
	}

	/**
	 * Clona el reloj logico.
	 */
	@Override
	public LamportClock clone() {
		LamportClock clock = new LamportClock();
		clock.c = this.c;
		return clock;
	}
}