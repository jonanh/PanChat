package panchat.ui.tests;

import javax.swing.JFrame;

import panchat.ui.main.InfoUsuario;

public class TestInfoUsuario extends JFrame {

	private static final long serialVersionUID = 1L;

	public TestInfoUsuario() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().add(new InfoUsuario());

		pack();

		setSize(750, 500);
		setVisible(true);
	}

	public static void main(String[] args) {
		new TestInfoUsuario();
	}
}
