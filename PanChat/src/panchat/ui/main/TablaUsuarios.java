package panchat.ui.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.Panchat;
import panchat.data.ListaUsuarios;
import panchat.data.models.UsuarioTablaModel;

public class TablaUsuarios extends JPanel {

	private static final long serialVersionUID = 1L;

	private ListaUsuarios listaUsuarios;

	private JTable tabla;

	private JButton boton;

	/**
	 * Crea un JPanel con una tabla de usuarios
	 * 
	 * @param panchat
	 */
	public TablaUsuarios(Panchat panchat) {
		this(panchat.getListaUsuarios());
	}

	/**
	 * Crea una JPanel con una tabla de usuarios
	 * 
	 * @param listaUsuarios
	 */
	public TablaUsuarios(ListaUsuarios listaUsuarios) {

		this.listaUsuarios = listaUsuarios;

		// Creamos la tabla con el modelo de datos proporcionado por
		// DatosTablaFicheros.

		tabla = new JTable(new UsuarioTablaModel(listaUsuarios));

		// Le añadimos el ActionListener

		tabla.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					action();
				}
			}
		});

		// Establecemos algunos parametros de la tabla.

		ListSelectionModel selectionModel = tabla.getSelectionModel();
		selectionModel.setSelectionInterval(0, 0);
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Añadimos la tabla a un JScroll y lo añadimos a este panel.

		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(tabla));

		// Creamos el boton

		boton = new JButton("Iniciar conversación");

		// Le añadimos el ActionListener

		boton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				action();
			}
		});

		// Añadimos un botón para iniciar conversación

		this.add(boton, BorderLayout.SOUTH);
	}

	private void action() {
		System.out.println(listaUsuarios.getUsuario(tabla.getSelectedRow()));
	}
}