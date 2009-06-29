package interfaz;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MiPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected double posX,posY;
	protected double tamX,tamY;
	Rectangle dimensiones;
	ImageIcon icono;
	Image imagen;
	boolean hayImagen=false;
	
	public MiPanel (){
		super();
	}
	
	public MiPanel(String ruta){
		URL url=this.getClass().getResource(ruta);
		System.out.println(url);
		icono=new ImageIcon(url);
		imagen=icono.getImage();
		this.setOpaque(true);
		hayImagen=true;
		this.setPreferredSize(new Dimension(50,50));
		this.setSize(new Dimension(50,50));
	}
	public void paint(Graphics g){
		
		dimensiones=this.getBounds();
		
		posX=dimensiones.getX();
		posY=dimensiones.getY();
		
		tamX=dimensiones.getWidth();
		tamY=dimensiones.getHeight();
		
		if(hayImagen){
			g.drawImage(imagen, (int)posX, (int)posY, (int)(posX+tamX), (int)(posY+tamY), this);
			this.setOpaque(false);
			
		}
			
		super.paint(g);
		
		
		
	}
	
	
	
	public static void main(String[] args){
		VentanaBase in=new VentanaBase();
		JButton boton =new JButton();
		MiPanel panel=new MiPanel();
		panel.add(boton);
		in.add(panel);
	}
}
