package simulation3.arrows;

import java.util.List;

import simulation3.arrows.SingleArrow;
import simulation3.model.SimulationArrowModel;
import simulation3.view.CellPosition;

public class SimpleMultipleArrow extends MultipleArrow {

	private static final long serialVersionUID = 1L;

	public SimpleMultipleArrow(CellPosition initialPos) {
		super(initialPos);
	}

	public SimpleMultipleArrow(CellPosition initialPos, SingleArrow arrow) {
		super(initialPos, arrow);
	}

	/**
	 * Verificamos si se encuentra en un lugar válido y/o libre :
	 * 
	 * <ul>
	 * <li>Una flecha no puede tener flechas que vayan hacia atrás.</li>
	 * <li>Una flecha no puede apuntar a una celda ya ocupada.</li>
	 * </ul>
	 * 
	 * @param messageArrow
	 * 
	 * @return Si es valida la flecha
	 */
	public boolean isValid(SimulationArrowModel simulationModel) {

		// Si sólo tenemos una flecha, entonces comprobar si esa flecha es
		// valida
		if (arrowList.size() == 1)
			return arrowList.get(0).isValid(simulationModel);

		return super.isValid(simulationModel);
	}

	@Override
	public boolean add2Simulation(SimulationArrowModel simulationModel) {

		List<SingleArrow> arrows = this.getFinalArrow(moveCell);

		if (arrows.size() > 0) {
			SingleArrow arrow = arrows.get(0);

			// Buscamos si existe una flecha que va al mismo proceso
			CellPosition found = null;
			for (CellPosition pos : getPositions()) {
				if (arrow.getFinalPos().process == pos.process
						&& !arrow.getFinalPos().equals(pos)) {
					found = pos;
					break;
				}
			}
			// Si no existía una flecha que vaya a ese proceso, la añadimos
			if (found != null)
				super.deleteArrow(found);

		}

		super.add2Simulation(simulationModel);

		return true;
	}
}
