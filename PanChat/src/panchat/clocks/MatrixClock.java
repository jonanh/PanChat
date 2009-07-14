package panchat.clocks;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;

import panchat.addressing.Usuario;
import panchat.addressing.ListaUsuarios;

public class MatrixClock implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID myId;

	/*
	 * Vamos a utilizar tablas hash en vez de matrices, para minimizar la
	 * información de la MatrixClock cuando esta matrix sea dinámica, es decir
	 * que sea modificable y admita insertar y eliminar antiguos usuarios.
	 */
	private Hashtable<UUID, Hashtable<UUID, Integer>> HashMatrix;

	public MatrixClock(UUID id) {
		myId = id;

		// Lo mismo inicializar M = new int[N][N]; pero con Tablas Hash de
		// Tablash Hash.
		HashMatrix = new Hashtable<UUID, Hashtable<UUID, Integer>>();

		// Es lo equivalente en tablas hash a :
		//
		// for (int i = 0; i < N; i++)
		// ....for (int j = 0; j < N; j++)
		// ........M[i][j] = 0;
		// 
		Iterator<Usuario> iter1 = ListaUsuarios.getInstanceOf().getIterator();
		while (iter1.hasNext()) {
			UUID uuid1 = iter1.next().uuid;

			Hashtable<UUID, Integer> tabla = new Hashtable<UUID, Integer>();
			HashMatrix.put(uuid1, tabla);

			Iterator<Usuario> iter2 = ListaUsuarios.getInstanceOf().getIterator();
			while (iter2.hasNext()) {
				UUID uuid2 = iter2.next().uuid;

				// Inicializamos todos los valores a 0
				tabla.put(uuid2, 0);
			}
		}

		/*
		 * Inicializacion a 1 del reloj lógico del vector principal
		 */
		tick();
	}

	public void tick() {

		// Equivalencia a M[myId][myId]++ en tablas hash de tablash hash 0:-);

		Hashtable<UUID, Integer> tabla = HashMatrix.get(myId);

		tabla.put(myId, tabla.get(myId) + 1);
	}

	public void sendAction() {
		// include the matrix in the message
		tick();
	}

	public void receiveAction(
			Hashtable<UUID, Hashtable<UUID, Integer>> nuevaHashMatrix,
			UUID srcId) {

		/*
		 * Actualizamos los valores de los vectores no principales
		 * 
		 * Hacemos una actualización por filas
		 */
		// Es lo equivalente en tablas hash a :
		//
		// for (int i = 0; i < N; i++)
		// ...if (i != myId)
		// ......for (int j = 0; j < N; j++)
		// .........if (W[i][j] > M[i][j])
		// ............M[i][j] = W[i][j];
		//
		Iterator<Usuario> iter1 = ListaUsuarios.getInstanceOf().getIterator();
		while (iter1.hasNext()) {
			UUID uuid1 = iter1.next().uuid;

			/*
			 * Hacemos una actualización por columnas.
			 */
			if (uuid1 != myId) {
				Iterator<Usuario> iter2 = ListaUsuarios.getInstanceOf()
						.getIterator();
				while (iter2.hasNext()) {
					UUID uuid2 = iter2.next().uuid;

					/*
					 * Obtenemos los valores de ambas matrices, los comparamos y
					 * reasignamos el máximo de los 2
					 */
					Integer valor1 = HashMatrix.get(uuid1).get(uuid2);
					Integer valor2 = nuevaHashMatrix.get(uuid1).get(uuid2);
					// Solo actualizamos si hay que actualizar
					if (valor2 > valor1)
						HashMatrix.get(uuid1).put(uuid2, valor2);
				}
			}
		}

		/*
		 * Actualizamos el vector principal con los valores del vector principal
		 * del origen
		 */
		// Es lo equivalente en tablas hash a :
		// for (int j = 0; j < N; j++)
		// ....if (M[myId][j] < W[srcId][j])
		// ........M[myId][j] = W[srcId][j];
		//
		Iterator<Usuario> iter = ListaUsuarios.getInstanceOf().getIterator();
		while (iter.hasNext()) {
			UUID uuid = iter.next().uuid;

			/*
			 * Obtenemos las filas de ambas matrices, los comparamos y
			 * reasignamos el máximo de los 2
			 */
			Integer valor1 = HashMatrix.get(myId).get(uuid);
			Integer valor2 = nuevaHashMatrix.get(srcId).get(uuid);
			// Solo actualizamos si hay que actualizar
			if (valor2 > valor1)
				HashMatrix.get(myId).put(uuid, valor2);
		}

		tick();
	}

	/**
	 * Añade una nueva address a la MatrixClock
	 * 
	 * @param nuevaAddress
	 */
	public void AnyadirAddress(Usuario nuevaAddress) {

		// Primero añadimos unas columnas que falten en las filas ya existentes.
		Iterator<Usuario> iter1 = ListaUsuarios.getInstanceOf().getIterator();
		while (iter1.hasNext()) {
			UUID uuid1 = iter1.next().uuid;

			if (uuid1 != nuevaAddress.uuid) {
				HashMatrix.get(uuid1).put(nuevaAddress.uuid, 0);
			}
		}

		// Añadimos la fila entera.
		Hashtable<UUID, Integer> tabla = new Hashtable<UUID, Integer>();
		HashMatrix.put(nuevaAddress.uuid, tabla);

		Iterator<Usuario> iter2 = ListaUsuarios.getInstanceOf().getIterator();
		while (iter2.hasNext()) {
			UUID uuid2 = iter2.next().uuid;

			// Inicializamos todos los valores a 0
			tabla.put(uuid2, 0);
		}
	}

	/**
	 * Devolvemos el valor de la matrix para las posiciones de i y j.
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public int getValue(UUID i, UUID j) {
		return HashMatrix.get(i).get(j);
	}
}