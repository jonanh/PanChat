package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;

public class Datos extends JPanel implements MouseListener,KeyListener,FocusListener{
	//esta clase consta de un jLabel que contiene un dato y cuando se clica aparece una caja e texto
	//como el estado personal del msn
	JLabel label=new JLabel();
	JTextField texto=new JTextField();
	int tamaño=7;
	
	
	public Datos(String defecto){
		
		super();
		this.setOpaque(false);
		//ponemos coomo layout el overlaylayout que nos permite superponer componetnes
		this.setLayout(new OverlayLayout(this));
		
		añadirComponentes(defecto);
		
		añadirEscuchas();
		
		
	}
	
	public Datos (String defecto, int tam){
		this(defecto);
		tamaño=tam;
	}
	
	private void añadirComponentes(String defecto){
		this.add(label);
		this.add(texto);
		
		texto.setColumns(tamaño);
		texto.setVisible(false);
		label.setText(defecto);
	}
	
	private void añadirEscuchas(){
		label.addMouseListener(this);
		texto.addMouseListener(this);
		texto.addKeyListener(this);
		texto.addFocusListener(this);
	}


	public void modificarTamaño(int tam){
		texto.setColumns(tam);
	}
	
	public String obtenerTexto(){
		return label.getText();
	}
	
	public void cambiarColor (Color color){
		label.setForeground(color);
	}
	
	public void cambiarTamañoLabel(int tam){
		label.setFont(new Font("Times New Roman",Font.BOLD,tam));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		//quien produce el evento
		if(arg0.getSource()==label){
			label.setVisible(false);
			texto.setVisible(true);
			//pedir el focus
			texto.requestFocus();
		}
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	
	
	//métodos de keyListener
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyChar()=='\n'){
			label.setText(texto.getText());
			texto.setVisible(false);
			label.setVisible(true);
			
		}
		
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public static void main(String[] args){
		VentanaBase frame=new VentanaBase();
		Datos ejemplo=new Datos("ejemplo");
		frame.add(ejemplo);
	}

	
	//si no escribimos ningún nombre si no que simplemento clickamos en otro componente
	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		texto.setVisible(false);
		label.setVisible(true);
	}
	

}
