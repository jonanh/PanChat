package panchat.share;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import panchat.share.Configuracion;
import panchat.share.excepciones.FileException;
import panchat.share.protocolo.Bloque;
import panchat.share.protocolo.Fichero;

public class CacheBloques {

	/*
	 * Cerrojo para controlar los accesos a la lista de bloques.
	 */
	private Object mutex = new Object();

	/*
	 * Lista de bloques cacheados, para implementar el comportamiento LRU.
	 */
	LinkedList<Bloque> listadoBloques = new LinkedList<Bloque>();

	/*
	 * Tabla Hash de cerrojos, para controlar el intento de carga simultanea del
	 * mismo bloque, por parte de procesos diferentes.
	 */
	Map<Bloque, Object> mutexBloqueHashTable = new Hashtable<Bloque, Object>();

	/*
	 * Patr�n Singleton
	 */
	private static final CacheBloques cacheBloques = new CacheBloques();

	private CacheBloques() {
	}

	public static CacheBloques getInstance() {
		return cacheBloques;
	}

	/**
	 * 
	 * @param pFichero
	 * @param pNumBloque
	 * @return
	 */
	public Bloque getBloque(Fichero pFichero, long pNumBloque)
			throws FileException {

		/*
		 * Comprobamos si el bloque solicitado es un bloque valido/existente.
		 */
		if (pFichero.getTamanyo() <= pNumBloque * Configuracion.Bloque_TAMANYO)
			throw new FileException();

		Bloque bloque;

		/*
		 * Creamos un bloque sin datos con la informaci�n del bloque a cargar.
		 */
		bloque = new Bloque(pFichero, pNumBloque, false);

		synchronized (mutex) {

			/*
			 * Comprobamos que nadie est� cargando el bloque que vamos a cargar,
			 * mirando la tabla hash de cerrojos de bloques.
			 */
			while ((mutexBloqueHashTable.get(bloque)) != null)

				try {
					if (ConfiguracionServidor.CacheBloques_DEBUG) {
						String mensaje = "Bloque [" + pNumBloque + "] : \""
								+ pFichero + "\" siendo cargado, esperando";
						System.out.println(mensaje);
					}

					mutex.wait();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			int indice = 0;

			/*
			 * Si el bloque existe en la cache, lo reintroducimos al final de la
			 * lista implementando el comportamiento LRU, y devolvemos el
			 * bloque.
			 */
			if ((indice = listadoBloques.indexOf(bloque)) != -1) {
				bloque = listadoBloques.remove(indice);
				listadoBloques.add(bloque);

				if (ConfiguracionServidor.CacheBloques_DEBUG) {
					String mensaje = "Actualizando lista LRU, y devolviendo Bloque ["
							+ pNumBloque + "] : \"" + pFichero + "\"";
					System.out.println(mensaje);
				}

				return bloque;
			}

			/*
			 * Como el bloque no estaba en la cache, debemos CARGAR el bloque y
			 * por lo tanto introducimos un cerrojo en la tabla hash de bloques
			 * de modo que si alguien m�s intenta cargarlo se bloquee.
			 */
			mutexBloqueHashTable.put(bloque, new Object());

			if (ConfiguracionServidor.CacheBloques_DEBUG) {
				String mensaje = "Cargando Bloque [" + pNumBloque + "] : \""
						+ pFichero + "\"";
				System.out.println(mensaje);
			}
		}

		/*
		 * Cargamos el bloque fuera del cerrojo.
		 */
		bloque = new Bloque(pFichero, pNumBloque, true);

		if (ConfiguracionServidor.CacheBloques_SLEEP)
			try {

				Thread.sleep(1000);

			} catch (Exception e) {
			}

		synchronized (mutex) {
			/*
			 * Eliminamos el cerrojo de bloque de la tabla hash de cerrojo de
			 * bloques.
			 */
			mutex.notifyAll();

			mutexBloqueHashTable.remove(bloque);

			/*
			 * Si la cache esta llena, eliminamos el elemento m�s antiguo.
			 */
			if (listadoBloques.size() > ConfiguracionServidor.CacheBloques_TAMANYO_CACHE)
				listadoBloques.poll();

			/*
			 * Introducimos el nuevo elemento.
			 */
			listadoBloques.add(bloque);

			if (ConfiguracionServidor.CacheBloques_DEBUG) {
				String mensaje = "Cargado Bloque [" + pNumBloque + "] : \""
						+ pFichero + "\" en la cache de bloques";
				System.out.println(mensaje);
			}

			return bloque;
		}
	}

	/*
	 * Comprobamos el correcto funcionamiento de la cache de bloques.
	 */
	public static void main(String[] args) throws IOException {

		final Fichero fichero = new Fichero("data/Chimaera.jpg");

		Runnable A = new Runnable() {

			@Override
			public void run() {
				try {

					CacheBloques.getInstance().getBloque(fichero, 0);

				} catch (FileException e) {
					e.printStackTrace();
				}

			}
		};

		Runnable B = new Runnable() {

			@Override
			public void run() {
				try {

					CacheBloques.getInstance().getBloque(fichero, 3);

				} catch (FileException e) {
					e.printStackTrace();
				}

			}
		};

		new Thread(A).start();
		new Thread(B).start();
		new Thread(A).start();
		new Thread(B).start();
		new Thread(A).start();
		new Thread(B).start();
		new Thread(A).start();
		new Thread(B).start();
	}
}
