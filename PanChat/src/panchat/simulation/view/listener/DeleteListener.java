package panchat.simulation.view.listener;

import java.awt.event.MouseEvent;

import panchat.simulation.arrows.MultipleArrow;
import panchat.simulation.view.CellPosition;
import panchat.simulation.view.Position;
import panchat.simulation.view.SimulationView;

public class DeleteListener extends ViewListener {

	public DeleteListener(SimulationView simulationView) {
		super(simulationView);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Obtenemos la posición
		Position pos = simulationView.getPosition(e);

		// Si la posicion es una celda
		if (pos instanceof CellPosition) {

			CellPosition cell = (CellPosition) pos;
			MultipleArrow arrow = simulationModel.deleteArrow(cell);

			if (arrow != null)
				if (!arrow.deleteArrow(cell))
					simulationModel.addArrow(arrow);
		}
	}
}
