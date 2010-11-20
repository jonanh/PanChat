package panchat.linker;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
import java.util.Map.Entry;

import panchat.data.User;

public class CausalMatrix implements Serializable {

	private static final long serialVersionUID = 1L;

	private final boolean DEBUG = false;

	private User usuario;

	private UUID myId;

	/*
	 * Vamos a utilizar tablas hash en vez de matrices, para minimizar la
	 * información de la MatrixClock cuando esta matrix sea dinámica, es decir
	 * que sea modificable y admita insertar y eliminar antiguos usuarios.
	 */
	Hashtable<UUID, Hashtable<UUID, Integer>> HashMatrix;

	public CausalMatrix(User usuario) {

		this.usuario = usuario;

		this.myId = usuario.uuid;

		HashMatrix = new Hashtable<UUID, Hashtable<UUID, Integer>>();

		anyadirUsuario(usuario);
	}

	/**
	 * Actualizamos los valores obteniendo los mayores valores de ámbas matrices
	 * 
	 * @param nuevaHashMatrix
	 */
	public void maxMatrix(CausalMatrix causalMatrix) {

		printDebug("matrix local");
		printMatrix();

		printDebug("matrix remoto");
		printMatrix();

		Hashtable<UUID, Hashtable<UUID, Integer>> nuevaHashMatrix = causalMatrix.HashMatrix;

		// Es lo equivalente en tablas hash a :
		//
		// for (int i = 0; i < N; i++)
		// ......for (int j = 0; j < N; j++)
		// .........if (W[i][j] > M[i][j])
		// ............M[i][j] = W[i][j];
		// 
		Iterator<Entry<UUID, Hashtable<UUID, Integer>>> iter = nuevaHashMatrix
				.entrySet().iterator();
		while (iter.hasNext()) {
			UUID uuid1 = iter.next().getKey();

			/*
			 * Si existe la fila en nuestra matrix, entonces tenemos que
			 * actualizar cada casilla de la fila
			 */
			if (HashMatrix.containsKey(uuid1)) {

				/*
				 * Hacemos una actualización por columnas.
				 */
				Iterator<Entry<UUID, Hashtable<UUID, Integer>>> iter2 = nuevaHashMatrix
						.entrySet().iterator();
				while (iter2.hasNext()) {
					UUID uuid2 = iter2.next().getKey();

					/*
					 * Si existe la columna en nuestra matrix, escogemos el
					 * valor más entre la columna nueva y la nuestra.
					 */
					if (HashMatrix.get(uuid1).containsKey(uuid2)) {

						/*
						 * Obtenemos los valores de ambas matrices, los
						 * comparamos y reasignamos el máximo de los 2
						 */
						Integer valor1 = HashMatrix.get(uuid1).get(uuid2);
						Integer valor2 = nuevaHashMatrix.get(uuid1).get(uuid2);

						// Solo actualizamos si hay que actualizar
						if (valor2 > valor1)
							HashMatrix.get(uuid1).put(uuid2, valor2);

					}
					/*
					 * Como esta fila no existe en nuestra matrix, simplemente
					 * ponemos el valor en nuestra matrix
					 */
					else {

						Integer valor2 = nuevaHashMatrix.get(uuid1).get(uuid2);
						HashMatrix.get(uuid1).put(uuid2, valor2);

					}
				}

			}
			/*
			 * No existe la fila en nuestra matrix, con lo cúal añadimos toda la
			 * fila de la matrix nueva, e incluimos nuestro identificador a 0.
			 */
			else {

				HashMatrix.put(uuid1, nuevaHashMatrix.get(uuid1));

				HashMatrix.get(uuid1).put(usuario.uuid, 0);
			}
		}

		printDebug("resultado");

		printMatrix();
	}

	/**
	 * Añade un nuevo usuario a la MatrixClock
	 * 
	 * @param nuevoUsuario
	 */
	public void anyadirUsuario(User nuevoUsuario) {

		if (DEBUG) {
			System.out
					.println("Usuario me : " + this.usuario
							+ "\n\tUsuario añadido :"
							+ nuevoUsuario.toStringComplete());
		}

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
	 * Autoincrementamos el valor M[myId][destId]++ de la matrix indicando que
	 * hemos enviado un mensaje a myId.
	 * 
	 * @param usuario
	 */
	public void incrementarDestino(User usuario) {
		// M[myId][destId]++;

		if (DEBUG)
			printMatrix();

		// Obtenemos la tabla referente a myId
		Hashtable<UUID, Integer> tabla = HashMatrix.get(myId);

		// Actualizamos el valor referente a destId
		tabla.put(usuario.uuid, tabla.get(usuario.uuid) + 1);
	}

	/**
	 * Autoincrementamos el valor M[myId][destId]++ de la matrix indicando que
	 * hemos enviado un mensaje a myId.
	 * 
	 * @param user
	 */
	public void incrementarDestino(LinkedList<User> listaUsuarios) {
		// for (int i = 0; i < destIds.size(); i++)
		// ....M[myId][destIds.getEntry(i)]++;

		// Obtenemos la tabla referente a myId
		Hashtable<UUID, Integer> tabla = HashMatrix.get(myId);

		for (User usuario : listaUsuarios)
			// Actualizamos el valor referente a destId
			tabla.put(usuario.uuid, tabla.get(usuario.uuid) + 1);
	}

	/**
	 * Devolvemos el valor de la matrix para las posiciones de i y j.
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public int getValue(UUID i, UUID j) {
		try {

			return HashMatrix.get(i).get(j);

		} catch (Exception e) {

			return 0;

		}
	}

	public void printMatrix() {
		if (DEBUG) {
			System.out.println("Volcado matrix hash");
			Iterator<Entry<UUID, Hashtable<UUID, Integer>>> iter = HashMatrix
					.entrySet().iterator();
			iter = HashMatrix.entrySet().iterator();
			while (iter.hasNext()) {
				UUID uuid = iter.next().getKey();

				Iterator<Entry<UUID, Integer>> iter2 = HashMatrix.get(uuid)
						.entrySet().iterator();
				iter2 = HashMatrix.get(uuid).entrySet().iterator();
				while (iter2.hasNext()) {
					UUID uuid2 = iter2.next().getKey();

					System.out.print("\t" + HashMatrix.get(uuid).get(uuid2));
				}
				System.out.println("");
			}
		}
	}

	private void printDebug(String string) {
		String msgClase = "CausalLinkerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}