package interfaz.ventanas;

import interfaz.elementos.Datos;
import interfaz.paneles.MiPanel;
import interfaz.paneles.PanelCentral;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Inicio extends VentanaBase implements ActionListener {

	private static final long serialVersionUID = 1L;

	MiPanel panel;
	Datos nombre;
	JButton boton = new JButton("Conectar");;

	public Inicio() {
		super();
		construir();

	}

	public Inicio(String name) {

		super(name);
		construir();

	}

	private void construir() {
		panel = new MiPanel("init.jpg");
		// para el nombre que quiere el usuario
		nombre = new Datos("an�nimo");
		// panel que contiene los elementos

		panel.add(nombre);
		panel.add(boton);
		this.add(panel);

		añadirEscuchas();
		nombre.cambiarColor(Color.red);
		nombre.cambiarTamañoLabel(27);
	}

	public void añadirEscuchas() {
		boton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// lo que haya que hacer al conectar
		VentanaBase ventana = new VentanaBase("Bienvenido "
				+ nombre.obtenerTexto());
		PanelCentral panel = new PanelCentral(nombre.obtenerTexto());
		ventana.add(panel);

	}

	public static void main(String[] args) {
		new Inicio("Bienvenido a Panchat").setVisible(true);
	}

}
