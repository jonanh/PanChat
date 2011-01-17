package order.layer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import panchat.data.User;
import panchat.messages.Message;

public abstract class OrderLayer extends Observable implements Observer {

	protected final static boolean DEBUG = true;

	public static enum receiveStatus {
		Receive, Delete, Nothing
	}

	/*
	 * Lista de capas inferiores.
	 */
	private List<OrderLayer> bottomLayer = new LinkedList<OrderLayer>();

	/*
	 * Usuario de la capa actual
	 */
	protected User user;

	/*
	 * Cola de mensajes pendientes y mensajes de entrega.
	 */
	protected Queue<Message> deliveryQueue = new LinkedList<Message>();
	protected Queue<Message> pendingQueue = new LinkedList<Message>();

	/*
	 * Lista de usuarios
	 */
	protected LinkedList<User> userList = new LinkedList<User>();

	/*
	 * Log de la capa actual.
	 */
	private String debugLog = "";

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
	 * 
	 * @return Devuelve la capability de ordenación que implementa la capa
	 *         actual.
	 */
	public abstract Message.Type orderCapability();

	/**
	 * Enviar mensaje a un usuario
	 * 
	 * @param user
	 * 
	 * @param msg
	 */
	public synchronized void sendMsg(User user, Message msg) {
		for (OrderLayer layer : bottomLayer)
			if (msg.isType(layer.orderCapability())) {
				debug("Enviando : " + layer.getClass().getName());
				layer.sendMsg(user, msg);
				break;
			}
	}

	/**
	 * Enviar mensaje a multiples usuarios
	 * 
	 * @param users
	 * 
	 * @param msg
	 */
	public synchronized void sendMsg(List<User> users, Message msg) {
		for (OrderLayer layer : bottomLayer)
			if (msg.isType(layer.orderCapability())) {
				debug("sendMsg to : " + layer.getClass().getName());
				layer.sendMsg(users, msg);
				break;
			}
	}

	/**
	 * Añadir capas inferiores.
	 * 
	 * Cuando enviemos un mensaje, la capa actual buscará la primera capa
	 * inferior que pueda enviarlo.
	 * 
	 * Cuando un mensaje llegue a una capa inferior, la capa actual estará
	 * escuchando, y si dicho mensaje es del tipo de la capa actual lo recogerá
	 * de dicha capa.
	 * 
	 * @param bottomLayers
	 */
	public void addBottomLayers(OrderLayer... bottomLayers) {
		for (OrderLayer layer : bottomLayers) {
			this.bottomLayer.add(layer);
			layer.addObserver(this);
		}
	}

	/**
	 * @param iter
	 * @return Devuelve si el mensaje se puede recibir o no. Este método ha de
	 *         ser implementado por cada capa de ordenación.
	 */
	protected abstract receiveStatus okayToRecv(Message msg);

	/**
	 * Cuando se actualiza una capa de inferior:
	 * 
	 * 1º Comprobamos si los mensajes son del tipo de nuestra capa. si lo son
	 * los pasamos a nuestra capa y los añadimos en orden a la cola de espera.
	 * 
	 * 2º Comprobamos la cola de espera, y si alguno de los mensajes valida el
	 * predicado OkayToRecv, pasamos el mensaje a la cola de envio.
	 * 
	 * 3º Si la cola de envio no está vacía, avisamos a las capas superiores de
	 * que tenemos mensajes recibidos.
	 */
	@Override
	public void update(Observable o, Object arg) {

		debug("Comprobando : " + this.getClass().getName() + "\n");
		debug("\tComprobando pending");

		OrderLayer orderLayer = (OrderLayer) o;

		// Recorremos los mensajes de la cola de mensaje recibidos
		Iterator<Message> iter = orderLayer.deliveryQueue.iterator();

		while (iter.hasNext()) {
			Message mensaje = iter.next();

			debug("\t\t- comprobando mensaje " + mensaje.toString());

			// Si el mensaje es del tipo de la capacidad de la capa actual, lo
			// eliminamos de la lista y lo añadimos a la lista de mensajes
			// pendientes.
			if (mensaje.isType(orderCapability())) {
				iter.remove();

				this.pendingQueue.offer(mensaje);

				debug("\t\t- añadido a pending! ");

				debug("\t\t- Stado del pendingQueue : " + pendingQueue);
			}
		}

		debug("\n\tComprobando delivery");

		boolean delivery = false;

		// Comprobamos ahora nuestra cola de mensajes pendientes
		iter = this.pendingQueue.iterator();

		while (iter.hasNext()) {
			Message mensaje = iter.next();

			debug("\t\t- comprobando mensaje " + mensaje.toString());

			if (okayToRecv(mensaje) == receiveStatus.Receive) {

				// Hay algo que mandar
				delivery = true;

				// Añadimos nuestro mensaje a la cola de mensajes listos para
				// enviar a la capa superior
				deliveryQueue.offer(mensaje);

				// Borramos el mensaje de la lista de mensajes pendientes
				iter.remove();

				// Volvemos a pasar la lista de pendientes desde el principio
				iter = pendingQueue.iterator();

				debug("\t\t- añadido a delivery! ");

			} else if (okayToRecv(mensaje) == receiveStatus.Delete) {
				iter.remove();
			}
		}
		debug("");

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
	public void addUser(User user) {
		// añadimos el usuario a la lista de usuarios
		this.userList.add(user);

		// añadimos los usuarios en las capas de abajo
		for (OrderLayer layer : bottomLayer)
			layer.addUser(user);
	}

	/**
	 * Eliminar usuario
	 * 
	 * @param user
	 */
	public void removeUser(User user) {
		// borramos el usuario a la lista de usuarios
		this.userList.remove(user);

		// borramos el usuarios de las capas de abajo
		for (OrderLayer layer : bottomLayer)
			layer.addUser(user);
	}

	protected void debug(String msg) {
		if (DEBUG)
			debugLog += msg + "\n";
	}

	/**
	 * @return Devuelve la información de depuración de la capa actual
	 */
	public String getDebugLog() {
		return debugLog;
	}

}