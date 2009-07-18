package interfaz.conversaciones;

import interfaz.ventanas.VentanaBase;
import interfaz.elementos.Editor;
import interfaz.paneles.MiPanel;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import panchat.data.Canal;
import panchat.data.ListaCanales;
import panchat.data.ListaUsuarios;
import panchat.data.Usuario;



public class ConversacionCanal extends MiPanel implements KeyListener,ActionListener{
	private static final long serialVersionUID = 1L;
	
	
	Canal canal;

	Editor escritura;
	Editor log;

	HashMap<String, String> hash;
	Vector<Character> lectura;
	
	JPanel informacion;
	
	JTable usuariosConectados;
	JComboBox usuariosNoConectados;
	
	public ConversacionCanal(HashMap<String, String> tabla,Canal canal) {
		super();
		construir(tabla,canal);
	}

	public ConversacionCanal(String ruta, HashMap<String, String> tabla,ListaUsuarios usuarios) {
		super(ruta);
		construir(tabla,canal);
	}

	private void construir(HashMap<String, String> tabla,Canal canal) {

		this.canal=canal;
		
		hash = tabla;

		escritura = new Editor("cuadro.jpg", hash);

		log = new Editor("pizarra.png", hash);
		log.setEditable(false);

		JScrollPane arriba = new JScrollPane(log);
		JScrollPane abajo = new JScrollPane(escritura);
		
		informacion=new JPanel();
		
		usuariosConectados=new JTable(canal);
		usuariosNoConectados=new JComboBox(canal);
		
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

		añadirEscuchas();

		this.setOpaque(false);

	}

	// si no redefino el m�todo poniendo escritura.paint no me pinta la imagen
	// deescritura

	public void paint(Graphics g) {
		escritura.paint(g);
		super.paint(g);

	}

	private void añadirEscuchas() {
		escritura.addKeyListener(this);
		usuariosNoConectados.addActionListener(this);
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
		
		ListaCanales listaCanal = new ListaCanales();
		
		// Obtenemos referencias a las clases Singleton
		ListaUsuarios usuarios = new ListaUsuarios(listaCanal);
		
		// Creamos un listado de usuarios
		LinkedList<Usuario> listaUsuarios = new LinkedList<Usuario>();
		listaUsuarios.add(new Usuario("127.0.0.1", 50000, "JonAn"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50001, "Javier"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50002, "Dennis"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50003, "Imanol"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50004, "Nagore"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50008, "Paco"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50004, "Loles"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50005, "Luis mari"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50006, "G"));
		listaUsuarios.add(new Usuario("127.0.0.1", 50007, "JonAn"));
		
		// Registramos el listado de usuarios en la clase Singleton Conexiones
		for (Usuario address : listaUsuarios)
			usuarios.añadirUsuario(address);
		
		Canal canal=new Canal("cacas",usuarios);
		
		// Añadimos los usuarios al canal
		for (Usuario usuario : listaUsuarios.subList(0, 5))
			canal.anyadirUsuarioConectado(usuario);
		
		

		ConversacionCanal edit = new ConversacionCanal(e,canal);
		in.add(edit);
		in.setTitle("conversación con el jonan");
		in.setVisible(true);

	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(arg0.getSource());
		System.out.println(usuariosNoConectados.getSelectedItem());
		System.out.println(usuariosNoConectados.getSelectedIndex());
	}




	
	

	
}
