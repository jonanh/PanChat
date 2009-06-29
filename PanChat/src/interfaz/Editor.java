package interfaz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextPane;


public class Editor extends JTextPane implements KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	HashMap <String,String> hash;
	Vector<Character> teclasPulsadas=new Vector<Character>();
	String path;
	ImageIcon icon;
	Image imagen;
	
	int posActual,posAnterior;
	boolean delete=false;
	
	char[] clave;
	String ruta;
	
	public Editor(String ruta, HashMap<String,String> hash){
		super();
		this.setOpaque(false);
		path=ruta;
		this.hash=hash;
		icon=new ImageIcon(this.getClass().getResource(path));
		imagen=icon.getImage();
		this.setForeground(Color.red);
		this.setText("\n     ");
		posActual=getCaretPosition();
		posAnterior=posActual;
		this.addKeyListener(this);
		//this.insertIcon(new ImageIcon("D:\\Java\\PanChat\\src\\interfaz\\xd.gif"));
	}
	
	public void paint(Graphics g){
		g.drawImage(imagen,this.getX(),this.getY(),this.getWidth(),this.getHeight(),this);
		super.paint(g);
	}
	
	

	@Override
	public void keyPressed(KeyEvent arg0) {
	
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		int lon=this.getStyledDocument().getLength();
		System.out.print(getCaretPosition()+" ");
		
		char tecla=arg0.getKeyChar();
		
		
		if(tecla=='\n'){
			this.setText(this.getText()+"     ");
			teclasPulsadas.removeAllElements();
		}
		else{
		posAnterior=posActual;
		posActual=getCaretPosition();
		System.out.println("ant: "+posAnterior+" Act: "+posActual);
		
		//esto es para detectar cuando borramos una letra
		if(posActual<posAnterior)
			delete=true;
		else if(posActual==posAnterior && delete)
			delete=false;
		else if(posActual==posAnterior && !delete)
			delete=true;
		
		
		if (delete)
			teclasPulsadas.removeElementAt(teclasPulsadas.size()-1);
		else if(tecla==' ')
			teclasPulsadas.removeAllElements();
		else 
			teclasPulsadas.addElement(tecla);
		}
		
		
		clave=obtenerClave(teclasPulsadas);
		String clave2=new String(clave);
		System.out.println("clave2 : "+clave2);
		
		if(hash.containsKey(clave2)){
			
			ruta=hash.get(clave2);
			
			String abs="D:\\Java\\PanChat\\src\\interfaz\\"+ruta;
			
			ImageIcon emoticon=new ImageIcon(abs);
			
			this.insertIcon(new ImageIcon( emoticon.getImage().getScaledInstance(25, 25, 0)));
			teclasPulsadas.removeAllElements();
			/*
			this.setCaretPosition(lon);
			this.insertIcon(emoticon);*/
			
			
			
		}
		
	}
	
	private char[] obtenerClave(Vector<Character> teclas){
		
		char[] clave=new char[teclas.size()];
		int i =0;
		for(;i<=teclas.size()-1;i++){
			char car=teclas.elementAt(i);
			if(car!='[' || car!=']' || car!=' ' || car!=','){
				clave[i]=car;
			}
		}
		
		return clave;
	}
	
	
	
	public static void main(String[] args){
		VentanaBase in=new VentanaBase();
		HashMap<String,String> e=new HashMap<String,String>(100);
		String f="xd";
		e.put(f,"xd.gif");
		e.put("nubes", "nubes.jpg");
		e.put("pizarra","pizarra.png");
		System.out.println("en main "+e.containsKey(f));
		
		Editor edit=new Editor("pizarra.png",e);
		in.add(edit);
		in.setVisible(true);
		
	}
}
