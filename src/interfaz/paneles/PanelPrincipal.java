package interfaz.paneles;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import panchat.Panchat;
import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.UserList;
import panchat.data.User;
import panchat.data.models.ChatRoomListAbstractTableModel;



public class PanelPrincipal extends JTabbedPane{
	
	private static final long serialVersionUID = 1L;
	
	static String camino ="/interfaz/imagenes/";
	
	Panchat panchat;
	
	PanelCentral central;
	PanelCanales can;
	
	ChatRoomList canales;
	ChatRoomListAbstractTableModel modelo;
	String name;
	
	public PanelPrincipal(String name, Panchat panchat){
		
		this.panchat=panchat;
		this.name=name;
		this.canales=panchat.getChannelList();
		modelo=new ChatRoomListAbstractTableModel(canales);
		
		central=new PanelCentral(panchat);
		can=new PanelCanales(panchat);
		
		this.addTab("Principal", central);
		this.addTab("canales", can);
	}
	
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



		Panchat panchat=new Panchat("Javier");
		panchat.accionInscribirCanal("canalLocos");

		jframe.add(new PanelPrincipal("Javier",panchat));



		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}
}
