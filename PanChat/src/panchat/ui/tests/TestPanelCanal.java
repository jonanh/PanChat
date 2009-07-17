package panchat.ui.tests;


import java.util.LinkedList;

import javax.swing.JFrame;

import panchat.channels.Canal;
import panchat.channels.ListaCanales;
import panchat.ui.channel.PanelCanal;
import panchat.users.ListaUsuarios;
import panchat.users.Usuario;

public class TestPanelCanal {

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

		Canal canal = new Canal("Frikis", usuarios);
		canales.añadirCanal(canal);
		
		// Añadimos el usuario
		canal.anyadirUsuarioConectado(listaUsuarios.get(0));
		canal.anyadirUsuarioConectado(listaUsuarios.get(1));
		
		
		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jframe.getContentPane().add(new PanelCanal(canal));

		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}
}
