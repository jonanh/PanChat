package simulation.view.listener;

import java.awt.event.MouseEvent;

import simulation.arrows.MessageArrow;
import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;
import simulation.view.CutPosition;
import simulation.view.Position;
import simulation.view.SimulationView;

public class MoveListener extends ViewListener {

	private MessageArrow moveArrow;
	private MessageArrow drawingArrow;
	private boolean initialOrFinal;

	public MoveListener(SimulationView simulationView) {
		super(simulationView);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Obtenemos la posición y la flecha en esa posición
		Position pos = simulationView.getPosition(e);

		// Si la posicion es una celda
		if (pos instanceof CellPosition) {

			CellPosition cell = (CellPosition) pos;
			drawingArrow = simulationModel.getArrow(pos);

			// Si existe una flecha, entonces la copiamos a move
			if (drawingArrow != null) {
				// hacemos una copia (backup) de la flecha que vamos a mover
				moveArrow = simulationModel.deleteArrow(cell);

				// Es inicial o final?
				initialOrFinal = moveArrow.getInitialPos().equals(pos);

				// Creamos una nueva flecha igual que la que estamos moviendo
				drawingArrow = moveArrow.clone();

				simulationView.setDrawingArrow(drawingArrow);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		// Si estamos dibujando
		if (drawingArrow != null) {

			MessageArrow arrow = simulationView.getDrawingArrow();

			// Eliminamos la flecha en dibujo
			simulationView.setDrawingArrow(null);

			// si la nueva posicion es valida la añadimos a la lista de flechas.
			if (arrow.isValid(simulationModel)) {

				simulationModel.addArrow(arrow);

			}
			// Si no es valida, y estabamos moviendo una flecha que ya existía,
			// volvemos a incluir la copia antigua que teniamos de la flecha
			// antes de moverla.
			else if (moveArrow != null) {

				simulationModel.addArrow(moveArrow);

				// Quitamos la flecha que estabamos moviendo
				moveArrow = null;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		// Si estamos dibujando
		if (drawingArrow != null) {

			// Obtenemos la posición
			Position pos = simulationView.getPosition(e);

			CellPosition cell;

			// Si la posicion es una celda
			if (pos instanceof CellPosition) {

				cell = (CellPosition) pos;

			}
			// Si es una columna, dejamos apuntando al mismo proceso, pero
			// cambiamos a que tick apunta
			else {

				CutPosition cut = (CutPosition) pos;

				if (initialOrFinal) {
					cell = drawingArrow.getInitialPos();
				} else {
					SingleArrow arrow = (SingleArrow) drawingArrow;
					cell = arrow.getFinalPos();
				}
				// Cambiamos el tick y lo volvemos a asignar
				cell.tick = cut.tick;
			}

			if (initialOrFinal) {
				drawingArrow.setInitialPos(cell);
			} else {
				SingleArrow arrow = (SingleArrow) drawingArrow;
				arrow.setFinalPos(cell);
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
}
