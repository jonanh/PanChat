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
import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.models.ChatRoomListAbstractTableModel;

public class ChannelTable extends JPanel {

	private static final long serialVersionUID = 1L;

	private final ChatRoomList channelList;

	private JTable table;

	private Panchat panchat;

	/**
	 * Crea un JPanel con una tabla de usuarios
	 * 
	 * @param panchat
	 */
	public ChannelTable(Panchat panchat) {
		this(panchat.getChannelList());

		this.panchat = panchat;
	}

	/**
	 * Crea un JPanel con una tabla de usuarios
	 * 
	 * @param pChannelList
	 */
	public ChannelTable(ChatRoomList pChannelList) {

		this.channelList = pChannelList;

		// Creamos la tabla con el modelo de datos proporcionado por
		// DatosTablaFicheros.

		table = new JTable(new ChatRoomListAbstractTableModel(channelList));

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
				ChatRoom canal = channelList.getChannel(table.getSelectedRow());

				if (canal != null)
					panchat.accionIniciarConversacionCanal(canal);
			}
		});

		final JPanel panel = this;

		boton2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Preguntamos el nombre que deseamos dar al canal.
				 */
				String input = JOptionPane.showInputDialog(panel,
						"Introduzca el nombre del nuevo canal", "Panchat",
						JOptionPane.INFORMATION_MESSAGE);

				// Si obtenemos un nombre decente :-P
				if (input != null && input.length() > 0) {

					panchat.accionInscribirCanal(input);
				}
			}
		});
	}
}