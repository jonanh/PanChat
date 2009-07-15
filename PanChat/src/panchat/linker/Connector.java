package panchat.linker;

import java.util.*;
import java.util.Map.Entry;
import java.net.*;
import java.io.*;

import panchat.Panchat;
import panchat.addressing.Usuario;
import panchat.listeners.ListenerThread;
import panchat.listeners.MulticastListenerThread;
import panchat.share.protocolo.RegistrarCliente;

/**
 * Esta gestiona los sockets con el conjunto de clientes
 * 
 * @author Jon Ander Hernández & Javier Mediavilla
 * 
 */
public class Connector {

	public final static boolean DEBUG = false;

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

	// Pool de threads
	private Hashtable<UUID, Thread> threadPool;

	// Multicast thread
	private MulticastListenerThread multicastThread;

	private Usuario usuario;
	private Panchat panchat;

	/**
	 * 
	 * @param usuario
	 * @throws Exception
	 */
	public Connector(Panchat panchat) {

		this.panchat = panchat;
		this.usuario = panchat.getUsuario();

		// Inicializamos link
		link = new Hashtable<UUID, Socket>();
		hashOIS = new Hashtable<UUID, ObjectInputStream>();
		hashOOS = new Hashtable<UUID, ObjectOutputStream>();

		// Inicializamos la threadPool
		threadPool = new Hashtable<UUID, Thread>();

		// Inicializamos los sockets.
		inicializarSockets();

		/*
		 * Nos damos a conocer al mundo, como buenos ciudadanos 0:-)
		 */
		enviarSaludo(true);

		/*
		 * Creamos un MulticastListenerThread para escuchar los eventos
		 * Multicast
		 */
		printDebug("Creando el MulticastListenerThread 0:-)");

		this.multicastThread = new MulticastListenerThread(socket, panchat,
				this);
	}

	private void inicializarSockets() {
		/*
		 * Creamos un server Socket
		 */
		try {
			printDebug("Creando ServerSocket");

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
	 * 
	 * Enviamos un saludo Multicast
	 * 
	 * @param Registrar
	 */
	public void enviarSaludo(boolean Registrar) {
		RegistrarCliente msgRegistrar = new RegistrarCliente(usuario, Registrar);
		byte[] buffer = msgRegistrar.bytes();

		DatagramPacket hi = new DatagramPacket(buffer, buffer.length, group,
				MDNS_PORT);
		try {

			if (Registrar)
				printDebug("Dandonos a conocer al mundo 0:-)");
			else {
				printDebug("Despidiendonos del mundo 0:-)");
				printDebug("num procesos activos : "
						+ String.valueOf(threadPool.size()));
			}
			socket.send(hi);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Registramos el usuario, creamos un socket con el
	 * 
	 * Este es el caso cuando UUID del usuario es menor que nuestro UUID, si no
	 * se llama a acceptConnect()
	 * 
	 * @param usuario
	 */
	public synchronized void connect(Usuario pUsuario) {
		Socket socket = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;

		try {
			// Creamos el socket
			socket = new Socket(pUsuario.ip, pUsuario.port);

			// Creamos los object streams
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			// Enviamos petición
			oos.writeObject(usuario);

			link.put(pUsuario.uuid, socket);
			hashOIS.put(pUsuario.uuid, ois);
			hashOOS.put(pUsuario.uuid, oos);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creamos el ListenerThread para escuchar al socket
		Thread thread = new ListenerThread(panchat, pUsuario, socket, ois);
		thread.start();

		// Añadimos el thread al thread Pool
		threadPool.put(pUsuario.uuid, thread);
	}

	/**
	 * Creamos un socket server y esperamos a que nos manden un usuario
	 * 
	 * Este es el caso cuando UUID del usuario es menor que nuestro UUID, si no
	 * se llama a acceptConnect()
	 */
	public synchronized void acceptConnect() {
		Socket socket = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		Usuario readUsuario = null;

		try {
			// Creamos el socket
			socket = listener.accept();

			// Creamos los object streams
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());

			// Leemos el objeto Usuario que nos envia el cliente
			try {
				readUsuario = (Usuario) ois.readObject();
			} catch (ClassNotFoundException e) {
				System.out.println("Connector.java: Objeto no recibido");
				e.printStackTrace();
			}

			link.put(readUsuario.uuid, socket);
			hashOIS.put(readUsuario.uuid, ois);
			hashOOS.put(readUsuario.uuid, oos);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creamos el ListenerThread para escuchar al socket
		Thread thread = new ListenerThread(panchat, readUsuario, socket, ois);
		thread.start();

		// Añadimos el thread al thread Pool
		threadPool.put(readUsuario.uuid, thread);
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

	/**
	 * Arrancar el hilo MulticastListenerThread
	 */
	public void arrancarMulticastListenerThread() {
		this.multicastThread.start();
	}

	/**
	 * Cerramos los sockets del cliente.
	 */
	public void closeSockets() {
		printDebug("Cerrando Sockets");

		// Cerramos los Socket con el resto de clientes.
		Iterator<Entry<UUID, Socket>> iter = link.entrySet().iterator();

		while (iter.hasNext()) {
			// Por cada socket guardado intentamos cerrarlo (por si no está
			// cerrado ya)
			try {
				Entry<UUID, Socket> entry = iter.next();
				printDebug("Cerrando socket de " + usuario + " a "
						+ entry.getKey());
				entry.getValue().close();
			} catch (Exception e) {
				printDebug("Fallo el cierre :-P");
			}
		}

		printDebug("Cerrados todos los sockets abiertos");

		Iterator<Entry<UUID, Thread>> iter2 = threadPool.entrySet().iterator();
		while (iter2.hasNext())
			// Esperamos a que termine el multicastThread
			try {
				Entry<UUID, Thread> entry = iter2.next();
				printDebug("Esperando a parar ListenerThread de " + usuario
						+ " a " + entry.getKey());
				entry.getValue().join();
			} catch (InterruptedException e) {
				printDebug("Fallo al esperar :-P");
			}

		printDebug("Parados todos los hilos de la thread pool");

		try {

			// Cerramos el ServerSocket
			listener.close();

		} catch (Exception e) {
		}

		printDebug("Cerrado ServerSocket");

		try {

			// Cerramos el socket multicast
			socket.close();

		} catch (Exception e) {
		}

		printDebug("Cerrado el socket multicastThread");

		// Esperamos a que termine el multicastThread
		try {
			multicastThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		printDebug("Parado el multicastThread");

	}

	private void printDebug(String string) {
		if (DEBUG)
			System.out.println(msgClase + string);
	}

	final static String msgClase = "Connector.java: ";
}
