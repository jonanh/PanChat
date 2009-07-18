package panchat.linker;

import java.util.*;
import java.io.*;

import panchat.Panchat;
import panchat.connector.Connector;
import panchat.data.Usuario;
import panchat.messages.SimpleMessage;

public class Linker {

	private Connector connector;

	Panchat panchat;
	Usuario myId;

	private Object mutex = new Object();

	/*
	 * Cola de mensajes de entrega.
	 */
	LinkedList<SimpleMessage> deliveryQ = new LinkedList<SimpleMessage>();

	public Linker(Panchat pPanchat) {
		this.panchat = pPanchat;
		this.myId = pPanchat.getUsuario();
		this.connector = panchat.getConnector();
	}

	/**
	 * Envio de mensaje
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
	 * Envio de mensaje multicast
	 * 
	 * @param destIds
	 * @param msg
	 */
	public void sendMsg(LinkedList<Usuario> destIds, Object msg) {
		for (Usuario usuario : destIds)
			sendMsg(usuario, msg);
	}

	/**
	 * Recibe un mensaje
	 * 
	 * @throws IOException
	 */
	public Object handleMsg() throws IOException {

		/*
		 * Esperamos mientras no hayamos recibido ningún elemento
		 */
		try {
			while (deliveryQ.isEmpty())
				deliveryQ.wait();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/*
		 * Hacemos riguroso el acceso a la lista
		 */
		synchronized (mutex) {
			return deliveryQ.poll();
		}
	}

	/**
	 * Añadimos un nuevo mensaje a la cola de mensajes
	 */
	public synchronized void anyadirMensaje(SimpleMessage msg) {
		deliveryQ.add(msg);
		deliveryQ.notify();
	}
}
