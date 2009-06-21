package zerconf.clock;

public class LamportClock {
	private int c;

	/**
	 * Inicializaci—n.
	 * 
	 * El reloj se iniciar‡ a 1.
	 */
	public LamportClock() {
		c = 1;
	}

	/**
	 * @return devuelve el valor del reloj l—gico.
	 */
	public int getValue() {
		return c;
	}

	/**
	 * Incrementamos el reloj l—gico
	 */
	public void tick() { // on internal events
		c = c + 1;
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
	public void receiveAction(int src, int sentValue) {
		c = (src > sentValue ? src : sentValue) + 1;
	}

	public void receiveAction(LamportClock receivedLamportClock) {
		/*
		 * Actualizamos el reloj l—gico asignando el m‡ximo valor entre el valor
		 * de este reloj y el reloj recibido.
		 */
		if (c < receivedLamportClock.c)
			c = receivedLamportClock.c;

	}
}