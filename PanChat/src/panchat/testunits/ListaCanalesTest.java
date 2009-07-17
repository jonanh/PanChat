package panchat.testunits;

import java.util.Collections;
import java.util.LinkedList;

import panchat.addressing.channels.Canal;
import panchat.addressing.channels.ListaCanales;
import panchat.addressing.users.ListaUsuarios;
import panchat.addressing.users.Usuario;

import junit.framework.TestCase;

public class ListaCanalesTest extends TestCase {

	LinkedList<Usuario> listaUsuarios;

	protected void setUp() throws Exception {
		super.setUp();

		// Creamos un listado de usuarios
		listaUsuarios = new LinkedList<Usuario>();
		listaUsuarios.add(new Usuario("127.0.0.1", 50000, "Dennis"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50001, "Imanol"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50002, "Javier"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50003, "JonAn"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50004, "Nagore"));

		Collections.sort(listaUsuarios);
	}

	/**
	 * Probamos a registrar usuarios, y comprobamos el número de usuarios
	 * conectados
	 */
	public void testGetNumUsuariosConectados() {

		ListaCanales canales = new ListaCanales();
		ListaUsuarios usuarios = new ListaUsuarios(canales);

		usuarios.añadirUsuario(listaUsuarios.get(0));
		usuarios.añadirUsuario(listaUsuarios.get(1));
		usuarios.añadirUsuario(listaUsuarios.get(2));

		Canal canal1 = new Canal("Frikis", usuarios);

		canales.añadirCanal(canal1);
		canal1.anyadirUsuarioConectado(listaUsuarios.get(0));

		assertEquals(1, canal1.getNumUsuariosConectados());
	}

	/**
	 * Probamos a registrar usuarios, y comprobamos el número de usuarios sin
	 * conectar
	 */
	public void testGetNumUsuariosDesconectados() {

		ListaCanales canales = new ListaCanales();
		ListaUsuarios usuarios = new ListaUsuarios(canales);

		usuarios.añadirUsuario(listaUsuarios.get(0));
		usuarios.añadirUsuario(listaUsuarios.get(1));
		usuarios.añadirUsuario(listaUsuarios.get(2));

		Canal canal1 = new Canal("Frikis", usuarios);

		canales.añadirCanal(canal1);
		canal1.anyadirUsuarioConectado(listaUsuarios.get(0));

		assertEquals(2, canal1.getNumUsuariosDesconectados());
	}

	/**
	 * Probamos a registrar un usuario, y probamos los usuarios sin registrar
	 * que quedan
	 */
	public void testGetUsuarioDesconectado() {

		ListaCanales canales = new ListaCanales();
		ListaUsuarios usuarios = new ListaUsuarios(canales);

		usuarios.añadirUsuario(listaUsuarios.get(0));
		usuarios.añadirUsuario(listaUsuarios.get(1));
		usuarios.añadirUsuario(listaUsuarios.get(2));

		Canal canal1 = new Canal("Frikis", usuarios);
		canal1.anyadirUsuarioConectado(listaUsuarios.get(0));

		canales.añadirCanal(canal1);

		assertEquals(listaUsuarios.get(0), canal1.getUsuarioConectado(0));
		assertEquals(listaUsuarios.get(1), canal1.getUsuarioDesconectado(0));
		assertEquals(listaUsuarios.get(2), canal1.getUsuarioDesconectado(1));
	}

	/**
	 * Probamos a registrar un usuario, y probamos a eliminarlo, y a comprobar
	 * los usuarios registrados y sin registrar.
	 */
	public void testAñadirYEliminar() {

		ListaCanales canales = new ListaCanales();
		ListaUsuarios usuarios = new ListaUsuarios(canales);

		usuarios.añadirUsuario(listaUsuarios.get(0));
		usuarios.añadirUsuario(listaUsuarios.get(1));
		usuarios.añadirUsuario(listaUsuarios.get(2));

		Canal canal1 = new Canal("Frikis", usuarios);
		canales.añadirCanal(canal1);
		
		// Añadimos el usuario
		canal1.anyadirUsuarioConectado(listaUsuarios.get(0));

		// Comprobamos que hay un usuario conectado
		assertEquals(1, canal1.getNumUsuariosConectados());
		assertEquals(2, canal1.getNumUsuariosDesconectados());
		
		canal1.eliminarUsuario(listaUsuarios.get(0));
		
		// Comprobamos que se ha desregistrado
		assertEquals(0, canal1.getNumUsuariosConectados());
		assertEquals(3, canal1.getNumUsuariosDesconectados());
		
		// Eliminamos el usuario
		usuarios.eliminarUsuario(listaUsuarios.get(0));

		assertEquals(0, canal1.getNumUsuariosConectados());
		assertEquals(2, canal1.getNumUsuariosDesconectados());
		assertEquals(listaUsuarios.get(1), canal1.getUsuarioDesconectado(0));
		assertEquals(listaUsuarios.get(2), canal1.getUsuarioDesconectado(1));
		
		
		// Lo volvemos a añadir y lo volvemos a eliminar
		usuarios.añadirUsuario(listaUsuarios.get(0));
		canal1.anyadirUsuarioConectado(listaUsuarios.get(0));
		
		// Comprobamos que hay un usuario conectado
		assertEquals(1, canal1.getNumUsuariosConectados());
		assertEquals(2, canal1.getNumUsuariosDesconectados());
		
		usuarios.eliminarUsuario(listaUsuarios.get(0));
		
		// Y comprobamos el resultado
		assertEquals(0, canal1.getNumUsuariosConectados());
		assertEquals(2, canal1.getNumUsuariosDesconectados());
	}
}
