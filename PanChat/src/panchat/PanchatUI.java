package panchat;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import panchat.Panchat;
import panchat.ui.main.TablaUsuarios;
import panchat.users.Usuario;

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

		getContentPane().add(new TablaUsuarios(panchat));

		pack();

		setSize(750, 500);
		setVisible(true);
	}

	public static void main(String[] args) {
		new PanchatUI();
	}
}
