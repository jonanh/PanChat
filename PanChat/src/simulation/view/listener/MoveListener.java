package simulation.view.listener;

import java.awt.event.MouseEvent;

import simulation.arrows.MultipleArrow;
import simulation.view.CellPosition;
import simulation.view.CutPosition;
import simulation.view.IPositionObserver;
import simulation.view.IPosition;
import simulation.view.SimulationView;

/**
 * Esta clase controla el comportamiento del View cuando hay que mover toda la
 * clase de flechas.
 * 
 * Durante el movimiento de flechas, se permite :
 * 
 * - Mover los puntos de una flecha. Si una flecha se mueve a una posición
 * inválida, esta volverá tal y como estaba.
 * 
 * Cuando movemos una flecha, en realidad lo que hacemos es quitar dicha flecha
 * del Model y guardarla de manera especial en el View. Cuando finalmente
 * fijamos la flecha, destruimos la flecha en dibujo y la añadimos
 * definitivamente al model.
 * 
 * Notas :
 * 
 * Cada flecha define si la nueva posición es válida o no mediante su método
 * isValid(SimulationData). De este modo cada tipo de flecha puede determinar si
 * en la posición nueva sería o válida.
 * 
 * La encarga de fijarse definitivamente sobre el Model es la propia flecha
 * mediante el método move(SimulationData), de este modo flechas con un
 * comportamiento más complejo pueden gestionar como quieren moverse.
 * 
 */
public class MoveListener extends ViewListener {

	// Flecha de backup, si movemos a una posicion invalida, volveremos sobre
	// esta flecha.
	protected MultipleArrow moveArrow = null;

	// Flecha de dibujo.
	protected MultipleArrow drawingArrow = null;

	// Guardamos la ultima posicion, para evitar recalcular si no cambiamos de
	// celda.
	protected CellPosition lastPosition;

	// Guardamos la posicion que estamos moviendo.
	protected CellPosition movingCell;

	public MoveListener(SimulationView simulationView) {
		super(simulationView);
	}

	@Override
	public void mousePressed(MouseEvent e) {

		// Si existe una flecha, entonces la copiamos a move
		if (drawingArrow == null) {

			// Obtenemos la posición y la flecha en esa posición
			IPosition pos = simulationView.getPosition(e);

			// Si la posicion es una celda
			if (pos instanceof CellPosition) {

				lastPosition = (CellPosition) pos;
				drawingArrow = simulationModel.deleteArrow(lastPosition);

			}
		}
		// Si existe una flecha, entonces la copiamos a move
		if (drawingArrow != null) {

			// hacemos una copia (backup) de la flecha que vamos a mover
			moveArrow = drawingArrow.clone();

			movingCell = drawingArrow.move(lastPosition);

			// Establecemos la flecha en modo dibujo en el SimulationView
			simulationView.setDrawingArrow(drawingArrow);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		// Si estamos dibujando
		if (drawingArrow != null) {

			// si la nueva posicion es valida la añadimos a la lista de flechas.
			if (drawingArrow.isValid(simulationModel)) {

				drawingArrow.add2Simulation(simulationModel);

			}
			// Si no es valida, y estabamos moviendo una flecha que ya existía,
			// volvemos a incluir la copia antigua que teniamos de la flecha
			// antes de moverla.
			else if (moveArrow != null) {

				moveArrow.add2Simulation(simulationModel);

			}
		}

		// Quitamos la flecha de backup
		simulationView.setDrawingArrow(null);
		moveArrow = null;
		drawingArrow = null;
		lastPosition = null;
		movingCell = null;

		// Actualizamos la posicion de la SimulationView, de manera que
		// dibuje la iluminación cuando pasa el cursor por encima
		simulationView.setPosition(simulationView.getPosition(e),
				IPositionObserver.Mode.Over, true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		// Si estamos dibujando
		if (drawingArrow != null) {

			// Obtenemos la posición
			IPosition pos = simulationView.getPosition(e);

			CellPosition cell = null;

			// Si la posicion es una celda
			if (pos instanceof CellPosition) {

				cell = (CellPosition) pos;

			}
			// Si es una columna, dejamos apuntando al mismo proceso, pero
			// cambiamos a que tick apunta
			else if (pos instanceof CutPosition) {

				CutPosition cut = (CutPosition) pos;

				cell = lastPosition.clone();

				// Cambiamos el tick y lo volvemos a asignar
				cell.tick = cut.tick;
			}

			// Si la posición era una posición dentro del canvas, entonces
			// cambiamos los valores.
			// Si la posicion anterior es distinta a la nueva posicion, entonces
			// se produce un movimiento.
			if (pos != null && !lastPosition.equals(cell)) {

				// Actualizamos la última posicion.
				lastPosition = cell;

				if (movingCell != null)
					movingCell.set(cell);

				drawingArrow.move(cell);

				// Actualizamos la posicion de la SimulationView, de manera que
				// dibuje la iluminación cuando pasa el cursor por encima
				simulationView.setPosition(simulationView.getPosition(e),
						IPositionObserver.Mode.Over, drawingArrow
								.isValid(simulationModel));
			}

		} else
			// Actualizamos la posicion de la SimulationView, de manera que
			// dibuje la iluminación cuando pasa el cursor por encima
			simulationView.setPosition(simulationView.getPosition(e),
					IPositionObserver.Mode.Over, true);
	}
}
