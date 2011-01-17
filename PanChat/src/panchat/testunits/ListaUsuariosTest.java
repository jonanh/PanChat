package panchat.testunits;

import java.util.Collections;
import java.util.LinkedList;

import panchat.data.ChatRoomList;
import panchat.data.UserList;
import panchat.data.User;
import junit.framework.TestCase;

public class ListaUsuariosTest extends TestCase {

	public static boolean DEBUG;;
	LinkedList<User> lista;

	UserList listaUsuarios;
	ChatRoomList listaCanales;

	protected void setUp() throws Exception {
		super.setUp();

		// Creamos un listado de usuarios
		lista = new LinkedList<User>();
		lista.add(new User("127.0.0.1", 50000, "Dennis"));
		lista.add(new User("127.0.0.1", 50001, "Imanol"));
		lista.add(new User("127.0.0.1", 50002, "Javier"));
		lista.add(new User("127.0.0.1", 50003, "JonAn"));
		lista.add(new User("127.0.0.1", 50004, "Nagore"));

		Collections.sort(lista);

		listaCanales = new ChatRoomList();
		listaUsuarios = new UserList(listaCanales);
	}

	/**
	 * Verificamos el funcionamiento de registrar y el iterador
	 */
	public void testAnyadirUsuario() {
		listaUsuarios.add(lista.get(0));
		listaUsuarios.add(lista.get(1));

		for (int i = 0; i < 2; i++) {
			assertEquals(lista.get(i), listaUsuarios.getUser(i));
		}
	}

	/**
	 * Comprobamos que dos listas de usuarios con 0 elementos son iguales
	 */
	public void testEqualsObject() {
		UserList lista1 = new UserList(listaCanales);
		UserList lista2 = new UserList(listaCanales);
		assertEquals(lista1, lista2);
	}

	/**
	 * Comprobamos que dos listas de usuarios con el mismo elemento son
	 * equivalentes
	 */
	public void testEqualsObject2() {
		UserList lista1 = new UserList(listaCanales);
		UserList lista2 = new UserList(listaCanales);
		lista1.add(lista.get(0));
		lista2.add(lista.get(0));
		assertEquals(lista1, lista2);
	}

	/**
	 * Comprobamos que dos listas de usuarios con distintos elementos son
	 * distintas
	 */
	public void testEqualsObject3() {
		UserList lista1 = new UserList(listaCanales);
		UserList lista2 = new UserList(listaCanales);
		lista1.add(lista.get(0));
		lista2.add(lista.get(1));
		assertFalse(lista1.equals(lista2));
	}
}
