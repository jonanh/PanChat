package panchat.share;

import java.util.Collections;
import java.util.LinkedList;

public class TimeoutThread extends Thread {

	private Object mutex = new Object();

	private static TimeoutThread timeOutThread = new TimeoutThread();

	private LinkedList<Trabajo> listaPlazos = new LinkedList<Trabajo>();

	private TimeoutThread() {
	}

	public static TimeoutThread getInstance() {
		return timeOutThread;
	}

	@Override
	public void run() {
		super.run();

		while (true) {
			synchronized (mutex) {
				/*
				 * Esperamos mientras no haya conexiones, o mientras nos hayamos
				 * despertado antes de tiempo.
				 */
				while (listaPlazos.isEmpty()
						|| System.currentTimeMillis() < listaPlazos.peek().plazo)
					try {
						if (listaPlazos.isEmpty())
							mutex.wait();
						else {
							mutex.wait(listaPlazos.peek().plazo
									- System.currentTimeMillis());
						}
					} catch (InterruptedException e) {
					}

				Trabajo trabajo = listaPlazos.poll();

				if (Configuracion.TimeoutThread_DEBUG) {
					String mensaje = "TimeoutThread.class : Plazo superado del trabajo : "
							+ trabajo;
					System.out.println(mensaje);
				}
				trabajo.cancelarTrabajo();
			}
		}
	}

	public void anyadirTrabajo(Trabajo pTrabajo) {
		synchronized (mutex) {
			if (Configuracion.TimeoutThread_DEBUG) {
				String mensaje = "TimeoutThread.class : Trabajo anyadido : "
						+ pTrabajo;
				System.out.println(mensaje);
			}
			/*
			 * A�adimos el elemento a la cola.
			 */
			listaPlazos.add(pTrabajo);

			/*
			 * Ordenamos la lista.
			 */
			Collections.sort(listaPlazos);

			/*
			 * Notificamos para reajustar el despertar.
			 */
			mutex.notifyAll();
		}
	}

	public void eliminarTrabajo(Trabajo pTrabajo) {
		synchronized (mutex) {
			if (Configuracion.TimeoutThread_DEBUG) {
				String mensaje = "TimeoutThread.class : Trabajo eliminado : "
						+ pTrabajo;
				System.out.println(mensaje);
			}
			listaPlazos.remove(pTrabajo);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		/*
		 * Tests
		 */

		class PruebaTrabajo extends Trabajo {

			@Override
			public void cancelarTrabajo() {
			}

			@Override
			public long getTrabajoTimeOut() {
				return 5000;
			}
		}

		TimeoutThread timeoutThread = new TimeoutThread();

		/*
		 * Creamos el escenario para la prueba
		 */
		PruebaTrabajo[] trabajo = new PruebaTrabajo[3];

		for (int i = 0; i < 3; i++) {
			Thread.sleep(500);

			trabajo[i] = new PruebaTrabajo();

			System.out.println("plazo : " + (4000 + i * 500) + " trabajo : "
					+ trabajo[i].hashCode());

			timeoutThread.anyadirTrabajo(trabajo[i]);
		}

		/*
		 * Mostramos el comportamiento al eliminar tareas
		 */
		System.out.println(timeoutThread.listaPlazos);

		System.out.println("Eliminamos la tercera conexion");
		timeoutThread.eliminarTrabajo(trabajo[2]);

		System.out.println(timeoutThread.listaPlazos);

		System.out.println("Eliminamos la segunda conexion");
		timeoutThread.eliminarTrabajo(trabajo[1]);

		System.out.println(timeoutThread.listaPlazos);
	}
}
