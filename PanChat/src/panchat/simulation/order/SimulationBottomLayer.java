package panchat.simulation.order;

import java.util.HashMap;
import java.util.List;

import panchat.data.User;
import panchat.messages.Message;
import panchat.messages.Message.Type;
import panchat.order.OrderLayer;

public class SimulationBottomLayer extends OrderLayer {

	private HashMap<User, Message> message = new HashMap<User, Message>();

	public SimulationBottomLayer(User user) {
		super(user);
	}

	@Override
	protected boolean okayToRecv(Message msg) {
		return true;
	}

	@Override
	public synchronized void sendMsg(User user, Message msg) {
		debug("\nMensaje enviado fisicamente a :" + user + " " + msg);
		this.message.put(user, msg);
	}

	@Override
	public synchronized void sendMsg(List<User> users, Message msg) {
		for (User user : users) {
			debug("\nMensaje enviado fisicamente a :" + user + " " + msg);
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
}
