package simulation.view.order;


import java.util.Vector;

import simulation.arrows.SingleArrow;

public interface OrderI {
	public boolean addLogicalOrder(SingleArrow arrow);
	public boolean removeLogicalOrder(SingleArrow arrow);
	public boolean moveLogicalOrder(SingleArrow arrow);
	public Vector<VectorClock> getVectorClocks();
}
