package panchat.ui.tests;

import java.util.LinkedList;

import javax.swing.JFrame;

import panchat.data.ChatRoomList;
import panchat.data.UserList;
import panchat.data.User;
import panchat.ui.main.UserTable;

public class TestTablaUsuarios {

	public static void main(String[] args) {

		// Obtenemos referencias a las clases Singleton
		UserList usuarios = new UserList(new ChatRoomList());
		
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

		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jframe.getContentPane().add(new UserTable(usuarios));

		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}
}
