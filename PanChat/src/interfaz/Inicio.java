package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Inicio extends VentanaBase implements ActionListener {
	
	MiPanel panel;
	Datos nombre;
	JButton boton=new JButton("Conectar");;
	
	public Inicio(){
		super();
		construir();
		
	}
	
	public Inicio(String name){
		
		super(name);
		construir();
		
	}
	
	private void construir(){
		panel=new MiPanel("init.jpg");
		//para el nombre que quiere el usuario
		nombre=new Datos("an�nimo");
		//panel que contiene los elementos
		
		
		panel.add(nombre);
		panel.add(boton);
		this.add(panel);
		
		a�adirEscuchas();
		nombre.cambiarColor(Color.red);
		nombre.cambiarTama�oLabel(27);
	}
	
	
	public void a�adirEscuchas(){
		boton.addActionListener(this);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//lo que haya que hacer al conectar
		this.dispose();
		
	}
	
	public static void main(String[] args){
		new Inicio("Bienvenido a Panchat");
	}

	
}
