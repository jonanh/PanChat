package interfaz.conexiones;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import panchat.addressing.Conexiones;

public class TablaConexiones extends JPanel {

	private static final long serialVersionUID = 1L;

	public TablaConexiones() {

		/*
		 * Creamos la tabla con el modelo de datos proporcionado por
		 * DatosTablaFicheros.
		 */
		JTable table = new JTable(Conexiones.getInstanceOf());

		/*
		 * Establecemos algunos parametros de la tabla.
		 */
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.setSelectionInterval(0, 0);
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		/*
		 * A�adimos la tabla a un JScroll y lo a�adimos a este panel.
		 */
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(table));

	}

	public static void main(String[] args) {

		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane pane = new JTabbedPane();

		/*
		 * A�adimos el panel TablaFicheros.
		 */
		pane.addTab("Ficheros descargables", new TablaConexiones());

		/*
		 * A�adimos el panel2
		 */
		pane.addTab("Ficheros descargados", new JPanel());

		jframe.getContentPane().add(pane);

		jframe.pack();

		jframe.setSize(750, 500);
		jframe.setVisible(true);
	}
}