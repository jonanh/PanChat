package panchat.testunits;

import java.util.LinkedList;

import panchat.addressing.Canal;
import panchat.addressing.ListaCanales;
import panchat.addressing.ListaUsuarios;
import panchat.addressing.Usuario;

import junit.framework.TestCase;

public class ListaCanalesTest extends TestCase {

	LinkedList<Usuario> listaUsuarios;

	protected void setUp() throws Exception {
		super.setUp();

		// Creamos un listado de usuarios
		listaUsuarios = new LinkedList<Usuario>();
		listaUsuarios.add(new Usuario("hostname", 50000, "JonAn"));
		listaUsuarios.add(new Usuario("hostname", 50001, "Javier"));
		listaUsuarios.add(new Usuario("hostname", 50002, "Dennis"));
		listaUsuarios.add(new Usuario("hostname", 50003, "Imanol"));
		listaUsuarios.add(new Usuario("hostname", 50004, "Nagore"));
	}

	public void testAñadirCanal() {

		ListaCanales canales = new ListaCanales();
		ListaUsuarios usuarios = new ListaUsuarios();

		usuarios.añadirUsuario(listaUsuarios.get(0));
		usuarios.añadirUsuario(listaUsuarios.get(0));

		Canal canal1 = new Canal("Frikis", usuarios);

		canales.añadirCanal(canal1);

		assertEquals(2, canal1.getSize());
	}
	
	public void testAñadirCanal2() {

		ListaCanales canales = new ListaCanales();
		ListaUsuarios usuarios = new ListaUsuarios();

		usuarios.añadirUsuario(listaUsuarios.get(0));
		usuarios.añadirUsuario(listaUsuarios.get(0));

		Canal canal1 = new Canal("Frikis", usuarios);
		canal1.anyadirUsuarioConectado(listaUsuarios.get(0));
		
		canales.añadirCanal(canal1);

		assertEquals(1, canal1.getSize());
	}


}
