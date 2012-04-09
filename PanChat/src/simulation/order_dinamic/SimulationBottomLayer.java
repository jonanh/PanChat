package simulation.order_dinamic;

import java.util.HashMap;
import java.util.List;

import order.Message;
import order.Message.Type;
import order.layer.OrderLayer;

import panchat.data.User;

/**
 * Esta clase simula la capa de red.
 * 
 */
public class SimulationBottomLayer extends OrderLayer {

	// Almacenamos el mensaje enviado. Nota: Tan sólo se puede mandar un sólo
	// mensaje en cada unidad de tiempo, aunque un mensaje si puede tener
	// multiples destinatarios.
	private HashMap<User, Message> message = new HashMap<User, Message>();

	public SimulationBottomLayer(User user) {
		super(user);
	}

	@Override
	protected receiveStatus okayToRecv(Message msg) {
		return receiveStatus.Receive;
	}

	@Override
	public synchronized void sendMsg(User user, Message msg, boolean answer) {
		this.message.put(user, msg);
	}

	@Override
	public synchronized void sendMsg(List<User> users, Message msg,
			boolean answer) {
		for (User user : users) {
			this.message.put(user, msg);
		}
	}

	@Override
	public Type orderCapability() {
		return null;
	}

	/*
	 * Funciones propias de la capa de abajo
	 */

	/**
	 * Devuelve el objeto enviado
	 * 
	 * @return Message
	 */
	public HashMap<User, Message> getSendedMsg() {
		HashMap<User, Message> returnMessage = message;
		message = new HashMap<User, Message>();
		return returnMessage;
	}

	/**
	 * Simulamos la recepción de un mensaje
	 * 
	 * @param msg
	 */
	public void receive(Message msg) {
		this.deliveryQueue.add(msg.clone());
		this.setChanged();
		this.notifyObservers(this);
	}

	@Override
	public String layerName() {
		return "PhysicalLayer";
	}
}
