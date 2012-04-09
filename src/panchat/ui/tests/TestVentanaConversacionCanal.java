package panchat.ui.tests;

import java.util.LinkedList;

import panchat.Panchat;
import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.UserList;
import panchat.data.User;
import panchat.ui.chat.ChatRoomWindow;

public class TestVentanaConversacionCanal {

	public static void main(String[] args) {

		Panchat panchat = new Panchat(new User("Jon Ander"));

		// Obtenemos referencias a las clases Singleton
		ChatRoomList canales = panchat.getChannelList();
		UserList usuarios = panchat.getListaUsuarios();

		// Creamos un listado de usuarios
		LinkedList<User> listaUsuarios = new LinkedList<User>();
		listaUsuarios.add(new User("JonAn"));
		listaUsuarios.add(new User("Javier"));
		listaUsuarios.add(new User("Dennis"));
		listaUsuarios.add(new User("Imanol"));
		listaUsuarios.add(new User("Nagore"));

		// Registramos el listado de usuarios en la clase Singleton Conexiones
		for (User address : listaUsuarios)
			usuarios.add(address);

		ChatRoom canal = new ChatRoom("Frikis", usuarios);
		canales.addChannel(canal);

		// Añadimos el usuario
		canal.joinUser(listaUsuarios.get(0));
		canal.joinUser(listaUsuarios.get(1));

		new ChatRoomWindow(panchat, canal);
	}
}
