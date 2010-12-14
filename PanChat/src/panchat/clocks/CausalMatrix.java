package panchat.clocks;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
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

		Hashtable<User, Hashtable<User, Integer>> nuevaHashMatrix = causalMatrix.HashMatrix;

		// Es lo equivalente en tablas hash a :
		//
		// for (int i = 0; i < N; i++)
		// ......for (int j = 0; j < N; j++)
		// .........if (W[i][j] > M[i][j])
		// ............M[i][j] = W[i][j];
		// 
		Iterator<Entry<User, Hashtable<User, Integer>>> iter = nuevaHashMatrix
				.entrySet().iterator();
		while (iter.hasNext()) {
			User user1 = iter.next().getKey();

			/*
			 * Si existe la fila en nuestra matrix, entonces tenemos que
			 * actualizar cada casilla de la fila
			 */
			if (HashMatrix.containsKey(user1)) {

				/*
				 * Hacemos una actualización por columnas.
				 */
				Iterator<Entry<User, Hashtable<User, Integer>>> iter2 = nuevaHashMatrix
						.entrySet().iterator();
				while (iter2.hasNext()) {
					User user2 = iter2.next().getKey();

					/*
					 * Si existe la columna en nuestra matrix, escogemos el
					 * valor más entre la columna nueva y la nuestra.
					 */
					if (HashMatrix.get(user1).containsKey(user2)) {

						/*
						 * Obtenemos los valores de ambas matrices, los
						 * comparamos y reasignamos el máximo de los 2
						 */
						Integer valor1 = HashMatrix.get(user1).get(user2);
						Integer valor2 = nuevaHashMatrix.get(user1).get(user2);

						// Solo actualizamos si hay que actualizar
						if (valor2 > valor1)
							HashMatrix.get(user1).put(user2, valor2);

					}
					/*
					 * Como esta fila no existe en nuestra matrix, simplemente
					 * ponemos el valor en nuestra matrix
					 */
					else {

						Integer valor2 = nuevaHashMatrix.get(user1).get(user2);
						HashMatrix.get(user1).put(user2, valor2);

					}
				}

			}
			/*
			 * No existe la fila en nuestra matrix, con lo cúal añadimos toda la
			 * fila de la matrix nueva, e incluimos nuestro identificador a 0.
			 */
			else {

				HashMatrix.put(user1, nuevaHashMatrix.get(user1));

				HashMatrix.get(user1).put(usuario, 0);
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
	public void send(LinkedList<User> listaUsuarios) {
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

	/*
	 * Tests
	 */
	public static void main(String[] args) {
		// Creamos usuarios
		User[] users = { new User("A"), new User("B"), new User("C") };

		// Creamos 2 vectores lógicos
		CausalMatrix vc1 = new CausalMatrix(users[0]);
		CausalMatrix vc2 = new CausalMatrix(users[1]);

		// Añadimos usuarios
		vc1.addUser(users[1]);
		vc1.addUser(users[2]);
		vc2.addUser(users[0]);
		vc2.addUser(users[2]);

		// Imprimos los vectores
		System.out.println(vc1);
		System.out.println(vc2);

		// Simulamos el envio de un mensaje del usuario A al usuario B
		System.out.println("Enviamos un mensaje del usuario A al B");
		System.out.println("Enviamos un mensaje del usuario A al C");
		vc1.send(users[1]);
		vc1.send(users[2]);
		System.out.println(vc1);

		// Recibimos el anterior mensaje
		System.out.println("Recibimos el anterior mensaje");
		vc2.receiveAction(vc1);
		System.out.println(vc2);
	}
}