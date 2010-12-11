package simulation.view.listener;

import java.awt.event.MouseEvent;

import simulation.arrows.MessageArrow;
import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;
import simulation.view.CutPosition;
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

			// Actualizamos la posicion de la SimulationView, de manera que
			// dibuje la iluminación cuando pasa el cursor por encima
			simulationView.setPosition(simulationView.getPosition(e),
					drawingArrow.isValid(simulationModel));

		} else
			// Actualizamos la posicion de la SimulationView, de manera que
			// dibuje la iluminación cuando pasa el cursor por encima
			simulationView.setPosition(simulationView.getPosition(e), true);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Si estamos dibujando
		if (drawingArrow != null) {

			SingleArrow arrow = (SingleArrow) simulationView.getDrawingArrow();

			// si la nueva posicion es valida la añadimos a la lista de flechas.
			if (arrow.isValid(simulationModel)) {

				simulationModel.addArrow(arrow);

			} // Si no es valida, y estabamos moviendo una flecha que ya
			// existía,
			// volvemos a incluir la copia antigua que teniamos de la flecha
			// antes de moverla.
			else if (moveArrow != null) {

				simulationModel.addArrow(moveArrow);

			}

			// Quitamos la flecha que estabamos moviendo
			simulationView.setDrawingArrow(null);
			moveArrow = null;
			drawingArrow = null;
		}

		// Actualizamos la posicion de la SimulationView, de manera que
		// dibuje la iluminación cuando pasa el cursor por encima
		simulationView.setPosition(simulationView.getPosition(e), true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		// Si estamos dibujando
		if (drawingArrow != null) {

			// Obtenemos la posición
			Position pos = simulationView.getPosition(e);

			// Si la posicion es una celda actualizamos la posicion
			if (pos instanceof CellPosition) {

				CellPosition cell = (CellPosition) pos;
				drawingArrow.setFinalPos(cell);

			}
			// Si es una columna, dejamos apuntando al mismo proceso, pero
			// cambiamos a que tick apunta
			else if (pos instanceof CutPosition) {

				CutPosition cell = (CutPosition) pos;
				// Obtenemos la posicion final
				CellPosition finalPos = drawingArrow.getFinalPos();
				// Cambiamos el tick y lo volvemos a asignar
				finalPos.tick = cell.tick;
				drawingArrow.setFinalPos(finalPos);
			}

			// Si la posición era una posición dentro del canvas, entonces
			// cambiamos los varlores.
			if (pos != null)
				// Actualizamos la posicion de la SimulationView, de manera que
				// dibuje la iluminación cuando pasa el cursor por encima
				simulationView.setPosition(simulationView.getPosition(e),
						drawingArrow.isValid(simulationModel));

		} else
			// Actualizamos la posicion de la SimulationView, de manera que
			// dibuje la iluminación cuando pasa el cursor por encima
			simulationView.setPosition(simulationView.getPosition(e), true);
	}
}
