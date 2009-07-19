package panchat.ui.chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.Panchat;
import panchat.data.Canal;
import panchat.data.Usuario;
import panchat.data.models.UsuariosConectadosTableModel;
import panchat.data.models.UsuariosDesconectadosTableModel;

public class PanelCanal extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable usuariosConectados;

	private JTable usuariosDesconectados;

	private UsuariosDesconectadosTableModel usuariosDesconectadosModel;

	private final Canal canal;

	private Panchat panchat;

	/**
	 * Crea un nuevo panel canal
	 * 
	 * @param panchat
	 * @param pCanal
	 */
	public PanelCanal(Panchat pPanchat, Canal pCanal) {
		this(pCanal);
		this.panchat = pPanchat;
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

		usuariosConectados = new JTable(new UsuariosConectadosTableModel(canal));

		// Guardamos el modelo de los usuarios desconectados para usarlo en el
		// ActionListener del boton
		usuariosDesconectadosModel = new UsuariosDesconectadosTableModel(canal);

		// Crreamos la tabla de desconectados
		usuariosDesconectados = new JTable(usuariosDesconectadosModel);

		// Establecemos algunos parametros de la tabla, para que solo se pueda
		// seleccionar 1 fila a la vez

		ListSelectionModel selectionModel = usuariosConectados
				.getSelectionModel();
		selectionModel.setSelectionInterval(0, 0);
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		selectionModel = usuariosDesconectados.getSelectionModel();
		selectionModel.setSelectionInterval(0, 0);
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Añadimos la tabla a un JScroll y lo añadimos a este panel.

		this.setLayout(new GridLayout(2, 1));

		this.add(new JScrollPane(usuariosConectados));

		JButton boton = new JButton("Invitar");
		boton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int filaSeleccionada = usuariosDesconectados.getSelectedRow();

				Usuario usuario = (Usuario) usuariosDesconectadosModel
						.getValueAt(filaSeleccionada, 0);

				if (usuario != null) {
					panchat.invitarUsuario(canal, usuario);
				}
			}
		});

		// Creamos un JPanel con un GridLayout y añadimos el JComboBox y el
		// botón

		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new BorderLayout());

		panelBotones.add(new JScrollPane(usuariosDesconectados),
				BorderLayout.CENTER);
		panelBotones.add(boton, BorderLayout.SOUTH);

		this.add(panelBotones);

		// Configuramos el prefered size como 200 de ancho y máximo de altura
		this.setPreferredSize(new Dimension(200, Short.MAX_VALUE));

	}
}