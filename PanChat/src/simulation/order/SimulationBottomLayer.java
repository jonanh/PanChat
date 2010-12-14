package simulation.order;

import java.util.LinkedList;

import panchat.data.User;
import panchat.messages.Message;
import panchat.messages.Message.Type;
import panchat.order.OrderLayer;

public class SimulationBottomLayer extends OrderLayer {

	private Message message;

	public SimulationBottomLayer(User user) {
		super(user);
	}

	@Override
	protected boolean okayToRecv(Message msg) {
		return true;
	}

	@Override
	public synchronized void sendMsg(User user, Message msg) {
		debug("sendMsg:" + user + " ");
		this.message = msg;
	}

	@Override
	public synchronized void sendMsg(LinkedList<User> users, Message msg) {
		debug("sendMsg:" + users + msg);
		this.message = msg;
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
	public Message getSendedMsg() {
		return this.message;
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
