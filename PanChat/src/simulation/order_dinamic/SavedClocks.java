package simulation.order_dinamic;

import order.clocks.CausalMatrix;
import order.clocks.VectorClock;

public class SavedClocks {

	public VectorClock sendClock;
	public VectorClock receiveClock;
	public CausalMatrix causal;

	public SavedClocks(VectorClock sendClock, VectorClock receiveClock,
			CausalMatrix causal) {

		this.sendClock = sendClock;
		this.receiveClock = receiveClock;
		this.causal = causal;

	}
}
