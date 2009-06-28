package interfaz;

import javax.swing.JFrame;

public class Inicio extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Inicio(){
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(400,400);
	
	}
	
	public static void main(String[] args){
		new Inicio();
	}
}
