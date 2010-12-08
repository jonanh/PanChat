package simulation.view.order;


import java.util.Vector;

import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;

public interface OrderI {
	public boolean addLogicalOrder(SingleArrow arrow,boolean isMultiple);
	public void removeLogicalOrder(CellPosition finalPos);
	public boolean moveLogicalOrder(SingleArrow arrow);
	public void removeOnlyLogicalOrder(CellPosition finalPos);
	public Vector<VectorClock> getVectorClocks();
}
