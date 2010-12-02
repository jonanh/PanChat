package simulation.view;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CellPosition implements Serializable, Position {

	public int process;
	public int tick;

	public CellPosition(int numProcess, int tick) {
		this.process = numProcess;
		this.tick = tick;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + process;
		result = prime * result + tick;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CellPosition) {
			CellPosition other = (CellPosition) obj;
			return (tick == other.tick && process == other.process);
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + process + "," + tick + ")";
	}
}
