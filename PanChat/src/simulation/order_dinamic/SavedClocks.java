package simulation.order_dinamic;

import order.clocks.CausalMatrix;
import order.clocks.VectorClock;

/**
 * 
 * Esta es una simple estructura para almacenar cada tipo de reloj generado
 * durante la simulación.
 * 
 */
public class SavedClocks {

	public VectorClock sendClock;
	public VectorClock receiveClock;
	public CausalMatrix causal;
	public String total;

	public SavedClocks(VectorClock sendClock, VectorClock receiveClock,
			CausalMatrix causal, String total) {

		this.sendClock = sendClock;
		this.receiveClock = receiveClock;
		this.causal = causal;
		this.total = total;
	}
}
