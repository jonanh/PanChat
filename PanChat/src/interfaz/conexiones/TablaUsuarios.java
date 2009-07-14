package interfaz.conexiones;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import panchat.addressing.ListaUsuarios;

public class TablaUsuarios extends JPanel {

	private static final long serialVersionUID = 1L;

	public TablaUsuarios(ListaUsuarios listaUsuarios) {

		/*
		 * Creamos la tabla con el modelo de datos proporcionado por
		 * DatosTablaFicheros.
		 */
		JTable table = new JTable(listaUsuarios);

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
		this.add(new JScrollPane(table));
		
		/*
		 * Añadimos un botón para iniciar conversación
		 */
		JButton boton = new JButton("Iniciar conversación");
		this.add(boton, BorderLayout.SOUTH);
	}
}