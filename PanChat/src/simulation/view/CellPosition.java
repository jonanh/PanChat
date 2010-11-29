package simulation.view;

public class CellPosition implements Position {

	public int process;
	public int tick;

	public CellPosition(int numProcess, int tick) {
		this.process = numProcess;
		this.tick = tick;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CellPosition) {
			CellPosition other = (CellPosition) obj;
			return (tick == other.tick && process == other.process);
		}
		return false;
	}
}
