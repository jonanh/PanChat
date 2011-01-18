package simulation.order_dinamic.arrows;

import java.awt.Color;

import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;

public class DeliveryArrow extends SingleArrow {

	private static final Color ARROW_COLOR = new Color(1f, 1f, 0f, .5f);

	public DeliveryArrow(CellPosition initialPos, CellPosition finalPos) {
		super(initialPos, finalPos, ARROW_COLOR);
	}

	public DeliveryArrow(CellPosition initialPos, CellPosition finalPos,
			Color color) {
		super(initialPos, finalPos, ARROW_COLOR);
	}

	private static final long serialVersionUID = 1L;

}
