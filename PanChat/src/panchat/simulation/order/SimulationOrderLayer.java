package panchat.simulation.order;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import panchat.clocks.SavedClocks;
import panchat.data.User;
import panchat.messages.Message;
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

	private static final Color ARROW_COLOR = new Color(1f, 1f, 0f, .5f);

	/*
	 * Atributos
	 */
	private SimulationView simulationView;
	private SimulationModel simulationModel;

	private List<SingleArrow> simulationArrows;

	/*
	 * Conjunto de las capas de ordenación. Para cada usuario disponemos de una
	 * capa de ordenación. Esta capa está diseñada para la simulación y por
	 * tanto permite simular el funcionamiento de la red, obteniendo el mensaje
	 * que deberíamos haber enviado por la red, e inyectando más tarde el
	 * mensaje como si lo hubieramos recibido.
	 */
	private HashMap<User, SimulationTopLayer> topLayers = new HashMap<User, SimulationTopLayer>();

	/*
	 * Los mensajes recibidos para cada posición. Las flechas semánticamente
	 * representan entregar un mensaje
	 */
	private HashMap<CellPosition, Message> receive = new HashMap<CellPosition, Message>();

	/*
	 * Guardamos los clocks en el View, para que cuando cambiemos de celda,
	 * podamos visualizarlas.
	 */
	private HashMap<CellPosition, SavedClocks> receiveClocks;

	public SimulationOrderLayer(SimulationView simulationView) {

		this.simulationView = simulationView;
		this.simulationModel = simulationView.getSimulationModel();
		this.simulationArrows = simulationView.getSimulationArrows();
		this.receiveClocks = simulationView.getReceiveClocks();

		simulationModel.addObserver(this);

	}

	/**
	 * Realizar nueva simulacion
	 */
	public synchronized void simulate() {

		// Reiniciamos la simulacion.
		topLayers.clear();
		simulationArrows.clear();
		receive.clear();
		receiveClocks.clear();

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
		for (iterator.tick = 0; iterator.tick < simulationModel.getTimeTicks(); iterator.tick++) {

			System.out.println();
			System.out.println("tick : " + iterator.tick);
			System.out.println("----------");
			System.out.println();

			for (iterator.process = 0; iterator.process < simulationModel
					.getNumProcesses(); iterator.process++) {

				// Los topLayers está diseñados para usarse con usuarios, por lo
				// tanto para la simulación realizamos un mapeo entre el
				// identificador proceso, y un usuario creado para
				// representarlo.
				User user = simulationModel.getUser(iterator.process);
				SimulationTopLayer topLayer = topLayers.get(user);

				// Las MultipleArrow son el conjunto de flechas que conforman
				// una operación.
				MultipleArrow multipleArrow;

				// Listado de flechas que salen del punto que estamos evaluando.
				List<SingleArrow> sendArrows;

				// Obtenemos la flecha correspondiente a este paso en el
				// simulador, comprobando si esta existe.
				multipleArrow = simulationModel.getArrow(iterator);
				if (multipleArrow != null) {

					// Obtenemos las flechas que salen de este punto
					sendArrows = multipleArrow.getInitialArrow(iterator);

					for (SingleArrow destArrow : sendArrows) {

						// Necesitamos hacer el mapeo proceso - usuario
						int process = destArrow.getFinalPos().process;
						User user2 = simulationModel.getUser(process);

						// Creamos el mensaje, y guardamos en el la posición
						// donde estamos (el tick y el proceso), de modo que
						// cuando lo recibamos creamos una flecha desde que se
						// envio, a ese momento en el que se reciba.
						Message msg = new Message(iterator.clone(), user,
								multipleArrow.getProperties().clone());

						topLayer.sendMsg(user2, msg);

						// Al ser simulado, no se envia realmente el paquete, y
						// lo que hacemos es recogerlo de la capa más baja de
						// las capas de ordenación. Tras su paso por las capas
						// de ordenación ellas habrán introducido en el mensaje
						// los relojes necesarios.
						Collection<Message> messages;
						messages = topLayer.getSendedMsg().values();

						// Guardamos el mensaje (simulando el retardo de la
						// red), y lo añadiremos al destinatario en el momento
						// que representa el final de la flecha.
						receive.put(destArrow.getFinalPos(), messages
								.iterator().next());

					}
				}

				/*
				 * Recepción de mensajes
				 */

				// Obtenemos el mensaje que previamente habíamos guardado en el
				// paso anterior, y que debemos entregar según la posición.
				Message msg = receive.get(iterator);

				// Si existe un nensaje por recibir
				if (msg != null) {

					// Hacemos que al capa simule la recepción del mensaje a
					// nivel de red.
					topLayer.receive(msg);

					// Ahora obtenemos el listado de mensajes que recibe el
					// usuario en ese instante. Como pueden existir mensajes
					// encolados, en un mismo tick podríamos recibir varios
					// mensajes.
					Iterator<Message> receivedMessages;
					receivedMessages = topLayer.getReceivedMsgs().iterator();
					while (receivedMessages.hasNext()) {

						Message rMsg = receivedMessages.next();

						// En el interior del mensaje habíamos guardado la
						// posición de destino
						CellPosition pos = (CellPosition) rMsg.getContent();

						// Creamos la flecha que representa la entrega
						SingleArrow arrow = new SingleArrow(pos, iterator
								.clone(), ARROW_COLOR);

						// La añadimos al listado de flechas de entrega.
						simulationArrows.add(arrow);
					}
				}

				/*
				 * Registramos los relojes de cada paso.
				 */
				receiveClocks.put(iterator.clone(), topLayer.getClocks());
			}
		}
		simulationView.repaint();

		System.out.println("\n\n");
	}

	/**
	 * Cuando se produce un cambio, debemos rehacer la simulación.
	 */
	@Override
	public void update(Observable o, Object arg) {
		simulate();
	}

	/**
	 * Cargamos un nuevo SimulationModel
	 * 
	 * @param simulationModel
	 */
	public void setSimulationModel(SimulationModel simulationModel) {

		// Dejamos de observar la antigua simulacion
		this.simulationModel.deleteObserver(this);

		// Observamos la nueva simulacion;
		this.simulationModel = simulationModel;
		this.simulationModel.addObserver(this);

		// Simulamos
		simulate();
	}
}
