package panchat.linker;

import java.util.*;
import java.net.*;
import java.io.*;

import panchat.Panchat;
import panchat.addressing.Usuario;
import panchat.listeners.MulticastListenerThread;
import panchat.share.protocolo.RegistrarCliente;

/**
 * Esta gestiona los sockets con el conjunto de clientes
 * 
 * @author Jon Ander Hernández & Javier Mediavilla
 * 
 */
public class Connector {

	public static boolean DEBUG = false;

	/*
	 * Constantes y variables para el socket multicast
	 */
	public final static String MDNS_GROUP = "224.0.0.251";
	public final static int MDNS_PORT = 5454;

	/**
	 * This is the multicast group, we are listening to for multicast DNS
	 * messages.
	 */
	private MulticastSocket socket;

	private InetAddress group;

	private ServerSocket listener;

	private Hashtable<UUID, Socket> link;
	private Hashtable<UUID, ObjectInputStream> hashOIS;
	private Hashtable<UUID, ObjectOutputStream> hashOOS;

	Usuario usuario;

	/**
	 * 
	 * @param usuario
	 * @throws Exception
	 */
	public Connector(Panchat panchat) {

		usuario = panchat.getUsuario();

		// Inicializamos link
		link = new Hashtable<UUID, Socket>();
		hashOIS = new Hashtable<UUID, ObjectInputStream>();
		hashOOS = new Hashtable<UUID, ObjectOutputStream>();

		// Inicializamos los sockets.
		inicializarSockets();

		/*
		 * Nos damos a conocer al mundo, como buenos ciudadanos 0:-)
		 */
		enviarSaludo();

		/*
		 * Creamos un MulticastListenerThread para escuchar los eventos
		 * Multicast
		 */
		if (DEBUG)
			System.out
					.println("Connector.java: Creando el MulticastListenerThread 0:-)");

		MulticastListenerThread thread = new MulticastListenerThread(socket,
				panchat, this);
		thread.start();
	}

	private void inicializarSockets() {
		/*
		 * Creamos un server Socket
		 */
		try {
			if (DEBUG)
				System.out.println("Connector.java: Creando ServerSocket");

			listener = new ServerSocket(usuario.port);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		/*
		 * Creamos el Socket multicast
		 */
		try {
			group = InetAddress.getByName(MDNS_GROUP);

			socket = new MulticastSocket(MDNS_PORT);

			socket.joinGroup(group);

		} catch (UnknownHostException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Enviamos un saludo Multicast
	 */
	public void enviarSaludo() {
		RegistrarCliente msgRegistrar = new RegistrarCliente(usuario, true);
		byte[] buffer = msgRegistrar.bytes();

		DatagramPacket hi = new DatagramPacket(buffer, buffer.length, group,
				MDNS_PORT);
		try {

			if (DEBUG)
				System.out
						.println("Connector.java: Dandonos a conocer al mundo 0:-)");

			socket.send(hi);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cerramos los sockets del cliente.
	 */
	public void closeSockets() {
		try {
			// Cerramos el socket multicast
			socket.close();

			// Cerramos el ServerSocket
			listener.close();

			// Cerramos los Socket con el resto de clientes.
			link.entrySet().iterator().next().getValue().close();

		} catch (Exception e) {
			System.err.println("Miellldaaa algo hemos cerrado mal 0:-) " + e);
		}
	}

	/**
	 * Mandamos registro
	 * 
	 * @param usuario
	 */
	public synchronized void connect(Usuario usuario) {
		try {
			// Creamos el socket
			Socket socket = new Socket(usuario.ip, usuario.port);

			// Creamos los object streams
			ObjectOutputStream oos = new ObjectOutputStream(socket
					.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket
					.getInputStream());

			oos.writeObject(usuario);

			link.put(usuario.uuid, socket);
			hashOIS.put(usuario.uuid, ois);
			hashOOS.put(usuario.uuid, oos);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void acceptConnect() {
		try {
			// Creamos el socket
			Socket socket = listener.accept();

			// Creamos los object streams
			ObjectInputStream ois = new ObjectInputStream(socket
					.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket
					.getOutputStream());

			Usuario usuario = null;

			try {
				usuario = (Usuario) ois.readObject();
			} catch (ClassNotFoundException e) {
				System.out.println("Connector.java: Objeto no recibido");
				e.printStackTrace();
			}

			link.put(usuario.uuid, socket);
			hashOIS.put(usuario.uuid, ois);
			hashOOS.put(usuario.uuid, oos);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Obtener el Socket
	 * 
	 * @param uuid
	 * @return
	 */
	public Socket getSocket(UUID uuid) {
		return link.get(uuid);
	}

	/**
	 * Obtener el ObjectInputStream
	 * 
	 * @param uuid
	 * @return
	 */
	public ObjectInputStream getOIS(UUID uuid) {
		return hashOIS.get(uuid);
	}

	/**
	 * Obtener el ObjectOutputStream
	 * 
	 * @param uuid
	 * @return
	 */
	public ObjectOutputStream getOOS(UUID uuid) {
		return hashOOS.get(uuid);
	}

}
