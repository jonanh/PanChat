package panchat.linker;

import java.util.*;
import java.io.*;

import panchat.Panchat;
import panchat.connector.Connector;
import panchat.messages.CausalMessage;
import panchat.users.Usuario;

public class Linker {

	private Connector connector;

	private Object mutex = new Object();

	private Hashtable<UUID, LinkedList<CausalMessage>> ObjectTable = new Hashtable<UUID, LinkedList<CausalMessage>>();

	public Linker(Panchat panchat) {
		connector = panchat.getConnector();
	}

	/**
	 * 
	 * @param destId
	 * @param msg
	 */
	public void sendMsg(Usuario destId, Object msg) {
		try {
			ObjectOutputStream oos = connector.getOOS(destId.uuid);
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			System.out.println("ConexionEnvio.java: Error al enviar");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param destIds
	 * @param msg
	 */
	public void multicast(LinkedList<Usuario> destIds, Object msg) {
		for (Usuario usuario : destIds)
			sendMsg(usuario, msg);
	}

	/**
	 * Recibe un mensaje de un cliente
	 * 
	 * @param fromId
	 *            La dirección del cliente del cúal recibir
	 * 
	 * @return El mensaje recibido
	 * 
	 * @throws IOException
	 */
	public Object receiveMsg(Usuario fromId) throws IOException {

		/*
		 * Esperamos mientras no hayamos recibido ningún elemento
		 */
		try {
			while (ObjectTable.get(fromId.uuid) == null)
				ObjectTable.wait();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/*
		 * Hacemos riguroso el acceso a la lista
		 */
		synchronized (mutex) {
			return ObjectTable.get(fromId.uuid).poll();
		}
	}

	/**
	 * Cierra los sockets.
	 */
	public void close() {
		connector.closeSockets();
	}

	/**
	 * Añadimos nuevo usuario en el causal linker
	 * 
	 * @param usuario
	 */
	public void anyadirUsuario(Usuario usuario) {
		ObjectTable.put(usuario.uuid, new LinkedList<CausalMessage>());
	}

	/**
	 * Añadimos un nuevo mensaje a la cola de mensajes
	 */
	public synchronized void anyadirMensaje(UUID uuid, CausalMessage msg) {
		ObjectTable.get(uuid).add(msg);
		ObjectTable.notify();
	}
}
