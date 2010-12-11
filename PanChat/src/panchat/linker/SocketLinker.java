package panchat.linker;

import java.util.*;
import java.io.*;

import panchat.Panchat;
import panchat.connector.SocketConnector;
import panchat.data.User;
import panchat.messages.Message;
import panchat.order.OrderLayer;

public class SocketLinker extends OrderLayer {

	private static final boolean DEBUG = false;

	private SocketConnector connector;

	private Object mutex = new Object();

	/*
	 * Cola de mensajes de entrega.
	 */
	LinkedList<Message> deliveryQ = new LinkedList<Message>();


	/**
	 * Función a nivel de paquete para mandar objetos
	 * 
	 * @param destId
	 * @param msg
	 */
	private void socketSendMsg(User destId, Object msg) {
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
	public synchronized void sendMsg(User destId, Object msg) {
		socketSendMsg(destId, new Message(msg, panchat.getUsuario()));
	}

	/**
	 * Envio de mensaje multicast
	 * 
	 * @param destIds
	 * @param msg
	 */
	public synchronized void sendMsg(LinkedList<User> destIds, Object msg) {
		for (User usuario : destIds)
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
	public synchronized void anyadirMensaje(Message msg) {
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
