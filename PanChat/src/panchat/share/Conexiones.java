package panchat.share;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import panchat.share.Conexion;
import panchat.share.TimeoutThread;

public class Conexiones {

	final static boolean DEBUG = true;
	final static int PRIORIDADES = 10;

	/*
	 * Colas de conexiones
	 */
	ArrayList<Queue<Conexion>> listaConexionesPendientes;
	Map<Conexion, Conexion> listaConexionesEnCurso;

	/*
	 * Cerrojo
	 */
	private static Object mutex = new Object();

	/*
	 * Variable para el patron Singleton.
	 */
	private static Conexiones listaConexiones = new Conexiones();

	/**
	 * 
	 */
	private Conexiones() {

		/*
		 * Construimos el hilo timeout y lo arrancamos
		 * 
		 * Y garantizamos la correcci�n del resto de Singletons
		 */
		TimeoutThread.getInstance().start();

		CacheBloques.getInstance();

		/*
		 * Inicializaci�n de las colas.
		 */
		listaConexionesPendientes = new ArrayList<Queue<Conexion>>(PRIORIDADES);

		for (int i = 0; i < PRIORIDADES; i++)
			listaConexionesPendientes.add(new LinkedList<Conexion>());

		listaConexionesEnCurso = new HashMap<Conexion, Conexion>();
	}

	/**
	 * 
	 * @return ListaConexiones
	 */
	public static Conexiones getInstance() {
		return listaConexiones;
	}

	/**
	 * 
	 * @param conexion
	 * @return Socket
	 */
	public Conexion getConexion() {

		synchronized (mutex) {

			while (listaConexionesPendientes.get(0).size() == 0)
				try {
					mutex.wait();
				} catch (InterruptedException e) {
				}

			/*
			 * Obtenemos el thread m�s prioritario
			 * 
			 * FIXME : sin prioridades actualmente
			 */
			Conexion conexion = listaConexionesPendientes.get(0).poll();

			/*
			 * La a�adimos a la tabla hash de conexiones.
			 */
			listaConexionesEnCurso.put(conexion, conexion);

			return conexion;
		}
	}

	/**
	 * Registramos una nueva peticion de conexion.
	 * 
	 * @param socket
	 */
	public void peticionConexion(Socket pSocket) {
		synchronized (mutex) {

			if (DEBUG) {
				String mensaje = "Conexiones.class : Peticion de conexion realizada : "
						+ pSocket;
				System.out.println(mensaje);
			}

			/*
			 * Creamos la conexion.
			 */
			Conexion conexion = new Conexion(pSocket);

			listaConexionesPendientes.get(0).add(conexion);

			TimeoutThread.getInstance().anyadirTrabajo(conexion);
			mutex.notifyAll();
		}
	}

	/**
	 * Eliminaci�n el socket pasado por par�metro del listado de conexiones en
	 * curso.
	 * 
	 * @param socket
	 */
	public void eliminarConexion(Conexion conexion, boolean terminacionNormal) {
		synchronized (mutex) {

			if (DEBUG)
				System.out.println("Conexiones.class : eliminarConexion "
						+ conexion);

			/*
			 * Si existe la eliminamos de la lista de Conexiones en curso y del
			 * hilo de tratamiento de timeouts.
			 */
			if (listaConexionesEnCurso.containsKey(conexion)) {

				listaConexionesEnCurso.remove(conexion);

				if (terminacionNormal)
					TimeoutThread.getInstance().eliminarTrabajo(conexion);
			} else {

				/*
				 * Todavia esta encolada, luego la buscamos en la cola de
				 * pendientes y lo eliminamos.
				 */
				for (Queue<Conexion> conexionCola : listaConexionesPendientes) {
					if (conexionCola.equals(conexion))
						listaConexionesPendientes.remove(conexionCola);
				}
			}
		}
	}
}
