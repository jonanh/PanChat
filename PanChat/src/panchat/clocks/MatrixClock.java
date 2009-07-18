package panchat.clocks;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import panchat.data.Usuario;

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

		this.myId = id;

		// Lo mismo inicializar M = new int[N][N]; pero con Tablas Hash de
		// Tablash Hash.
		HashMatrix = new Hashtable<UUID, Hashtable<UUID, Integer>>();

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
		Iterator<Entry<UUID, Hashtable<UUID, Integer>>> iter = HashMatrix
				.entrySet().iterator();
		while (iter.hasNext()) {
			UUID uuid1 = iter.next().getKey();

			/*
			 * Hacemos una actualización por columnas.
			 */
			if (uuid1 != myId) {
				Iterator<Entry<UUID, Hashtable<UUID, Integer>>> iter2 = HashMatrix
						.entrySet().iterator();
				while (iter2.hasNext()) {
					UUID uuid2 = iter2.next().getKey();

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
		iter = HashMatrix.entrySet().iterator();
		while (iter.hasNext()) {
			UUID uuid = iter.next().getKey();

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
	 * Añade un nuevo usuario a la MatrixClock
	 * 
	 * @param nuevoUsuario
	 */
	public void anyadirUsuario(Usuario nuevoUsuario) {

		// Primero añadimos unas columnas que falten en las filas ya existentes.
		Iterator<Entry<UUID, Hashtable<UUID, Integer>>> iter = HashMatrix
				.entrySet().iterator();
		while (iter.hasNext()) {
			UUID uuid = iter.next().getKey();

			HashMatrix.get(uuid).put(nuevoUsuario.uuid, 0);
		}

		// Añadimos la fila entera.
		Hashtable<UUID, Integer> tabla = new Hashtable<UUID, Integer>();
		HashMatrix.put(nuevoUsuario.uuid, tabla);

		iter = HashMatrix.entrySet().iterator();
		while (iter.hasNext()) {
			UUID uuid = iter.next().getKey();

			// Inicializamos todos los valores a 0
			tabla.put(uuid, 0);
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