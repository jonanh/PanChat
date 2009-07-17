package panchat.testunits;

import java.util.Collections;
import java.util.LinkedList;

import panchat.channels.ListaCanales;
import panchat.users.ListaUsuarios;
import panchat.users.Usuario;
import junit.framework.TestCase;

public class ListaUsuariosTest extends TestCase {

	public static boolean DEBUG;;
	LinkedList<Usuario> lista;

	ListaUsuarios listaUsuarios;
	ListaCanales listaCanales;

	protected void setUp() throws Exception {
		super.setUp();

		// Creamos un listado de usuarios
		lista = new LinkedList<Usuario>();
		lista.add(new Usuario("127.0.0.1", 50000, "Dennis"));
		lista.add(new Usuario("127.0.0.1", 50001, "Imanol"));
		lista.add(new Usuario("127.0.0.1", 50002, "Javier"));
		lista.add(new Usuario("127.0.0.1", 50003, "JonAn"));
		lista.add(new Usuario("127.0.0.1", 50004, "Nagore"));

		Collections.sort(lista);

		listaCanales = new ListaCanales();
		listaUsuarios = new ListaUsuarios(listaCanales);
	}

	/**
	 * Verificamos el funcionamiento de registrar y el iterador
	 */
	public void testAñadirUsuario() {
		listaUsuarios.añadirUsuario(lista.get(0));
		listaUsuarios.añadirUsuario(lista.get(1));

		for (int i = 0; i < 2; i++) {
			assertEquals(lista.get(i), listaUsuarios.getUsuario(i));
		}
	}

	/**
	 * Comprobamos que dos listas de usuarios con 0 elementos son iguales
	 */
	public void testEqualsObject() {
		ListaUsuarios lista1 = new ListaUsuarios(listaCanales);
		ListaUsuarios lista2 = new ListaUsuarios(listaCanales);
		assertEquals(lista1, lista2);
	}

	/**
	 * Comprobamos que dos listas de usuarios con el mismo elemento son
	 * equivalentes
	 */
	public void testEqualsObject2() {
		ListaUsuarios lista1 = new ListaUsuarios(listaCanales);
		ListaUsuarios lista2 = new ListaUsuarios(listaCanales);
		lista1.añadirUsuario(lista.get(0));
		lista2.añadirUsuario(lista.get(0));
		assertEquals(lista1, lista2);
	}

	/**
	 * Comprobamos que dos listas de usuarios con distintos elementos son
	 * distintas
	 */
	public void testEqualsObject3() {
		ListaUsuarios lista1 = new ListaUsuarios(listaCanales);
		ListaUsuarios lista2 = new ListaUsuarios(listaCanales);
		lista1.añadirUsuario(lista.get(0));
		lista2.añadirUsuario(lista.get(1));
		assertFalse(lista1.equals(lista2));
	}
}
