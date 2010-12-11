package simulation.arrows;

import java.awt.Graphics2D;

import simulation.model.SimulationModel;
import simulation.view.CellPosition;

public interface MessageArrow {

	/**
	 * @return the initialPos
	 */
	public CellPosition getInitialPos();

	/**
	 * @param initialPos
	 *            the finalPos to set
	 */
	public void setInitialPos(CellPosition initialPos);

	public MessageArrow clone();

	public void draw(Graphics2D g);

	public boolean isValid(SimulationModel simulationModel);

}
