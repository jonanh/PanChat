package simulation.arrows;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.EnumMap;

import order.Message.Type;

import simulation.model.SimulationArrowModel;
import simulation.view.CellPosition;

public interface MessageArrow {

	/**
	 * @return the initialPos
	 */
	public CellPosition getInitialPos();

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
	 * Dibuja la flecha sobre el contexto gráfico
	 * 
	 * @param g
	 * @param color
	 */
	public void draw(Graphics2D g, Color color);

	/**
	 * @return Devuelve un clon de la flecha
	 */
	public MessageArrow clone();
}
