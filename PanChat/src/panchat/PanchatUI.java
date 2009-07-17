package panchat;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import panchat.Panchat;
import panchat.ui.main.TablaUsuarios;
import panchat.users.Usuario;

public class PanchatUI extends JFrame {

	private static final long serialVersionUID = 1L;

	final Panchat panchat;
	
	public PanchatUI() {

		Usuario user = new Usuario("127.0.0.1", 50003, "XXX");
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
