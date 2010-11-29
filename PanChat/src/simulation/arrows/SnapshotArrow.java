package simulation.arrows;

import java.io.Serializable;

import simulation.view.CellPosition;

@SuppressWarnings("serial")
public class SnapshotArrow extends MessageArrow implements Serializable {

	public SnapshotArrow(CellPosition initalPos, CellPosition finalPos) {
		super(initalPos, finalPos);
	}

}
