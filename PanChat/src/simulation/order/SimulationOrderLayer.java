package simulation.order;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import panchat.data.User;
import panchat.messages.Message;
import simulation.arrows.MessageArrow;
import simulation.arrows.MultipleArrow;
import simulation.arrows.SingleArrow;
import simulation.model.SimulationModel;
import simulation.view.CellPosition;

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
	private HashMap<User, SimulationBottomLayer> bottomLayers = new HashMap<User, SimulationBottomLayer>();

	private SimulationModel simulationModel;

	private HashMap<CellPosition, Message> receive = new HashMap<CellPosition, Message>();

	public SimulationOrderLayer(SimulationModel simulationModel) {
		this.simulationModel = simulationModel;
	}

	/**
	 * Realizar nueva simulacion
	 */
	public void simulate() {
		CellPosition iterator = new CellPosition(0, 0);
		// Vamos recorriendo las casillas
		for (; iterator.tick < simulationModel.getTimeTicks(); iterator.tick++)
			for (; iterator.process < simulationModel.getNumProcesses(); iterator.process++) {
				MessageArrow arrow = simulationModel.getArrow(iterator);

				// Si obtenemos una flecha multiple, debemos enviar el mensaje a
				// varios destinatarios
				if (arrow instanceof MultipleArrow) {

				} else if (arrow instanceof SingleArrow) {

				}
			}
	}

	@Override
	public void update(Observable o, Object arg) {
		// rehacer simulacion
		simulate();
	}
}
