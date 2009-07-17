package panchat.ui.channel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.addressing.channels.Canal;
import panchat.addressing.channels.CanalComboBoxModel;
import panchat.addressing.channels.CanalTableModel;

public class PanelCanal extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;

	private JComboBox JComboBox;

	private Canal canal;

	public PanelCanal(Canal pCanal) {

		canal = pCanal;

		// Creamos la tabla con el modelo de datos proporcionado por
		// DatosTablaFicheros.

		table = new JTable(new CanalTableModel(canal));

		// Establecemos algunos parametros de la tabla.

		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.setSelectionInterval(0, 0);
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Añadimos la tabla a un JScroll y lo añadimos a este panel.

		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(table), BorderLayout.CENTER);

		// Creamos un JCombobox para añadir a nuevos usuarios

		JComboBox = new JComboBox(new CanalComboBoxModel(canal));

		JButton boton = new JButton("Invitar");
		boton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// canal.anyadirUsuario(usuario)
			}
		});

		// Creamos un JPanel con un GridLayout y añadimos el JComboBox y el
		// botón

		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(2, 1));

		panelBotones.add(JComboBox);
		panelBotones.add(boton);

		this.add(panelBotones, BorderLayout.SOUTH);
		
		System.out.println(this.getPreferredSize());
		//this.setPreferredSize(preferredSize)
		
	}
}