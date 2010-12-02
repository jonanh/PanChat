package simulation.arrows;

import simulation.view.CellPosition;

public interface MessageArrow{

	/**
	 * @return the initialPos
	 */
	public CellPosition getInitialPos();

	/**
	 * @param initialPos
	 *            the finalPos to set
	 */
	public void setInitialPos(CellPosition initialPos);
}
