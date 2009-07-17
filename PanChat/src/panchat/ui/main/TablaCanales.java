package panchat.ui.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.addressing.channels.ListaCanales;
import panchat.addressing.channels.ListaCanalesAbstractTableModel;

public class TablaCanales extends JPanel {

	private static final long serialVersionUID = 1L;

	private final ListaCanales listaCanales;

	private JTable table;

	public TablaCanales(ListaCanales pListaCanales) {

		this.listaCanales = pListaCanales;

		// Creamos la tabla con el modelo de datos proporcionado por
		// DatosTablaFicheros.

		table = new JTable(new ListaCanalesAbstractTableModel(listaCanales));

		// Establecemos algunos parametros de la tabla.

		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.setSelectionInterval(0, 0);
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Añadimos la tabla a un JScroll y lo añadimos a este panel.

		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(table), BorderLayout.CENTER);

		// Añadimos un botón para entrar en un canal y otro boton para crear una
		// nueva conversacion.

		JButton boton1 = new JButton("Entrar en conversación");
		JButton boton2 = new JButton("Crear nueva conversación");

		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(1, 2));

		panelBotones.add(boton1);
		panelBotones.add(boton2);

		this.add(panelBotones, BorderLayout.SOUTH);

		// Añadimos los event listeners

		boton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(listaCanales
						.getCanal(table.getSelectedRow()));
			}
		});

		boton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(listaCanales
						.getCanal(table.getSelectedRow()));
			}
		});
	}
}