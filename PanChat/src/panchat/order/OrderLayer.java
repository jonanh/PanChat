package panchat.order;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import panchat.data.User;
import panchat.messages.Message;

public abstract class OrderLayer extends Observable implements Observer {

	private List<OrderLayer> bottomLayer;

	protected User user;

	/*
	 * Cola de mensajes pendientes y mensajes de entrega.
	 */
	private LinkedList<Message> deliveryQueue = new LinkedList<Message>();
	private LinkedList<Message> pendingQueue = new LinkedList<Message>();

	/**
	 * Crear capa de ordenacion
	 * 
	 * @param user
	 * @param upLayer
	 */
	public OrderLayer(User user) {
		this.user = user;
	}

	/**
	 * Devuelve la capability de ordenación que implementa la capa actual.
	 * 
	 * @return
	 */
	public abstract Message.Type orderCapability();

	/**
	 * 
	 */
	protected abstract boolean okayToRecv(Message msg);

	/**
	 * Añadir
	 */
	public void addBottomLayers(List<OrderLayer> bottomLayers) {
		this.bottomLayer = bottomLayers;
		for (OrderLayer layer : bottomLayer)
			this.addObserver(layer);
	}

	/**
	 * Enviar mensaje a un usuario
	 * 
	 * @param user
	 * @param msg
	 */
	public synchronized void sendMsg(User user, Message msg) {
		for (OrderLayer layer : bottomLayer)
			if (msg.isType(layer.orderCapability())) {
				layer.sendMsg(user, msg);
				break;
			}
	}

	/**
	 * Enviar mensaje a multiples usuarios
	 * 
	 * @param users
	 * @param msg
	 */
	public synchronized void sendMsg(LinkedList<User> users, Message msg) {
		for (OrderLayer layer : bottomLayer)
			if (msg.isType(layer.orderCapability())) {
				layer.sendMsg(users, msg);
				break;
			}
	}

	/**
	 * Cuando se actualiza una capa de abajo, cogemos los mensajes que soporta
	 * nuestra capa.
	 */
	@Override
	public void update(Observable o, Object arg) {

		OrderLayer orderLayer = (OrderLayer) o;

		// Recorremos los mensajes de la cola de mensaje recibidos
		Iterator<Message> iter = orderLayer.deliveryQueue.iterator();

		while (iter.hasNext()) {
			Message mensaje = iter.next();

			// Si el mensaje es del tipo de la capacidad de la capa actual, lo
			// eliminamos de la lista y lo añadimos a la lista de mensajes
			// pendientes.
			if (mensaje.isType(orderCapability())) {
				iter.remove();
				pendingQueue.add(mensaje);
			}
		}

		boolean delivery = false;

		// Comprobamos ahora nuestra cola de mensajes pendientes
		iter = orderLayer.pendingQueue.iterator();
		while (iter.hasNext()) {
			Message mensaje = iter.next();

			if (okayToRecv(mensaje)) {

				// Hay algo que mandar
				delivery = true;

				// Añadimos nuestro mensaje a la cola de mensajes listos para
				// enviar a la capa superior
				deliveryQueue.add(mensaje);

				// Borramos el mensaje de la lista de mensajes pendientes
				iter.remove();

				// Volvemos a pasar la lista de pendientes desde el principio
				iter = pendingQueue.iterator();
			}
		}

		// Si hay algo que enviar, notificamos a las capas superiores
		if (delivery) {
			this.setChanged();
			this.notifyObservers();
		}
	}

	/*
	 * Añadir y quitar usuarios
	 */

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