package interfaz.ventanas;

import interfaz.elementos.Datos;
import interfaz.paneles.MiPanel;

import interfaz.paneles.PanelPrincipal;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;

import panchat.Panchat;



public class Inicio extends VentanaBase implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	static String camino ="/interfaz/imagenes/";

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
		panel = new MiPanel(camino+"init.jpg");
		// para el nombre que quiere el usuario
		nombre = new Datos("anonimo");
		// panel que contiene los elementos

		panel.add(nombre);
		panel.add(boton);
		this.add(panel);

		anyadirEscuchas();
		nombre.cambiarColor(Color.red);
		nombre.cambiarTamanyoLabel(27);
	}

	public void anyadirEscuchas() {
		boton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// lo que haya que hacer al conectar
		
		Panchat panchat=new Panchat(nombre.obtenerTexto());
		VentanaBase ventana = new VentanaBase("Bienvenido "
				+ nombre.obtenerTexto());
		
		PanelPrincipal panel = new PanelPrincipal(nombre.obtenerTexto(),panchat);
		ventana.add(panel);

	}

	public static void main(String[] args) {
		new Inicio("Bienvenido a Panchat").setVisible(true);
	}

}
