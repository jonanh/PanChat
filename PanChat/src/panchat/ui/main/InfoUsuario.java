package panchat.ui.main;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InfoUsuario extends JPanel {

	private static final long serialVersionUID = 1L;

	public InfoUsuario() {
		JLabel label = new JLabel("Nombre de usuario :");
		
		JTextField textbox = new JTextField();

		JButton boton = new JButton("Conectar");

		Box hbox = Box.createHorizontalBox();
		hbox.add(label);
		hbox.add(textbox);
		hbox.add(boton);

		setLayout(new BorderLayout());
		add(hbox, BorderLayout.NORTH);
	}
}
