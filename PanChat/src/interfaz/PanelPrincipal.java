package interfaz;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import panchat.addressing.Canal;
import panchat.addressing.ListaCanales;
import panchat.addressing.ListaUsuarios;
import panchat.addressing.Usuario;

public class PanelPrincipal extends JTabbedPane{
	PanelCentral central;
	PanelCanales can;
	
	ListaCanales canales;
	String name;
	
	public PanelPrincipal(String name, ListaCanales canales){
		this.name=name;
		this.canales=canales;
		
		central=new PanelCentral(name);
		can=new PanelCanales(canales);
		
		this.addTab("Principal", central);
		this.addTab("canales", can);
	}
	
	public static void main(String[] args) {
		// Obtenemos referencias a las clases Singleton
		ListaUsuarios usuarios = new ListaUsuarios();
		ListaCanales canales = new ListaCanales();

		// Creamos un listado de usuarios
		LinkedList<Usuario> listaUsuarios = new LinkedList<Usuario>();
		listaUsuarios.add(new Usuario("127.0.0.1", 50000, "JonAn"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50001, "Javier"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50002, "Dennis"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50003, "Imanol"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50004, "Nagore"));

		// Creamos un listado de canales
		LinkedList<Canal> listaCanales = new LinkedList<Canal>();
		listaCanales.add(new Canal("Locos", listaUsuarios));
		listaCanales.add(new Canal("Intrepidos", listaUsuarios.subList(1, 3)));
		listaCanales
		.add(new Canal("Programadores", listaUsuarios.subList(2, 4)));

		// Registramos el listado de usuarios en la clase Singleton Conexiones
		for (Usuario address : listaUsuarios)
			usuarios.añadirUsuario(address);

		// Registramos el listado de usuarios en la clase Singleton Canales
		for (Canal canal : listaCanales)
			canales.añadirCanal(canal);

		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);





		jframe.add(new PanelPrincipal("Javier",canales));



		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}
}