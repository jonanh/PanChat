package panchat.ui.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import panchat.Panchat;
import panchat.data.Usuario;

public class PanchatUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private Panchat panchat;

	public PanchatUI() {

		final JFrame frame = this;

		String nombreUsuario = null;

		/*
		 * Mientras el usuario nos rellene el InputDialog vacio o nos pulse
		 * cancelar, le pedimos nuevamente un nombre de usuario
		 */
		while (nombreUsuario == null || nombreUsuario.length() == 0) {
			nombreUsuario = JOptionPane.showInputDialog(frame,
					"Introduzca su nickName", "Panchat",
					JOptionPane.INFORMATION_MESSAGE);
		}

		// Creamos un nuevo usuario con el nick leido
		Usuario user = new Usuario(nombreUsuario);

		// Creamos una nueva clase panchat
		panchat = new Panchat(user);

		// Cambiamos el listener de la ventana para gestionar nosotros la
		// correcta terminación de la aplicación
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				panchat.accionDesegistrarCliente();
				System.exit(0);
			}
		});

		// Creamos un panel y añadimos los paneles de usuarios y canales
		JTabbedPane pane = new JTabbedPane();

		pane.addTab("Usuarios", new TablaUsuarios(panchat));

		pane.addTab("Canales", new TablaCanales(panchat));

		// Añadimos el panel al contenido de la ventana
		this.getContentPane().add(pane);

		setTitle(nombreUsuario);

		pack();

		setSize(750, 500);
		setVisible(true);
	}

	public static void main(String[] args) {

		new PanchatUI();

	}
}
