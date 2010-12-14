package simulation.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

import panchat.data.User;
import panchat.messages.Message;
import panchat.messages.Message.Type;
import panchat.order.CausalVectorOrderLayer;
import panchat.order.FifoOrderLayer;
import panchat.order.OrderLayer;
import simulation.view.CellPosition;
import sun.tools.tree.ThisExpression;

public class SimulationTopLayer extends OrderLayer {

	SimulationBottomLayer bottomLayer;

	public SimulationTopLayer(User user, Collection<User> users) {
		this(user);
		// añadimos los usuarios
		for (User u : users)
			if (!u.equals(user))
				this.addUser(u);
	}

	public SimulationTopLayer(User user) {
		super(user);

		// Creamos las capas de orden
		bottomLayer = new SimulationBottomLayer(user);
		FifoOrderLayer fifo = new FifoOrderLayer(user);
		CausalVectorOrderLayer causal = new CausalVectorOrderLayer(user);

		// Vinculamos las capas en el orden inverso, ya que notifyObservers
		// llama a los observadores en orden inverso al orden en el que son
		// registrados.
		this.addBottomLayers(fifo, causal, bottomLayer);
		causal.addBottomLayers(fifo, bottomLayer);
		fifo.addBottomLayers(bottomLayer);
	}

	@Override
	protected boolean okayToRecv(Message msg) {
		return true;
	}

	@Override
	public Type orderCapability() {
		return null;
	}

	/**
	 * Devuelve el objeto enviado
	 * 
	 * @return Message
	 */
	public Message getSendedMsg() {
		return bottomLayer.getSendedMsg();
	}

	/**
	 * Simulamos la recepción de un mensaje
	 * 
	 * @param msg
	 */
	public void receive(Message msg) {
		bottomLayer.receive(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see panchat.order.OrderLayer#update(java.util.Observable,
	 * java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
		if (this.deliveryQueue.size() > 0) {
			debug("mensaje recibido!!!");
			debug(this.deliveryQueue.toString());
		}
	}

	public static void main(String[] args) {
		// Creamos usuarios
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("A"));
		users.add(new User("B"));
		users.add(new User("C"));

		SimulationTopLayer topLayer1 = new SimulationTopLayer(users.get(0),
				users);
		SimulationTopLayer topLayer2 = new SimulationTopLayer(users.get(1),
				users);

		// Enviamos una posicion
		CellPosition cell1 = new CellPosition(0, 2);
		CellPosition cell2 = new CellPosition(0, 3);

		Message msg1 = new Message(cell1, users.get(0), Message.Type.FIFO);
		Message msg2 = new Message(cell2, users.get(0), Message.Type.FIFO);

		topLayer1.sendMsg(users.get(1), msg1);
		msg1 = topLayer1.getSendedMsg();

		topLayer1.sendMsg(users.get(1), msg2);
		msg2 = topLayer1.getSendedMsg();

		topLayer2.receive(msg2);
		topLayer2.receive(msg1);
	}
}
