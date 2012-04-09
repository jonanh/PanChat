package simulation.order_dinamic;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

import order.Message;
import order.Message.Type;
import order.clocks.CausalMatrix;
import order.clocks.VectorClock;
import order.layer.CausalMatrixLayer;
import order.layer.FifoOrderLayer;
import order.layer.OrderLayer;
import order.layer.TotalOrderLayer;

import panchat.data.User;

/**
 * Esta clase simula la recepción de los mensajes por el programa.
 */
public class SimulationTopLayer extends OrderLayer {

	/*
	 * Capas de ordenación
	 */
	SimulationBottomLayer bottomLayer;
	FifoOrderLayer fifo;
	CausalMatrixLayer causal;
	TotalOrderLayer total;

	Queue<Message> receivedQueue = new LinkedList<Message>();

	public SimulationTopLayer(User user, Collection<User> users) {
		this(user);

		// Debemos añadir los usuarios que pertenecerán al grupo.
		for (User u : users)
			if (!u.equals(user))
				this.addUser(u);
	}

	public SimulationTopLayer(User user) {
		super(user);

		/*
		 * Creamos las capas de orden
		 */
		bottomLayer = new SimulationBottomLayer(user);
		fifo = new FifoOrderLayer(user);
		causal = new CausalMatrixLayer(user);
		total = new TotalOrderLayer(user, true);

		/*
		 * Vinculamos las capas en el orden inverso, ya que notifyObservers
		 * llama a los observadores en orden inverso al orden en el que son
		 * registrados.
		 */
		this.addBottomLayers(total, causal, fifo, bottomLayer);
		total.addBottomLayers(causal, fifo, bottomLayer);
		causal.addBottomLayers(fifo, bottomLayer);
		fifo.addBottomLayers(bottomLayer);
	}

	@Override
	protected receiveStatus okayToRecv(Message msg) {
		return OrderLayer.receiveStatus.Receive;
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
	public HashMap<User, Message> getSendedMsg() {
		return bottomLayer.getSendedMsg();
	}

	/**
	 * Simulamos la recepción de un mensaje. Es decir estamos simulando el
	 * momento en que el cliente recibiría a través del socket el mensaje.
	 * 
	 * @param msg
	 */
	public void receive(Message msg) {
		bottomLayer.receive(msg);
	}

	/**
	 * @return Obtenemos los mensajes que el cliente habría recibido en ese tick
	 *         tras todo el procesamiento de las capas de ordenación.
	 */
	public Collection<Message> getReceivedMsgs() {
		Queue<Message> returnQueue = receivedQueue;
		receivedQueue = new LinkedList<Message>();
		return returnQueue;
	}

	/**
	 * 
	 * @return Devuelve los relojes de las diferentes capas.
	 */
	public SavedClocks getClocks() {
		VectorClock sendClock = fifo.getSendClock().clone();
		VectorClock receiveClock = fifo.getReceiveClock().clone();
		CausalMatrix causalMatrix = causal.getCausalMatrix().clone();
		String simulationDelivery = new String(total.getSimulationDelivery());

		return new SavedClocks(sendClock, receiveClock, causalMatrix,
				simulationDelivery);
	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
		if (this.deliveryQueue.size() > 0) {
			debug("\nMensajes recibidos en el cliente :");
			debug("\t" + this.deliveryQueue.toString() + "\n\n");

			this.receivedQueue.addAll(deliveryQueue);
			this.deliveryQueue.clear();
		}
	}

	@Override
	public String layerName() {
		return "TopLayer";
	}
}
