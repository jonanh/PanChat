package panchat.testunits;

import java.util.LinkedList;

import panchat.Panchat;
import panchat.data.ListaUsuarios;
import panchat.data.Usuario;
import junit.framework.TestCase;

public class ConnectorTest extends TestCase {

	public final static boolean DEBUG = false;

	private LinkedList<Usuario> listaUsuarios;

	protected void setUp() throws Exception {
		super.setUp();

		// Creamos un listado de usuarios
		listaUsuarios = new LinkedList<Usuario>();
		listaUsuarios.add(new Usuario("JonAn"));
		listaUsuarios.add(new Usuario("Javier"));
		listaUsuarios.add(new Usuario("Dennis"));
		listaUsuarios.add(new Usuario("Imanol"));
		listaUsuarios.add(new Usuario("Nagore"));
		listaUsuarios.add(new Usuario("Nerea"));
		listaUsuarios.add(new Usuario("Ainara"));
	}

	/**
	 * Test para comprobar el correcto registro de sus usuarios
	 */
	public void testRegistroMulticastUsuarios() {

		LinkedList<ListaUsuarios> listaListaUsuarios;
		LinkedList<Panchat> listaListaPanchat;

		listaListaUsuarios = new LinkedList<ListaUsuarios>();
		listaListaPanchat = new LinkedList<Panchat>();

		for (Usuario usuario : listaUsuarios.subList(0, 5)) {

			// Creamos una nueva clase Panchat
			Panchat panchat = new Panchat(usuario);

			// Dejamos un tiempo para que salude
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Añadimos las listas de usuarios de cada instancia de Panchat a
			// una lista
			listaListaUsuarios.add(panchat.getListaUsuarios());
			listaListaPanchat.add(panchat);
		}

		if (DEBUG)
			for (ListaUsuarios listaUsuarios : listaListaUsuarios)
				System.out.println(listaUsuarios);

		// Comprobamos que todos se han registrado
		for (int i = 0; i < listaListaUsuarios.size() - 1; i++) {
			ListaUsuarios lista1 = listaListaUsuarios.get(0 + i);
			ListaUsuarios lista2 = listaListaUsuarios.get(1 + i);

			assertEquals(lista1, lista2);
		}

		// Ordenamos terminar la aplicación
		for (Panchat panchat : listaListaPanchat.subList(0, 3))
			panchat.desegistrarCliente();

		// Dejamos un tiempo para que termine todo
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Comprobamos que finalmente se han quedado desregistrados 3 clientes
		// (solo se tienen registrados a si mismos)
		assertEquals(listaListaUsuarios.get(0).getNumUsuarios(), 1);
		assertEquals(listaListaUsuarios.get(1).getNumUsuarios(), 1);
		assertEquals(listaListaUsuarios.get(2).getNumUsuarios(), 1);

		// Comprobamos que los otros dos clientes se tienen registrados
		// mutuamente
		assertEquals(listaListaUsuarios.get(3).getNumUsuarios(), 2);
		assertEquals(listaListaUsuarios.get(4).getNumUsuarios(), 2);

	}
}
