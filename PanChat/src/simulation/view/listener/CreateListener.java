package simulation.view.listener;

import java.awt.event.MouseEvent;

import simulation.arrows.MessageArrow;
import simulation.model.SimulationModel;
import simulation.view.CellPosition;
import simulation.view.Position;
import simulation.view.SimulationView;

public class CreateListener extends ViewListener {

	SimulationModel simulationModel;
	MessageArrow moveArrow;
	MessageArrow drawingArrow;

	public CreateListener(SimulationView simulationView) {
		super(simulationView);
		this.simulationModel = simulationView.getSimulationModel();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Obtenemos la posición y la flecha en esa posición
		Position pos = simulationView.getPosition(e);

		// Si la posicion es una celda
		if (pos instanceof CellPosition) {

			CellPosition cell = (CellPosition) pos;
			drawingArrow = simulationModel.getArrow(pos);

			// Si no existe ninguna flecha, o si hemos pinchado en el comienzo
			// de la flecha, creamos una nueva flecha :
			if (drawingArrow == null
					|| cell.equals(drawingArrow.getInitialPos())) {

				drawingArrow = new MessageArrow(cell, cell);
				simulationView.setDrawingArrow(drawingArrow);

			} else {
				moveArrow = simulationModel.deleteArrow(cell);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		MessageArrow arrow = simulationView.getDrawingArrow();

		// si la nueva posicion es valida se incluye
		if (simulationModel.isValidArrow(arrow)) {

			simulationModel.addArrow(simulationView.getDrawingArrow());

		} // Si no es valida, volvemos a la anterior fecha
		else if (moveArrow != null) {

			simulationModel.addArrow(moveArrow);
		}

		// Eliminamos la flecha en dibujo
		simulationView.setDrawingArrow(null);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Obtenemos la posición y la flecha en esa posición
		Position pos = simulationView.getPosition(e);

		// Si la posicion es una celda
		if (pos instanceof CellPosition) {

			CellPosition cell = (CellPosition) pos;
			drawingArrow.setFinalPos(cell);

			super.mouseDragged(e);
		}
	}
}
