package interfaz;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Vector;

public class Conversacion extends MiPanel implements KeyListener{
	
	Editor escritura;
	Editor log;
	
	HashMap<String,String> hash;
	Vector<Character> lectura;
	
	public Conversacion(HashMap<String,String> tabla){
		super();
		construir(tabla);
	}
	
	public Conversacion(String ruta, HashMap<String,String> tabla){
		super(ruta);
		construir(tabla);
	}
	
	private void construir(HashMap<String,String> tabla){
		hash=tabla;
		
		escritura=new Editor("cuadro.jpg",hash);
		log=new Editor("pizarra.png",hash);
		
		
		this.setLayout(new GridLayout(2,1));
		this.add(log);
		this.add(escritura);
		
		añadirEscuchas();
		
	}
	
	private void añadirEscuchas(){
		escritura.addKeyListener(this);
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getKeyChar()=='\n'){
			//coger mensaje, ponerlo en log y enviarlo
			lectura=escritura.obtenerContenido();
			escritura.setText(null);
			log.escribirVector(lectura);
			lectura.removeAllElements();
		}
		
	}
	
	
	
	public static void main(String[] args){
		VentanaBase in=new VentanaBase();
		HashMap<String,String> e=new HashMap<String,String>(100);
		String f="xd";
		e.put(f,"xd.gif");
		e.put("nubes", "nubes.jpg");
		e.put("pizarra","pizarra.png");
		
		
		
		Conversacion edit=new Conversacion(e);
		in.add(edit);
		in.setVisible(true);
		
	}

	
}
