package interfaz.paneles;



import java.awt.BorderLayout;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.data.Canal;
import panchat.data.ListaUsuarios;
import panchat.data.ListaCanales;
import panchat.data.Usuario;
import panchat.data.models.ListaCanalesAbstractTableModel;


public class PanelCanales extends MiPanel{
	
	private static final long serialVersionUID = 1L;
	
	ListaCanalesAbstractTableModel canales;

	public PanelCanales (ListaCanalesAbstractTableModel canales){
		super();
		acciones(canales);
	}

	public PanelCanales (ListaCanalesAbstractTableModel canales, String ruta){
		super(ruta);
		acciones(canales);
	}


	private void acciones(ListaCanalesAbstractTableModel canales){

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
		
		ListaCanales canales = new ListaCanales();
		ListaCanalesAbstractTableModel modelo=new ListaCanalesAbstractTableModel(canales);
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

		// Creamos un listado de canales
		LinkedList<Canal> listaCanales = new LinkedList<Canal>();

		Canal canalLocos = new Canal("Locos", usuarios);
		Canal canalIntrepidos = new Canal("Intrepidos", usuarios);
		Canal canalProgramadores = new Canal("Programadores", usuarios);
		
		for (Usuario usuario : listaUsuarios)
			canalLocos.anyadirUsuarioConectado(usuario);
		for (Usuario usuario : listaUsuarios.subList(1, 3))
			canalIntrepidos.anyadirUsuarioConectado(usuario);
		for (Usuario usuario : listaUsuarios.subList(2, 4))
			canalIntrepidos.anyadirUsuarioConectado(usuario);

		listaCanales.add(canalLocos);
		listaCanales.add(canalIntrepidos);
		listaCanales.add(canalProgramadores);

		// Registramos el listado de usuarios en la clase Singleton Canales
		for (Canal canal : listaCanales)
			canales.añadirCanal(canal);

		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);





		jframe.add(new PanelCanales(modelo));



		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}

}
