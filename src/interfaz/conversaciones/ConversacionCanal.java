package interfaz.conversaciones;

import interfaz.ventanas.VentanaBase;
import interfaz.elementos.Editor;
import interfaz.paneles.MiPanel;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.UserList;
import panchat.data.User;
import panchat.data.models.UsuariosConectadosTableModel;
import panchat.data.models.UsuariosDesconectadosTableModel;



public class ConversacionCanal extends MiPanel implements KeyListener,MouseListener{
	private static final long serialVersionUID = 1L;
	
	
	ChatRoom canal;

	Editor escritura;
	Editor log;

	
	Vector<Character> lectura;
	
	JPanel informacion;
	
	JTable usuariosConectados;
	JTable usuariosNoConectados;
	
	
	UsuariosConectadosTableModel modeloConectados;
	UsuariosDesconectadosTableModel modeloDesconectados;
	
	static String camino= "/interfaz/imagenes/";
	
	public ConversacionCanal(ChatRoom canal) {
		super();
		construir(canal);
	}

	public ConversacionCanal(String ruta,UserList usuarios) {
		super(camino+ruta);
		construir(canal);
	}

	private void construir(ChatRoom canal) {

		this.canal=canal;
		modeloConectados= new UsuariosConectadosTableModel(canal);
		modeloDesconectados= new UsuariosDesconectadosTableModel(canal);
			
		

		escritura = new Editor(camino+"cuadro.jpg");

		log = new Editor(camino+"pizarra.png");
		log.setEditable(false);

		JScrollPane arriba = new JScrollPane(log);
		JScrollPane abajo = new JScrollPane(escritura);
		
		informacion=new JPanel();
		
		usuariosConectados=new JTable(modeloConectados);
		usuariosNoConectados=new JTable(modeloDesconectados);
		
		informacion.setLayout(new BorderLayout());
		informacion.add(usuariosConectados,BorderLayout.CENTER);
		informacion.add(usuariosNoConectados,BorderLayout.SOUTH);
		

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridheight = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.7;
		c.weighty = 0.7;
		this.add(arriba, c);
		
		c.gridheight = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0.05;
		c.weighty = 0.1;
		this.add(informacion,c);

		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.weightx = 0.2;
		c.weighty = 0.2;
		this.add(abajo, c);

		anyadirEscuchas();

		this.setOpaque(false);

	}

	// si no redefino el m�todo poniendo escritura.paint no me pinta la imagen
	// deescritura

	public void paint(Graphics g) {
		escritura.paint(g);
		super.paint(g);

	}

	private void anyadirEscuchas() {
		escritura.addKeyListener(this);
		usuariosNoConectados.addMouseListener(this);
	}

	
	
	
	
	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getKeyChar() == '\n') {
			// coger mensaje, ponerlo en log y enviarlo
			lectura = escritura.obtenerContenido();
			escritura.setText(null);
			log.escribirVector(lectura);
			lectura.removeAllElements();
		}

	
	}
	
	
	public static void main(String[] args) {
		VentanaBase in = new VentanaBase();
		HashMap<String, String> e = new HashMap<String, String>(100);
		String f = "xd";
		e.put(f, "xd.gif");
		e.put("nubes", "nubes.jpg");
		e.put("pizarra", "pizarra.png");
		
		ChatRoomList listaCanal = new ChatRoomList();
		
		// Obtenemos referencias a las clases Singleton
		UserList usuarios = new UserList(listaCanal);
		
		// Creamos un listado de usuarios
		LinkedList<User> listaUsuarios = new LinkedList<User>();
		listaUsuarios.add(new User("127.0.0.1", 50000, "JonAn"));
		listaUsuarios.add(new User("127.0.0.1", 50001, "Javier"));
		listaUsuarios.add(new User("127.0.0.1", 50002, "Dennis"));
		listaUsuarios.add(new User("127.0.0.1", 50003, "Imanol"));
		listaUsuarios.add(new User("127.0.0.1", 50004, "Nagore"));
		listaUsuarios.add(new User("127.0.0.1", 50008, "Paco"));
		listaUsuarios.add(new User("127.0.0.1", 50004, "Loles"));
		listaUsuarios.add(new User("127.0.0.1", 50005, "Luis mari"));
		listaUsuarios.add(new User("127.0.0.1", 50006, "G"));
		listaUsuarios.add(new User("127.0.0.1", 50007, "JonAn"));
		
		// Registramos el listado de usuarios en la clase Singleton Conexiones
		for (User address : listaUsuarios)
			usuarios.add(address);
		
		ChatRoom canal=new ChatRoom("cacas",usuarios);
		
		// Añadimos los usuarios al canal
		for (User usuario : listaUsuarios.subList(0, 5))
			canal.joinUser(usuario);
		
		

		ConversacionCanal edit = new ConversacionCanal(canal);
		in.add(edit);
		in.setTitle("conversación con el jonan");
		in.setVisible(true);

	}



	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println(arg0.getSource());
		System.out.println(usuariosNoConectados.getValueAt(usuariosNoConectados.getSelectedRow(), usuariosNoConectados.getSelectedColumn()));
		
		
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
