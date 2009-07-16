package interfaz.conexiones;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.addressing.channels.ListaCanales;

public class TablaCanales extends JPanel {

	private static final long serialVersionUID = 1L;

	public TablaCanales(ListaCanales listaCanales) {

		/*
		 * Creamos la tabla con el modelo de datos proporcionado por
		 * DatosTablaFicheros.
		 */
		JTable table = new JTable(listaCanales);

		/*
		 * Establecemos algunos parametros de la tabla.
		 */
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.setSelectionInterval(0, 0);
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		/*
		 * Añadimos la tabla a un JScroll y lo añadimos a este panel.
		 */
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(table), BorderLayout.CENTER);

		/*
		 * Añadimos un botón para entrar en un canal y otro boton para crear una
		 * nueva conversacion.
		 */
		JButton boton1 = new JButton("Entrar en conversación");
		JButton boton2 = new JButton("Crear nueva conversación");

		Box hbox = Box.createHorizontalBox();
		hbox.add(boton1);
		hbox.add(boton2);

		this.add(hbox, BorderLayout.SOUTH);
	}
}