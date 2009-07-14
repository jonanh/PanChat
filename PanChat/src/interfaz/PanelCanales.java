package interfaz;



import java.awt.BorderLayout;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.addressing.Canal;
import panchat.addressing.ListaCanales;
import panchat.addressing.ListaUsuarios;
import panchat.addressing.Usuario;



public class PanelCanales extends MiPanel{
	ListaCanales canales;

	public PanelCanales (ListaCanales canales){
		super();
		acciones(canales);
	}

	public PanelCanales (ListaCanales canales, String ruta){
		super(ruta);
		acciones(canales);
	}


	private void acciones(ListaCanales canales){

		this.canales=canales;

		JTable tabla=new JTable(canales);

		/*
		 * Establecemos algunos parametros de la tabla.
		 */
		ListSelectionModel selectionModel = tabla.getSelectionModel();
		selectionModel.setSelectionInterval(0, 0);
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		/*
		 * Añadimos la tabla a un JScroll y lo añadimos a este panel.
		 */
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(tabla), BorderLayout.CENTER);

		JButton crearConversacion=new JButton("Crear conversacion");
		this.add(crearConversacion,BorderLayout.SOUTH);
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





		jframe.add(new PanelCanales(canales));



		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}

}
