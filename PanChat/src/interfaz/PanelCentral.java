package interfaz;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;




public class PanelCentral extends MiPanel implements MouseListener{
	
	//tabla hash con las rutas de los emoticonos
	static HashMap<String,String> emoticonos=new HashMap<String,String>();
	
	PanelRecuadro imagen=new PanelRecuadro("nubes.jpg");
	MiPanel nick=new MiPanel();
	MiPanel usuarios=new MiPanel();
	
	
	JScrollPane scroll=new JScrollPane(usuarios);
	
	Datos nickname;
	Datos estado;
	
	DefaultListModel modelo=new DefaultListModel();
	JList listaUsuarios=new JList(modelo);
	
	public PanelCentral(String nombre){
		super();
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		
		
		
		
		
		nickname=new Datos(nombre);
		estado=new Datos("conectado");
		
		nick.setLayout(new GridLayout(2,1));
		nick.add(nickname);
		nick.add(estado);
		
		
	
		
		
		int i=0;
		for(;i<10;i++){
			modelo.add(i, new String("usuario"+i));
			
		}
		
		

			
		listaUsuarios.addMouseListener(this);
		
		usuarios.add(listaUsuarios);
		
		GridBagConstraints c=new GridBagConstraints();
		
		
		c.insets=new Insets(2,2,2,2);
		c.weightx=0.15;
		c.weighty=0.6;
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=2;
		c.gridheight=2;
		c.anchor=GridBagConstraints.CENTER;
		c.fill=GridBagConstraints.BOTH;
		this.add(imagen,c);
		
		c.weightx=0.2;
		c.weighty=0.2;
		c.gridx=2;
		c.gridy=0;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.gridheight=GridBagConstraints.RELATIVE;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.NONE;
		c.insets=new Insets(40,15,40,2);
		this.add(nickname,c);
		
		c.weightx=0.2;
		c.weighty=0.2;
		c.gridy=1;
		c.anchor=GridBagConstraints.SOUTH;
		this.add(estado,c);
		
		c.insets=new Insets(5,4,5,3);
		c.weightx=1;
		c.weighty=1;
		c.gridy=2;
		c.gridx=0;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.gridheight=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.EAST;
		c.fill=GridBagConstraints.BOTH;
		this.add(scroll,c);
	    cargarEmoticonos();
		

		
	}
	
	static HashMap<String,String> obtenerEmoticonos(){
		return emoticonos;
	}
	
	static boolean estaEmoticon(String clave){
		return emoticonos.containsKey(clave);
	}
	
	static String obtenerRuta(String clave){
		return emoticonos.get(clave);
	}
	
	public void cargarEmoticonos(){
		String f="xd";
		emoticonos.put(f,"xd.png");
		System.out.println(estaEmoticon(f));
	}
	
	public void paint(Graphics g){
		
		
		super.paint(g);
		
	}

	public static void main(String[] args){
		VentanaBase in=new VentanaBase();
		PanelCentral central=new PanelCentral("Javier");
		in.add(central);
		in.setVisible(true);
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		if(arg0.getClickCount()==2){
		System.out.println(( modelo.getElementAt(listaUsuarios.getSelectedIndex())));
		new VentanaBase().add(new Conversacion(emoticonos));
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



	
	

}
