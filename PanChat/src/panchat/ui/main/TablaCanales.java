package panchat.ui.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.Panchat;
import panchat.channels.Canal;
import panchat.channels.ListaCanales;
import panchat.channels.models.ListaCanalesAbstractTableModel;

public class TablaCanales extends JPanel {

	private static final long serialVersionUID = 1L;

	private final ListaCanales listaCanales;

	private JTable table;

	private Panchat panchat;

	/**
	 * Crea un JPanel con una tabla de usuarios
	 * 
	 * @param panchat
	 */
	public TablaCanales(Panchat panchat) {
		this(panchat.getListaCanales());

		this.panchat = panchat;
	}

	/**
	 * Crea un JPanel con una tabla de usuarios
	 * 
	 * @param pListaCanales
	 */
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

		final JPanel panel = this;

		boton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Pedimos mediante un mensaje el numero del juego al que
				 * deseamos jugar
				 */
				String input = JOptionPane.showInputDialog(panel,
						"Introduzca el nombre del nuevo canal", "Panchat",
						JOptionPane.INFORMATION_MESSAGE);

				// Si obtenemos un nombre decente :-P
				if (input != null && input.length() > 0) {
					Canal canal = new Canal(input);
					panchat.getConnector().escribirMultiCastSocket(canal);
				}
			}
		});
	}
}