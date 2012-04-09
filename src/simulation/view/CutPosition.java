package simulation.view;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CutPosition implements Serializable, IPosition {

	public int tick;

	public CutPosition(int tick) {
		this.tick = tick;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CutPosition) {
			CutPosition other = (CutPosition) obj;
			return (tick == other.tick);
		}
		return false;
	}
}
