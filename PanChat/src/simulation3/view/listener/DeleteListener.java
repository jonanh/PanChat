package simulation3.view.listener;

import java.awt.event.MouseEvent;

import simulation3.arrows.MultipleArrow;
import simulation3.view.CellPosition;
import simulation3.view.Position;
import simulation3.view.SimulationView;

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
