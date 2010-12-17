package panchat.clocks;

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
