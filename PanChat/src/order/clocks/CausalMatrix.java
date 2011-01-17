package order.clocks;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import panchat.data.User;

public class CausalMatrix implements Serializable, IClock<CausalMatrix> {

	private static final long serialVersionUID = 1L;

	private User usuario;

	private User myId;

	/*
	 * Vamos a utilizar tablas hash en vez de matrices, para minimizar la
	 * información de la MatrixClock cuando esta matrix sea dinámica, es decir
	 * que sea modificable y admita insertar y eliminar antiguos usuarios.
	 */
	public Hashtable<User, Hashtable<User, Integer>> HashMatrix;

	public CausalMatrix(User usuario) {

		this.usuario = usuario;

		this.myId = usuario;

		HashMatrix = new Hashtable<User, Hashtable<User, Integer>>();

		addUser(usuario);
	}

	/**
	 * Actualizamos los valores obteniendo los mayores valores de ámbas matrices
	 * 
	 * @param nuevaHashMatrix
	 */
	public void receiveAction(CausalMatrix causalMatrix) {

		Hashtable<User, Hashtable<User, Integer>> M = this.HashMatrix;
		Hashtable<User, Hashtable<User, Integer>> W = causalMatrix.HashMatrix;

		// Es lo equivalente en tablas hash a :
		//
		// for (int i = 0; i < N; i++)
		// ......for (int j = 0; j < N; j++)
		// .........if (W[i][j] > M[i][j])
		// ............M[i][j] = W[i][j];
		// 
		Iterator<Entry<User, Hashtable<User, Integer>>> i = W.entrySet()
				.iterator();
		while (i.hasNext()) {

			User w_i = i.next().getKey();

			/*
			 * Si existe la fila en nuestra matrix, entonces tenemos que
			 * actualizar cada casilla de la fila
			 */
			if (M.containsKey(w_i)) {

				/*
				 * Hacemos una actualización por columnas.
				 */
				Iterator<Entry<User, Hashtable<User, Integer>>> j = W
						.entrySet().iterator();
				while (j.hasNext()) {
					User w_i_j = j.next().getKey();

					/*
					 * Si existe la columna en nuestra matrix, escogemos el
					 * valor más entre la columna nueva y la nuestra.
					 */
					if (M.get(w_i).containsKey(w_i_j)) {

						/*
						 * Obtenemos los valores de ambas matrices, los
						 * comparamos y reasignamos el máximo de los 2
						 */
						Integer M_i_j = M.get(w_i).get(w_i_j);
						Integer W_i_j = W.get(w_i).get(w_i_j);

						// Solo actualizamos si hay que actualizar
						if (W_i_j > M_i_j)
							HashMatrix.get(w_i).put(w_i_j, W_i_j);

					}
					/*
					 * Como esta fila no existe en nuestra matrix, simplemente
					 * ponemos el valor en nuestra matrix
					 */
					else {

						Integer valor2 = W.get(w_i).get(w_i_j);
						HashMatrix.get(w_i).put(w_i_j, valor2);

					}
				}

			}
			/*
			 * No existe la fila en nuestra matrix, con lo cúal añadimos toda la
			 * fila de la matrix nueva, e incluimos nuestro identificador a 0.
			 */
			else {

				HashMatrix.put(w_i, W.get(w_i));

				HashMatrix.get(w_i).put(usuario, 0);
			}
		}
	}

	/**
	 * Añade un nuevo usuario a la MatrixClock
	 * 
	 * @param nuevoUsuario
	 */
	public void addUser(User nuevoUsuario) {

		// Primero añadimos unas columnas que falten en las filas ya existentes.
		Iterator<Entry<User, Hashtable<User, Integer>>> iter = HashMatrix
				.entrySet().iterator();
		while (iter.hasNext()) {
			User uuid = iter.next().getKey();

			HashMatrix.get(uuid).put(nuevoUsuario, 0);
		}

		// Añadimos la fila entera.
		Hashtable<User, Integer> tabla = new Hashtable<User, Integer>();
		HashMatrix.put(nuevoUsuario, tabla);

		iter = HashMatrix.entrySet().iterator();
		while (iter.hasNext()) {
			User uuid = iter.next().getKey();

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
	public void send(User usuario) {
		// M[myId][destId]++;

		// Obtenemos la tabla referente a myId
		Hashtable<User, Integer> tabla = HashMatrix.get(myId);

		// Actualizamos el valor referente a destId
		tabla.put(usuario, tabla.get(usuario) + 1);
	}

	/**
	 * Autoincrementamos el valor M[myId][destId]++ de la matrix indicando que
	 * hemos enviado un mensaje a myId.
	 * 
	 * @param user
	 */
	public void send(List<User> listaUsuarios) {
		// for (int i = 0; i < destIds.size(); i++)
		// ....M[myId][destIds.getEntry(i)]++;

		// Obtenemos la tabla referente a myId
		Hashtable<User, Integer> tabla = HashMatrix.get(myId);

		for (User usuario : listaUsuarios)
			// Actualizamos el valor referente a destId
			tabla.put(usuario, tabla.get(usuario) + 1);
	}

	/**
	 * Devolvemos el valor de la matrix para las posiciones de i y j.
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public int getValue(User i, User j) {
		try {

			return HashMatrix.get(i).get(j);

		} catch (Exception e) {

			return 0;

		}
	}

	public void printMatrix() {
		System.out.println(toString());
	}

	@Override
	public String toString() {

		String result = "CausalMatrix" + HashMatrix;
		return result;
	}

	/**
	 * Clonar matriz causal
	 */
	public CausalMatrix clone() {
		CausalMatrix clone = new CausalMatrix(myId);
		clone.receiveAction(this);
		return clone;
	}
}