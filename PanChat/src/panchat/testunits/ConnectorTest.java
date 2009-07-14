package panchat.testunits;

import java.net.InetAddress;
import java.util.LinkedList;

import panchat.Panchat;
import panchat.addressing.ListaUsuarios;
import panchat.addressing.Usuario;
import panchat.linker.Connector;
import panchat.listeners.MulticastListenerThread;
import junit.framework.TestCase;

public class ConnectorTest extends TestCase {

	public static boolean DEBUG;
	LinkedList<Usuario> listaUsuarios;
	String hostname;

	protected void setUp() throws Exception {
		super.setUp();

		ConnectorTest.DEBUG = false;
		Connector.DEBUG = false;
		MulticastListenerThread.DEBUG = false;

		hostname = InetAddress.getLocalHost().getHostAddress();

		// Creamos un listado de usuarios
		listaUsuarios = new LinkedList<Usuario>();
		listaUsuarios.add(new Usuario(hostname, 50000, "JonAn"));
		listaUsuarios.add(new Usuario(hostname, 50001, "Javier"));
		listaUsuarios.add(new Usuario(hostname, 50002, "Dennis"));
		listaUsuarios.add(new Usuario(hostname, 50003, "Imanol"));
		listaUsuarios.add(new Usuario(hostname, 50004, "Nagore"));
	}

	/**
	 * Test para comprobar la correcta construcción de una clase Connector
	 */
	public void testConnector() {
		Usuario usuario = new Usuario(hostname, 50100, "JonAn");
		assertNotNull(new Connector(new Panchat(usuario)));
	}

	/**
	 * Test para comprobar el correcto registro de sus usuarios
	 */
	public void testRegistroMulticastUsuarios() {

		LinkedList<ListaUsuarios> listaListaUsuarios = new LinkedList<ListaUsuarios>();

		for (Usuario usuario : listaUsuarios) {

			// Creamos una nueva clase Panchat
			Panchat panchat = new Panchat(usuario);

			// Creamos un nuevo conector
			new Connector(panchat);

			// Dejamos un tiempo para que salude
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Añadimos las listas de usuarios de cada instancia de Panchat a
			// una lista
			listaListaUsuarios.add(panchat.getListaUsuarios());
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
	}

}
