package panchat.ui.tests;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import panchat.addressing.channels.Canal;
import panchat.addressing.channels.ListaCanales;
import panchat.addressing.users.ListaUsuarios;
import panchat.addressing.users.Usuario;
import panchat.ui.main.TablaCanales;

public class TestTablaCanales {

	public static void main(String[] args) {

		// Obtenemos referencias a las clases Singleton
		ListaCanales canales = new ListaCanales();
		ListaUsuarios usuarios = new ListaUsuarios(canales);

		// Creamos un listado de usuarios
		LinkedList<Usuario> listaUsuarios = new LinkedList<Usuario>();
		listaUsuarios.add(new Usuario("127.0.0.1", 50000, "JonAn"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50001, "Javier"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50002, "Dennis"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50003, "Imanol"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50004, "Nagore"));

		// Registramos el listado de usuarios en la clase Singleton Conexiones
		for (Usuario address : listaUsuarios)
			usuarios.añadirUsuario(address);

		// Creamos un listado de canales
		LinkedList<Canal> listaCanales = new LinkedList<Canal>();

		Canal canalLocos = new Canal("Locos", usuarios);
		Canal canalIntrepidos = new Canal("Intrepidos", usuarios);
		Canal canalProgramadores = new Canal("Programadores", usuarios);
		
		for (Usuario usuario : listaUsuarios)
			canalLocos.anyadirUsuarioConectado(usuario);
		for (Usuario usuario : listaUsuarios.subList(1, 3))
			canalIntrepidos.anyadirUsuarioConectado(usuario);
		for (Usuario usuario : listaUsuarios.subList(2, 4))
			canalIntrepidos.anyadirUsuarioConectado(usuario);

		listaCanales.add(canalLocos);
		listaCanales.add(canalIntrepidos);
		listaCanales.add(canalProgramadores);

		// Registramos el listado de usuarios en la clase Singleton Canales
		for (Canal canal : listaCanales)
			canales.añadirCanal(canal);

		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane pane = new JTabbedPane();

		/*
		 * Añadimos el panel2
		 */
		pane.addTab("Canales", new TablaCanales(canales));

		jframe.getContentPane().add(pane);

		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}
}
