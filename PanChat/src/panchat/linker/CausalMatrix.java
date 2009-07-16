package panchat.linker;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import panchat.addressing.users.ListaUsuarios;
import panchat.addressing.users.Usuario;

public class CausalMatrix implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID myId;
	private ListaUsuarios listaUsuarios;

	/*
	 * Vamos a utilizar tablas hash en vez de matrices, para minimizar la
	 * información de la MatrixClock cuando esta matrix sea dinámica, es decir
	 * que sea modificable y admita insertar y eliminar antiguos usuarios.
	 */
	private Hashtable<UUID, Hashtable<UUID, Integer>> HashMatrix;

	public CausalMatrix(UUID id, ListaUsuarios listaUsuarios) {

		this.myId = id;
		this.listaUsuarios = listaUsuarios;

		// Lo mismo inicializar M = new int[N][N]; pero con Tablas Hash de
		// Tablash Hash.
		HashMatrix = new Hashtable<UUID, Hashtable<UUID, Integer>>();

		// Es lo equivalente en tablas hash a :
		//
		// for (int i = 0; i < N; i++)
		// ....for (int j = 0; j < N; j++)
		// ........M[i][j] = 0;
		// 
		Iterator<Usuario> iter1 = listaUsuarios.getIterator();
		while (iter1.hasNext()) {
			UUID uuid1 = iter1.next().uuid;

			Hashtable<UUID, Integer> tabla = new Hashtable<UUID, Integer>();
			HashMatrix.put(uuid1, tabla);

			Iterator<Usuario> iter2 = listaUsuarios.getIterator();
			while (iter2.hasNext()) {
				UUID uuid2 = iter2.next().uuid;

				// Inicializamos todos los valores a 0
				tabla.put(uuid2, 0);
			}
		}
	}

	/**
	 * Actualizamos los valores obteniendo los mayores valores de ámbas matrices
	 * 
	 * @param nuevaHashMatrix
	 */
	public void maxMatrix(CausalMatrix causalMatrix) {

		Hashtable<UUID, Hashtable<UUID, Integer>> nuevaHashMatrix = causalMatrix.HashMatrix;

		/*
		 * Actualizamos los valores de los vectores no principales
		 * 
		 * Hacemos una actualización por filas
		 */
		// Es lo equivalente en tablas hash a :
		//
		// for (int i = 0; i < N; i++)
		// ......for (int j = 0; j < N; j++)
		// .........if (W[i][j] > M[i][j])
		// ............M[i][j] = W[i][j];
		//
		Iterator<Usuario> iter1 = listaUsuarios.getIterator();
		while (iter1.hasNext()) {
			UUID uuid1 = iter1.next().uuid;

			/*
			 * Hacemos una actualización por columnas.
			 */
			Iterator<Usuario> iter2 = listaUsuarios.getIterator();
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

	/**
	 * Añade una nueva address a la MatrixClock
	 * 
	 * @param nuevaAddress
	 */
	public void anyadirAddress(Usuario nuevaAddress) {

		// Primero añadimos unas columnas que falten en las filas ya existentes.
		Iterator<Usuario> iter1 = listaUsuarios.getIterator();
		while (iter1.hasNext()) {
			UUID uuid1 = iter1.next().uuid;

			if (uuid1 != nuevaAddress.uuid) {
				HashMatrix.get(uuid1).put(nuevaAddress.uuid, 0);
			}
		}

		// Añadimos la fila entera.
		Hashtable<UUID, Integer> tabla = new Hashtable<UUID, Integer>();
		HashMatrix.put(nuevaAddress.uuid, tabla);

		Iterator<Usuario> iter2 = listaUsuarios.getIterator();
		while (iter2.hasNext()) {
			UUID uuid2 = iter2.next().uuid;

			// Inicializamos todos los valores a 0
			tabla.put(uuid2, 0);
		}
	}

	/**
	 * Autoincrementamos el valor M[myId][destId]++ de la matrix indicando que
	 * hemos enviado un mensaje a myId.
	 * 
	 * @param usuario
	 */
	public void incrementarDestino(Usuario usuario) {
		// M[myId][destId]++;

		// Obtenemos la tabla referente a myId
		Hashtable<UUID, Integer> tabla = HashMatrix.get(myId);

		// Actualizamos el valor referente a destId
		tabla.put(usuario.uuid, tabla.get(usuario.uuid) + 1);
	}

	/**
	 * Autoincrementamos el valor M[myId][destId]++ de la matrix indicando que
	 * hemos enviado un mensaje a myId.
	 * 
	 * @param usuario
	 */
	public void incrementarDestino(LinkedList<Usuario> listaUsuarios) {
		// for (int i = 0; i < destIds.size(); i++)
		// ....M[myId][destIds.getEntry(i)]++;

		// Obtenemos la tabla referente a myId
		Hashtable<UUID, Integer> tabla = HashMatrix.get(myId);

		for (Usuario usuario : listaUsuarios)
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
		return HashMatrix.get(i).get(j);
	}
}