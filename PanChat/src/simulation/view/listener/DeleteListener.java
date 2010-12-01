package simulation.view.listener;

import java.awt.event.MouseEvent;

import simulation.view.CellPosition;
import simulation.view.Position;
import simulation.view.SimulationView;

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
			simulationModel.deleteArrow(cell);
		}
	}
}
