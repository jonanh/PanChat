package panchat.simulation.arrows;

import java.awt.Graphics2D;
import java.util.EnumMap;

import panchat.messages.Message.Type;
import panchat.simulation.model.SimulationArrowModel;
import panchat.simulation.view.CellPosition;

public interface MessageArrow {

	/**
	 * @return the initialPos
	 */
	public CellPosition getInitialPos();

	/**
	 * Método que accionamos cuando hemos movido la flecha.
	 * 
	 * La flecha usará este método para recalcularse cuando sea una flecha
	 * compleja.
	 */
	// public CellPosition move(CellPosition newPosition);

	/**
	 * Método que accionamos cuando hemos movido la flecha.
	 * 
	 * La flecha usará este método para recalcularse cuando sea una flecha
	 * compleja.
	 */
	// public void move(CellPosition oldPosition, CellPosition newPosition);

	/**
	 * Método que accionamos cuando hemos movido la flecha.
	 * 
	 * @param simulationModel
	 */
	// public boolean add2Simulation(SimulationModel simulationModel);

	/**
	 * @return Devuelve las propiedades de la flecha.
	 */
	public EnumMap<Type, Boolean> getProperties();

	/**
	 * @param simulationModel
	 * 
	 * @return Devuelve si la flecha es válida, ya que las flechas pueden
	 *         imponer restricciones de movilidad o de posición.
	 */
	public boolean isValid(SimulationArrowModel simulationModel);

	/**
	 * Dibuja la flecha sobre el contexto gráfico
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g);

	/**
	 * @return Devuelve un clon de la flecha
	 */
	public MessageArrow clone();
}
