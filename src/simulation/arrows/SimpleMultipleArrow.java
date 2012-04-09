package simulation.arrows;

import java.util.List;

import simulation.arrows.SingleArrow;
import simulation.model.SimulationArrowModel;
import simulation.view.CellPosition;

public class SimpleMultipleArrow extends MultipleArrow {

	private static final long serialVersionUID = 1L;

	public SimpleMultipleArrow(CellPosition initialPos) {
		super(initialPos);
	}

	public SimpleMultipleArrow(CellPosition initialPos, SingleArrow arrow) {
		super(initialPos, arrow);
	}

	/**
	 * Comprobamos que sólo una flecha va a un proceso.
	 */
	@Override
	public boolean add2Simulation(SimulationArrowModel simulationModel) {

		List<SingleArrow> arrows = this.getFinalArrow(moveCell);

		if (arrows.size() > 0) {
			SingleArrow arrow = arrows.get(0);

			// Buscamos si existe una flecha que va al mismo proceso
			CellPosition found = null;
			for (SingleArrow arrow2 : this.getArrowList()) {
				boolean differentArrows = !arrow.equals(arrow2);
				boolean sameProcess = arrow2.getFinalPos().process == arrow
						.getFinalPos().process;
				if (differentArrows && sameProcess) {
					found = arrow2.getFinalPos();
					break;
				}
			}
			// Si existía una flecha que vaya a ese proceso, la borramos
			if (found != null)
				super.deleteArrow(found);

		}

		super.add2Simulation(simulationModel);

		return true;
	}

	/**
	 * Rutina para clonar MultipleArrows
	 */
	@Override
	public MultipleArrow clone() {
		MultipleArrow newArrow = new SimpleMultipleArrow(initialPos);

		newArrow.properties = this.properties.clone();

		for (SingleArrow arrow : arrowList)
			newArrow.addArrow(arrow.clone());

		return newArrow;
	}
}
