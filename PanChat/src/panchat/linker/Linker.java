package panchat.linker;

import java.util.*;
import java.io.*;

import panchat.Panchat;
import panchat.addressing.Usuario;

public class Linker {
	private Connector connector;

	public Linker(Panchat panchat) throws Exception {
		connector = new Connector(panchat);
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

		try {
			return connector.getOIS(fromId.uuid).readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Cierra los sockets.
	 */
	public void close() {
		connector.closeSockets();
	}
}
