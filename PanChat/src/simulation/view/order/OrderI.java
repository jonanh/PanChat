package simulation.view.order;

import java.awt.Graphics2D;

import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;

public interface OrderI {
	
	public boolean addLogicalOrder(SingleArrow arrow, boolean isMultiple);

	public void removeLogicalOrder(CellPosition finalPos);

	public boolean moveLogicalOrder(SingleArrow arrow);

	public void removeOnlyLogicalOrder(CellPosition finalPos);

	public void recalculateVectors(int originalTick);

	void draw(Graphics2D g2);
}
