package panchat.testunits;

import java.util.Collections;
import java.util.LinkedList;

import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.UserList;
import panchat.data.User;

import junit.framework.TestCase;

public class ListaCanalesTest extends TestCase {

	LinkedList<User> listaUsuarios;

	protected void setUp() throws Exception {
		super.setUp();

		// Creamos un listado de usuarios
		listaUsuarios = new LinkedList<User>();
		listaUsuarios.add(new User("127.0.0.1", 50000, "Dennis"));
		listaUsuarios.add(new User("127.0.0.1", 50001, "Imanol"));
		listaUsuarios.add(new User("127.0.0.1", 50002, "Javier"));
		listaUsuarios.add(new User("127.0.0.1", 50003, "JonAn"));
		listaUsuarios.add(new User("127.0.0.1", 50004, "Nagore"));

		Collections.sort(listaUsuarios);
	}

	/**
	 * Probamos a registrar usuarios, y comprobamos el número de usuarios
	 * conectados
	 */
	public void testGetNumUsuariosConectados() {

		ChatRoomList canales = new ChatRoomList();
		UserList usuarios = new UserList(canales);

		usuarios.add(listaUsuarios.get(0));
		usuarios.add(listaUsuarios.get(1));
		usuarios.add(listaUsuarios.get(2));

		ChatRoom canal1 = new ChatRoom("Frikis", usuarios);

		canales.addChannel(canal1);
		canal1.joinUser(listaUsuarios.get(0));

		assertEquals(1, canal1.getNumUsuariosConectados());
	}

	/**
	 * Probamos a registrar usuarios, y comprobamos el número de usuarios sin
	 * conectar
	 */
	public void testGetNumUsuariosDesconectados() {

		ChatRoomList canales = new ChatRoomList();
		UserList usuarios = new UserList(canales);

		usuarios.add(listaUsuarios.get(0));
		usuarios.add(listaUsuarios.get(1));
		usuarios.add(listaUsuarios.get(2));

		ChatRoom canal1 = new ChatRoom("Frikis", usuarios);

		canales.addChannel(canal1);
		canal1.joinUser(listaUsuarios.get(0));

		assertEquals(2, canal1.getNumUsuariosDesconectados());
	}

	/**
	 * Probamos a registrar un usuario, y probamos los usuarios sin registrar
	 * que quedan
	 */
	public void testGetUsuarioDesconectado() {

		ChatRoomList canales = new ChatRoomList();
		UserList usuarios = new UserList(canales);

		usuarios.add(listaUsuarios.get(0));
		usuarios.add(listaUsuarios.get(1));
		usuarios.add(listaUsuarios.get(2));

		ChatRoom canal1 = new ChatRoom("Frikis", usuarios);
		canal1.joinUser(listaUsuarios.get(0));

		canales.addChannel(canal1);

		assertEquals(listaUsuarios.get(0), canal1.getUsuarioConectado(0));
		assertEquals(listaUsuarios.get(1), canal1.getUsuarioDesconectado(0));
		assertEquals(listaUsuarios.get(2), canal1.getUsuarioDesconectado(1));
	}

	/**
	 * Probamos a registrar un usuario, y probamos a eliminarlo, y a comprobar
	 * los usuarios registrados y sin registrar.
	 */
	public void testAñadirYEliminar() {

		ChatRoomList canales = new ChatRoomList();
		UserList usuarios = new UserList(canales);

		usuarios.add(listaUsuarios.get(0));
		usuarios.add(listaUsuarios.get(1));
		usuarios.add(listaUsuarios.get(2));

		ChatRoom canal1 = new ChatRoom("Frikis", usuarios);
		canales.addChannel(canal1);
		
		// Añadimos el usuario
		canal1.joinUser(listaUsuarios.get(0));

		// Comprobamos que hay un usuario conectado
		assertEquals(1, canal1.getNumUsuariosConectados());
		assertEquals(2, canal1.getNumUsuariosDesconectados());
		
		canal1.removeUser(listaUsuarios.get(0));
		
		// Comprobamos que se ha desregistrado
		assertEquals(0, canal1.getNumUsuariosConectados());
		assertEquals(3, canal1.getNumUsuariosDesconectados());
		
		// Eliminamos el usuario
		usuarios.remove(listaUsuarios.get(0));

		assertEquals(0, canal1.getNumUsuariosConectados());
		assertEquals(2, canal1.getNumUsuariosDesconectados());
		assertEquals(listaUsuarios.get(1), canal1.getUsuarioDesconectado(0));
		assertEquals(listaUsuarios.get(2), canal1.getUsuarioDesconectado(1));
		
		
		// Lo volvemos a añadir y lo volvemos a eliminar
		usuarios.add(listaUsuarios.get(0));
		canal1.joinUser(listaUsuarios.get(0));
		
		// Comprobamos que hay un usuario conectado
		assertEquals(1, canal1.getNumUsuariosConectados());
		assertEquals(2, canal1.getNumUsuariosDesconectados());
		
		usuarios.remove(listaUsuarios.get(0));
		
		// Y comprobamos el resultado
		assertEquals(0, canal1.getNumUsuariosConectados());
		assertEquals(2, canal1.getNumUsuariosDesconectados());
	}
}
