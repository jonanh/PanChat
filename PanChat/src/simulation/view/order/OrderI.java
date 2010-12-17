package simulation.view.order;

import java.awt.Graphics2D;

import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;

public interface OrderI {
	
	public boolean addLogicalOrder(SingleArrow arrow);

	public void removeFinalOrder(CellPosition finalPos);
	
	public void removeInitialOrder(CellPosition initPos);
	
	public void setNumProcessChanged();

	public void recalculateVectors(int originalTick);

	void draw(Graphics2D g2);
}
