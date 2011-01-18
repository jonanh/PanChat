package simulation.order_dinamic;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import order.Message;

import panchat.data.User;
import simulation.arrows.MultipleArrow;
import simulation.arrows.SingleArrow;
import simulation.model.SimulationArrowModel;
import simulation.order_dinamic.arrows.DeliveryArrow;
import simulation.view.CellPosition;
import simulation.view.ISimulator;
import simulation.view.SimulationView;

/**
 * SimulationOrderLayer almacena los mensajes llegados desde capas superiores en
 * una tabla hash, además del estado de los relojes en el instante de la
 * recepcion.
 * 
 */
public class SimulationOrderModel extends Observable implements ISimulator {

	/*
	 * Atributos
	 */

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
	 * Guardamos los clocks para cada posición.
	 */
	private HashMap<CellPosition, SavedClocks> receiveClocks = new HashMap<CellPosition, SavedClocks>();

	/*
	 * Guardamos los logs para cada posición.
	 */
	private HashMap<CellPosition, String> receiveLogs = new HashMap<CellPosition, String>();

	/*
	 * Lista de flechas de entrega
	 */
	private List<SingleArrow> simulationArrows = new LinkedList<SingleArrow>();

	/*
	 * Referencia al modelo de flechas.
	 */
	private SimulationArrowModel simulationArrowModel;

	public SimulationOrderModel(SimulationArrowModel simulationModel) {

		simulationArrowModel = simulationModel;

	}

	/**
	 * Cargamos un nuevo SimulationModel
	 * 
	 * @param simulationModel
	 */
	public void setSimulationModel(SimulationArrowModel simulationModel) {

		// Observamos la nueva simulacion;
		this.simulationArrowModel = simulationModel;
	}

	/**
	 * @return Obtenemos el número de procesos
	 */
	public int getNumProcesses() {
		return this.simulationArrowModel.getNumProcesses();
	}

	/**
	 * @param process
	 * 
	 * @return Devuelve el usuario correspondiente a un proceso determinado.
	 */
	public User getUser(int process) {
		return this.simulationArrowModel.getUser(process);
	}

	/**
	 * @param position
	 * 
	 * @return Devuelve la información del reloj correspondiente a la posición
	 *         position.
	 */
	public SavedClocks getClocks(CellPosition position) {
		if (position != null)
			return receiveClocks.get(position);
		else
			return null;
	}

	/**
	 * 
	 * @param position
	 * 
	 * @return Devuelve la información de las capas de ordenación.
	 */
	public String getLog(CellPosition position) {
		if (position != null)
			return receiveLogs.get(position);
		else
			return null;
	}

	/**
	 * Realizar nueva simulacion
	 */
	@Override
	public void simulate(SimulationView simulationView) {

		simulationArrowModel = simulationView.getSimulationModel();

		// Reiniciamos la simulacion.
		topLayers.clear();
		simulationArrows.clear();
		receive.clear();
		receiveClocks.clear();
		receiveLogs.clear();

		// Creamos los top layers
		for (User user : simulationArrowModel.getUserList()) {
			topLayers.put(user, new SimulationTopLayer(user));
		}

		// Registramos los usuarios en los top layers
		for (User user : simulationArrowModel.getUserList()) {
			for (User user2 : simulationArrowModel.getUserList()) {
				if (!user.equals(user2))
					topLayers.get(user).addUser(user2);
			}
		}

		CellPosition iterator = new CellPosition(0, 0);
		// Vamos recorriendo las casillas
		for (iterator.tick = 0; iterator.tick < simulationArrowModel
				.getTimeTicks(); iterator.tick++) {

			for (iterator.process = 0; iterator.process < simulationArrowModel
					.getNumProcesses(); iterator.process++) {

				// Los topLayers está diseñados para usarse con usuarios, por lo
				// tanto para la simulación realizamos un mapeo entre el
				// identificador proceso, y un usuario creado para
				// representarlo.
				User user = simulationArrowModel.getUser(iterator.process);
				SimulationTopLayer topLayer = topLayers.get(user);

				// Las MultipleArrow son el conjunto de flechas que conforman
				// una operación.
				MultipleArrow multipleArrow;

				// Listado de flechas que salen del punto que estamos evaluando.
				List<SingleArrow> sendArrows;

				// Obtenemos la flecha correspondiente a este paso en el
				// simulador, comprobando si esta existe.
				multipleArrow = simulationArrowModel.getArrow(iterator);
				if (multipleArrow != null) {

					// Obtenemos las flechas que salen de este punto
					sendArrows = multipleArrow.getInitialArrow(iterator);

					for (SingleArrow destArrow : sendArrows) {

						// Necesitamos hacer el mapeo proceso - usuario
						int process = destArrow.getFinalPos().process;
						User user2 = simulationArrowModel.getUser(process);

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
						SingleArrow arrow = new DeliveryArrow(pos, iterator
								.clone());

						// La añadimos al listado de flechas de entrega.
						simulationArrows.add(arrow);
					}
				}

				/*
				 * Registramos los relojes de cada paso.
				 */
				CellPosition pos = iterator.clone();
				receiveClocks.put(pos, topLayer.getClocks());
				receiveLogs.put(pos, topLayer.getDebugLog());
			}
		}
	}

	@Override
	public void drawSimulation(Graphics2D g) {
		SimulationView.paintArrows(g, this.simulationArrows);
	}
}
