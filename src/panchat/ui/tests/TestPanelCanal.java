package panchat.ui.tests;


import java.util.LinkedList;

import javax.swing.JFrame;

import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.UserList;
import panchat.data.User;
import panchat.ui.chat.ChatRoomPanel;

public class TestPanelCanal {

	public static void main(String[] args) {

		// Obtenemos referencias a las clases Singleton
		ChatRoomList canales = new ChatRoomList();
		UserList usuarios = new UserList(canales);
		
		// Creamos un listado de usuarios
		LinkedList<User> listaUsuarios = new LinkedList<User>();
		listaUsuarios.add(new User("127.0.0.1", 50000, "JonAn"));
		listaUsuarios.add(new User("127.0.0.1", 50001, "Javier"));
		listaUsuarios.add(new User("127.0.0.1", 50002, "Dennis"));
		listaUsuarios.add(new User("127.0.0.1", 50003, "Imanol"));
		listaUsuarios.add(new User("127.0.0.1", 50004, "Nagore"));

		// Registramos el listado de usuarios en la clase Singleton Conexiones
		for (User address : listaUsuarios)
			usuarios.add(address);

		ChatRoom canal = new ChatRoom("Frikis", usuarios);
		canales.addChannel(canal);
		
		// Añadimos el usuario
		canal.joinUser(listaUsuarios.get(0));
		canal.joinUser(listaUsuarios.get(1));
		
		
		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jframe.getContentPane().add(new ChatRoomPanel(canal));

		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}
}
