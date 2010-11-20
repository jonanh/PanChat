package interfaz.paneles;



import java.awt.BorderLayout;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.Panchat;

import panchat.data.models.ChatRoomListAbstractTableModel;


public class PanelCanales extends MiPanel{
	
	private static final long serialVersionUID = 1L;
	
	ChatRoomListAbstractTableModel canales;
	
	Panchat panchat;

	public PanelCanales (Panchat panchat){
		super();
		acciones(panchat);
	}

	public PanelCanales (String ruta,Panchat panchat){
		super(ruta);
		acciones(panchat);
	}


	private void acciones(Panchat panchat){
		
		this.panchat=panchat;
		
		canales=new ChatRoomListAbstractTableModel(panchat.getChannelList());

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
		Panchat panchat=new Panchat("kk");
		
		panchat.accionInscribirCanal("balones");
		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);





		jframe.add(new PanelCanales(panchat));



		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}

}
