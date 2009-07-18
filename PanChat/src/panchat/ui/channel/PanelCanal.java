package panchat.ui.channel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.Panchat;
import panchat.data.Canal;
import panchat.data.models.CanalComboBoxModel;
import panchat.data.models.CanalTableModel;
import panchat.share.protocolo.UsuarioCanal;

public class PanelCanal extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;

	private JComboBox JComboBox;

	private Canal canal;

	private Panchat panchat;

	/**
	 * Crea un nuevo panel canal
	 * 
	 * @param panchat
	 * @param pCanal
	 */
	public PanelCanal(Panchat panchat, Canal pCanal) {
		this(pCanal);
		this.panchat = panchat;
	}

	/**
	 * Crea un nuevo panel canal
	 * 
	 * @param pCanal
	 */
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
				UsuarioCanal usuarioCanal = new UsuarioCanal(panchat
						.getUsuario(), canal, true);
				panchat.getConnector().escribirMultiCastSocket(usuarioCanal);
			}
		});

		// Creamos un JPanel con un GridLayout y añadimos el JComboBox y el
		// botón

		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(2, 1));

		panelBotones.add(JComboBox);
		panelBotones.add(boton);

		this.add(panelBotones, BorderLayout.SOUTH);

		// Configuramos el prefered size como 200 de ancho y máximo de altura
		this.setPreferredSize(new Dimension(200, Short.MAX_VALUE));

	}
}