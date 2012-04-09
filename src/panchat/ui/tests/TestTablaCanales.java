package panchat.ui.tests;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.UserList;
import panchat.data.User;
import panchat.ui.main.ChannelTable;

public class TestTablaCanales {

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

		// Creamos un listado de canales
		LinkedList<ChatRoom> listaCanales = new LinkedList<ChatRoom>();

		ChatRoom canalLocos = new ChatRoom("Locos", usuarios);
		ChatRoom canalIntrepidos = new ChatRoom("Intrepidos", usuarios);
		ChatRoom canalProgramadores = new ChatRoom("Programadores", usuarios);
		
		for (User usuario : listaUsuarios)
			canalLocos.joinUser(usuario);
		for (User usuario : listaUsuarios.subList(1, 3))
			canalIntrepidos.joinUser(usuario);
		for (User usuario : listaUsuarios.subList(2, 4))
			canalIntrepidos.joinUser(usuario);

		listaCanales.add(canalLocos);
		listaCanales.add(canalIntrepidos);
		listaCanales.add(canalProgramadores);

		// Registramos el listado de usuarios en la clase Singleton Canales
		for (ChatRoom canal : listaCanales)
			canales.addChannel(canal);

		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane pane = new JTabbedPane();

		/*
		 * Añadimos el panel2
		 */
		pane.addTab("Canales", new ChannelTable(canales));

		jframe.getContentPane().add(pane);

		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}
}
