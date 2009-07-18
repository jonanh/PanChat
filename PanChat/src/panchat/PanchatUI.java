package panchat;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import panchat.Panchat;
import panchat.data.Usuario;
import panchat.ui.main.TablaCanales;
import panchat.ui.main.TablaUsuarios;

public class PanchatUI extends JFrame {

	private static final long serialVersionUID = 1L;

	final Panchat panchat;

	public PanchatUI() {

		String nombreUsuario = "";

		final JFrame frame = this;

		while (nombreUsuario == null || nombreUsuario.length() == 0) {
			nombreUsuario = JOptionPane.showInputDialog(frame,
					"Introduzca su nickName", "Panchat",
					JOptionPane.INFORMATION_MESSAGE);
		}

		Usuario user = new Usuario(nombreUsuario);

		panchat = new Panchat(user);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				panchat.desegistrarCliente();
				System.exit(0);
			}
		});

		JTabbedPane pane = new JTabbedPane();

		/*
		 * Añadimos el panel TablaFicheros.
		 */
		pane.addTab("Usuarios", new TablaUsuarios(panchat));

		/*
		 * Añadimos el panel2
		 */
		pane.addTab("Canales", new TablaCanales(panchat));

		this.getContentPane().add(pane);

		pack();

		setSize(750, 500);
		setVisible(true);
	}

	public static void main(String[] args) {
		new PanchatUI();
	}
}
