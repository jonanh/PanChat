package panchat.testunits;

import java.util.LinkedList;

import panchat.Panchat;
import panchat.data.UserList;
import panchat.data.User;
import junit.framework.TestCase;

public class ConnectorTest extends TestCase {

	public final static boolean DEBUG = false;

	private LinkedList<User> listaUsuarios;

	protected void setUp() throws Exception {
		super.setUp();

		// Creamos un listado de usuarios
		listaUsuarios = new LinkedList<User>();
		listaUsuarios.add(new User("JonAn"));
		listaUsuarios.add(new User("Javier"));
		listaUsuarios.add(new User("Dennis"));
		listaUsuarios.add(new User("Imanol"));
		listaUsuarios.add(new User("Nagore"));
		listaUsuarios.add(new User("Nerea"));
		listaUsuarios.add(new User("Ainara"));
	}

	/**
	 * Test para comprobar el correcto registro de sus usuarios
	 */
	public void testRegistroMulticastUsuarios() {

		LinkedList<UserList> listaListaUsuarios;
		LinkedList<Panchat> listaListaPanchat;

		listaListaUsuarios = new LinkedList<UserList>();
		listaListaPanchat = new LinkedList<Panchat>();

		for (User usuario : listaUsuarios.subList(0, 5)) {

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
			for (UserList listaUsuarios : listaListaUsuarios)
				System.out.println(listaUsuarios);

		// Comprobamos que todos se han registrado
		for (int i = 0; i < listaListaUsuarios.size() - 1; i++) {
			UserList lista1 = listaListaUsuarios.get(0 + i);
			UserList lista2 = listaListaUsuarios.get(1 + i);

			assertEquals(lista1, lista2);
		}

		// Mandamos un mensaje de uno a todos
		for (Panchat lPanchat : listaListaPanchat) {
			for (User lUsuario : lPanchat.getListaUsuarios()
					.getUserList()) {
				if (!lPanchat.getUsuario().equals(lUsuario)) {
					assertNotNull(lPanchat.getConnector().getOIS(lUsuario.uuid));
					assertNotNull(lPanchat.getConnector().getOOS(lUsuario.uuid));

					lPanchat.getCausalLinker().sendMsg(lUsuario, 5);
				}
			}
		}

		// Ordenamos terminar la aplicación
		for (Panchat panchat : listaListaPanchat.subList(0, 3))
			panchat.accionDesegistrarCliente();

		// Dejamos un tiempo para que termine todo
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Comprobamos que finalmente se han quedado desregistrados 3 clientes
		// (solo se tienen registrados a si mismos)
		assertEquals(1, listaListaUsuarios.get(0).length());
		assertEquals(1, listaListaUsuarios.get(1).length());
		assertEquals(1, listaListaUsuarios.get(2).length());

		// Comprobamos que los otros dos clientes se tienen registrados
		// mutuamente
		assertEquals(2, listaListaUsuarios.get(3).length());
		assertEquals(2, listaListaUsuarios.get(4).length());

	}
}
