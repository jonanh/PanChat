package simulation.view.listener;

import java.awt.event.MouseEvent;

import simulation.arrows.MessageArrow;
import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;
import simulation.view.Position;
import simulation.view.SimulationView;

public class CreateListener extends ViewListener {

	private SingleArrow moveArrow;
	private SingleArrow drawingArrow;

	public CreateListener(SimulationView simulationView) {
		super(simulationView);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Obtenemos la posición y la flecha en esa posición
		Position pos = simulationView.getPosition(e);

		// Si la posicion es una celda
		if (pos instanceof CellPosition) {

			CellPosition cell = (CellPosition) pos;
			MessageArrow arrow = simulationModel.getArrow(pos);

			// Si no existe ninguna flecha, o si hemos pinchado en el comienzo
			// de la flecha, creamos una nueva flecha :
			if (arrow == null || cell.equals(arrow.getInitialPos())) {

				drawingArrow = new SingleArrow(cell, cell);
				simulationView.setDrawingArrow(drawingArrow);

			}
			// Si hemos pinchado en una flecha del extremo final, nos permite
			// mover el final de la flecha.
			else {
				moveArrow = (SingleArrow) simulationModel.deleteArrow(cell);
				drawingArrow = new SingleArrow(moveArrow.getInitialPos(), cell);
				simulationView.setDrawingArrow(drawingArrow);
			}
		}

		// Actualizamos la posicion de la SimulationView, de manera que
		// dibuje la iluminación cuando pasa el cursor por encima
		simulationView.setPosition(simulationView.getPosition(e), drawingArrow
				.isValid(simulationModel));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		SingleArrow arrow = (SingleArrow) simulationView.getDrawingArrow();

		// Eliminamos la flecha en dibujo
		simulationView.setDrawingArrow(null);

		// si la nueva posicion es valida la añadimos a la lista de flechas.
		if (arrow.isValid(simulationModel)) {

			simulationModel.addArrow(arrow);

		} // Si no es valida, y estabamos moviendo una flecha que ya existía,
		// volvemos a incluir la copia antigua que teniamos de la flecha
		// antes de moverla.
		else if (moveArrow != null) {

			simulationModel.addArrow(moveArrow);

			// Quitamos la flecha que estabamos moviendo
			moveArrow = null;
		}

		// Actualizamos la posicion de la SimulationView, de manera que
		// dibuje la iluminación cuando pasa el cursor por encima
		simulationView.setPosition(simulationView.getPosition(e), true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		// Obtenemos la posición
		Position pos = simulationView.getPosition(e);

		// Si la posicion es una celda
		if (pos instanceof CellPosition) {

			CellPosition cell = (CellPosition) pos;
			drawingArrow.setFinalPos(cell);
		}

		// Actualizamos la posicion de la SimulationView, de manera que
		// dibuje la iluminación cuando pasa el cursor por encima
		simulationView.setPosition(simulationView.getPosition(e), drawingArrow
				.isValid(simulationModel));
	}
}
