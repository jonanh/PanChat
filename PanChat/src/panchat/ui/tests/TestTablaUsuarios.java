package panchat.ui.tests;

import java.util.LinkedList;

import javax.swing.JFrame;

import panchat.addressing.channels.ListaCanales;
import panchat.addressing.users.ListaUsuarios;
import panchat.addressing.users.Usuario;
import panchat.ui.main.TablaUsuarios;

public class TestTablaUsuarios {

	public static void main(String[] args) {

		// Obtenemos referencias a las clases Singleton
		ListaUsuarios usuarios = new ListaUsuarios(new ListaCanales());
		
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

		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jframe.getContentPane().add(new TablaUsuarios(usuarios));

		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}
}
