package panchat.ui.tests;

import java.util.LinkedList;

import panchat.Panchat;
import panchat.data.Canal;
import panchat.data.ListaCanales;
import panchat.data.ListaUsuarios;
import panchat.data.Usuario;
import panchat.ui.chat.VentanaConversacionCanal;

public class TestVentanaConversacionCanal {

	public static void main(String[] args) {

		Panchat panchat = new Panchat(new Usuario("Jon Ander"));

		// Obtenemos referencias a las clases Singleton
		ListaCanales canales = panchat.getListaCanales();
		ListaUsuarios usuarios = panchat.getListaUsuarios();

		// Creamos un listado de usuarios
		LinkedList<Usuario> listaUsuarios = new LinkedList<Usuario>();
		listaUsuarios.add(new Usuario("JonAn"));
		listaUsuarios.add(new Usuario("Javier"));
		listaUsuarios.add(new Usuario("Dennis"));
		listaUsuarios.add(new Usuario("Imanol"));
		listaUsuarios.add(new Usuario("Nagore"));

		// Registramos el listado de usuarios en la clase Singleton Conexiones
		for (Usuario address : listaUsuarios)
			usuarios.añadirUsuario(address);

		Canal canal = new Canal("Frikis", usuarios);
		canales.añadirCanal(canal);

		// Añadimos el usuario
		canal.anyadirUsuarioConectado(listaUsuarios.get(0));
		canal.anyadirUsuarioConectado(listaUsuarios.get(1));

		new VentanaConversacionCanal(panchat, canal);
	}
}
