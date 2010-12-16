package panchat.simulation.order;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import panchat.data.User;
import panchat.messages.Message;
import panchat.messages.Message.Type;
import panchat.simulation.arrows.MultipleArrow;
import panchat.simulation.arrows.SingleArrow;
import panchat.simulation.model.SimulationModel;
import panchat.simulation.view.CellPosition;
import panchat.simulation.view.SimulationView;

/**
 * SimulationOrderLayer almacena los mensajes llegados desde capas superiores en
 * una tabla hash, además del estado de los relojes en el instante de la
 * recepcion.
 * 
 */
public class SimulationOrderLayer implements Observer {

	/*
	 * Guardamos unas tablas con las capas de ordenación
	 */
	private HashMap<User, SimulationTopLayer> topLayers = new HashMap<User, SimulationTopLayer>();

	private SimulationView simulationView;
	private SimulationModel simulationModel;

	private HashMap<CellPosition, Message> receive = new HashMap<CellPosition, Message>();

	private List<SingleArrow> simulationArrows;

	public SimulationOrderLayer(SimulationView simulationView) {
		this.simulationView = simulationView;
		this.simulationModel = simulationView.getSimulationModel();
		this.simulationArrows = simulationView.getSimulationArrows();
		simulationModel.addObserver(this);
	}

	/**
	 * Realizar nueva simulacion
	 */
	public synchronized void simulate() {

		// Limpiamos los resultados anteriores
		topLayers.clear();
		simulationArrows.clear();
		receive.clear();
		
		// Creamos los top layers
		for (User user : simulationModel.getUserList()) {
			topLayers.put(user, new SimulationTopLayer(user));
		}

		// Registramos los usuarios en los top layers
		for (User user : simulationModel.getUserList()) {
			for (User user2 : simulationModel.getUserList()) {
				if (!user.equals(user2))
					topLayers.get(user).addUser(user2);
			}
		}

		CellPosition iterator = new CellPosition(0, 0);
		// Vamos recorriendo las casillas
		for (iterator.tick = 0; iterator.tick < simulationModel.getTimeTicks(); iterator.tick++)
			for (iterator.process = 0; iterator.process < simulationModel
					.getNumProcesses(); iterator.process++) {

				// Averiguamos quien es el usuario correspondiente al
				// proceso
				User user = simulationModel.getUser(iterator.process);

				SimulationTopLayer topLayer = topLayers.get(user);

				/*
				 * Envio de mensajes
				 */

				// Obtenemos el MultipleArrow
				MultipleArrow multipleArrow = simulationModel
						.getArrow(iterator);

				// Si existe una flecha en esta posición
				if (multipleArrow != null) {

					// Obtenemos la lista de flechas simples
					List<SingleArrow> arrow = multipleArrow
							.getInitialArrow(iterator);

					// Obtenemos los destinatarios usando los destinos de las
					// flechas.
					for (SingleArrow destArrow : arrow) {

						int process = destArrow.getFinalPos().process;
						User user2 = simulationModel.getUser(process);

						// Creamos el mensaje
						Message msg = new Message(iterator.clone(), user,
								Type.CAUSAL);

						// Lo enviamos al destinatario
						topLayer.sendMsg(user2, msg);

						// Recogemos el mensaje enviado
						Collection<Message> messages;
						messages = topLayer.getSendedMsg().values();

						if (messages.size() != 1) {
							System.out
									.println("error al intentar enviar los mensajes");
							System.out.println(messages);
						} else {
							receive.put(destArrow.getFinalPos(), messages
									.iterator().next());
						}
					}
				}
				/*
				 * Recepción de mensajes
				 */
				// Obtenemos el mensaje
				Message msg = receive.get(iterator);

				// Si existe un nensaje por recibir
				if (msg != null) {
					// Hacemos que al capa simule la recepción del mensaje
					topLayer.receive(msg);

					// Obtenemos los mensajes recibidos
					Iterator<Message> receivedMessages;
					receivedMessages = topLayer.getReceivedMsgs().iterator();
					while (receivedMessages.hasNext()) {

						// Obtenemos de cada mensaje recibido las coordenadas de
						// cuando fue enviado, y las usamos para dibujar una
						// flecha de recepcion.
						Message rMsg = receivedMessages.next();
						CellPosition pos = (CellPosition) rMsg.getContent();
						SingleArrow arrow = new SingleArrow(pos, iterator
								.clone(), new Color(1f, 1f, 0f, .5f));
						simulationArrows.add(arrow);
					}
				}
			}
		simulationView.repaint();
		
		System.out.println("\n\n");
	}

	@Override
	public void update(Observable o, Object arg) {
		simulate();
	}

	public void setSimulationModel(SimulationModel simulationModel) {
		// Dejamos de observar la antigua simulacion
		this.simulationModel.deleteObserver(this);

		// Observamos la nueva simulacion;
		this.simulationModel = simulationModel;
		this.simulationModel.addObserver(this);

		// Simulamos
		// simulate();
	}
}
