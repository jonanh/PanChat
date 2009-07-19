package panchat.linker;

import java.util.*;
import java.io.*;

import panchat.Panchat;
import panchat.connector.Connector;
import panchat.data.Usuario;
import panchat.messages.SimpleMessage;

public class Linker {

	private static final boolean DEBUG = false;

	private Connector connector;

	private Panchat panchat;

	private Object mutex = new Object();

	/*
	 * Cola de mensajes de entrega.
	 */
	LinkedList<SimpleMessage> deliveryQ = new LinkedList<SimpleMessage>();

	public Linker(Panchat pPanchat) {
		this.panchat = pPanchat;
		this.connector = pPanchat.getConnector();
	}

	/**
	 * Función a nivel de paquete para mandar objetos
	 * 
	 * @param destId
	 * @param msg
	 */
	void socketSendMsg(Usuario destId, Object msg) {
		try {
			ObjectOutputStream oos = connector.getOOS(destId.uuid);
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envio de mensaje
	 * 
	 * @param destId
	 * @param msg
	 */
	public synchronized void sendMsg(Usuario destId, Object msg) {
		socketSendMsg(destId, new SimpleMessage(msg, panchat.getUsuario()));
	}

	/**
	 * Envio de mensaje multicast
	 * 
	 * @param destIds
	 * @param msg
	 */
	public synchronized void sendMsg(LinkedList<Usuario> destIds, Object msg) {
		for (Usuario usuario : destIds)
			sendMsg(usuario, msg);
	}

	/**
	 * Recibe un mensaje
	 * 
	 * @throws IOException
	 */
	public Object handleMsg() throws IOException {

		synchronized (mutex) {

			printDebug("invocado handleMsg");

			/*
			 * Esperamos mientras no hayamos recibido ningún elemento
			 */
			try {
				while (deliveryQ.isEmpty())
					deliveryQ.wait();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			printDebug("Un objeto encontrado");

			/*
			 * Hacemos riguroso el acceso a la lista
			 */
			return deliveryQ.poll();
		}
	}

	/**
	 * Añadimos un nuevo mensaje a la cola de mensajes
	 */
	public synchronized void anyadirMensaje(SimpleMessage msg) {
		synchronized (mutex) {
			printDebug("Mensaje añadido a la cola del Linker");

			deliveryQ.add(msg);
			mutex.notifyAll();
		}
	}

	private void printDebug(String string) {
		String msgClase = "ListenerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
