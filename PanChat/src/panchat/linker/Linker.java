package panchat.linker;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import panchat.data.User;
import panchat.messages.Message;

public abstract class Linker extends Observable implements Observer {

	/*
	 * 
	 */
	protected LinkedList<Linker> linker = new LinkedList<Linker>();

	protected User myId;

	/*
	 * Cola de mensajes pendientes y mensajes de entrega.
	 */
	protected LinkedList<? extends Message> deliveryQ;
	protected LinkedList<? extends Message> pendingQ;

	/**
	 * Crear capa de ordenacion
	 * 
	 * @param user
	 * @param upLinkers
	 */
	public Linker(User user, Linker... upLinkers) {
		for (Linker elem : upLinkers) {
			linker.add(elem);
		}
		this.myId = user;
	}

	/**
	 * Enviar mensaje a un usuario
	 * 
	 * @param user
	 * @param msg
	 */
	public abstract void sendMsg(User user, Object msg);

	/**
	 * Enviar mensaje a multiples usuarios
	 * 
	 * @param users
	 * @param msg
	 */
	public abstract void sendMsg(LinkedList<User> users, Object msg);

	/**
	 * Recibir mensajes
	 */
	public LinkedList<? extends Message> receive() {
		return deliveryQ;
	}

	/**
	 * Añadir usuario
	 * 
	 * @param user
	 */
	public abstract void addUser(User user);

	/**
	 * Eliminar usuario
	 * 
	 * @param user
	 */
	public abstract void removeUser(User user);

}