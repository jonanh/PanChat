package simulation.arrows;

import java.awt.geom.Point2D;
import java.io.Serializable;

import simulation.view.CellPosition;
import simulation.view.SimulationView;

@SuppressWarnings("serial")
public class SingleArrow extends Arrow implements MessageArrow, Serializable {

	CellPosition initialPos;
	CellPosition finalPos;

	public SingleArrow(CellPosition initialPos, CellPosition finalPos) {
		super(0f, 0f, 0f, 0f);
		update(initialPos, finalPos);
	}

	private void update(CellPosition initialPos, CellPosition finalPos) {
		this.initialPos = initialPos;
		this.finalPos = finalPos;

		Point2D.Float pos1 = SimulationView.PositionCoords(initialPos);
		Point2D.Float pos2 = SimulationView.PositionCoords(finalPos);

		super.setLine(pos1, pos2);
	}

	/**
	 * @return the finalPos
	 */
	public CellPosition getFinalPos() {
		return finalPos;
	}

	/**
	 * @param finalPos
	 *            the finalPos to set
	 */
	public void setFinalPos(CellPosition finalPos) {
		update(initialPos, finalPos);
	}

	/**
	 * @return the initialPos
	 */
	public CellPosition getInitialPos() {
		return initialPos;
	}

	/**
	 * @param initialPos
	 *            the finalPos to set
	 */
	public void setInitialPos(CellPosition initialPos) {
		update(initialPos, finalPos);
	}

	@Override
	public String toString() {
		return "Flecha[ " + initialPos + finalPos + " ]";
	}
}
