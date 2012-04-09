package simulation.view.listener;

import java.awt.event.MouseEvent;

import simulation.arrows.MultipleArrow;
import simulation.view.CellPosition;
import simulation.view.CutPosition;
import simulation.view.IPositionObserver;
import simulation.view.IPosition;
import simulation.view.SimulationView;

/**
 * Esta clase controla el comportamiento del View cuando hay que mover una
 * flecha, por lo que actuará de controlador del patrón MVC.
 * 
 * Durante el movimiento de flechas, se permite :
 * 
 * - Mover los puntos de una flecha existente. Si una flecha se mueve a una
 * posición inválida y soltamos la flecha en esa posición inválida restauraremos
 * el estado previo.
 * 
 * Para ello cuando movemos una flecha en realidad lo que hacemos :
 * 
 * <ul>
 * <li>Quitamos del SimulationArrowModel la flecha que vamos a mover</li>
 * <li>Almacenamos temporalmente dicha flecha en una variable de trabajo en el
 * propio view (drawingArrow)</li>
 * <li>Cuando fijamos la flecha (soltamos el ratón), establecemos nuevamente a
 * null la variable temporal y volvemos a añadir la flecha al
 * SimulationArrowModel</li>
 * </ul>
 * 
 * Notas :
 * 
 * Cada flecha define si una nueva posición es válida o no mediante su método
 * isValid(SimulationData). De este modo cada tipo de flecha puede determinar si
 * en la posición nueva sería o no válida.
 * 
 * La encargada de fijarse definitivamente sobre el Model es la propia flecha
 * mediante el método move(SimulationData), de este modo flechas con un
 * comportamiento más complejo pueden gestionar como quieren moverse.
 * 
 */
public class MoveListener extends ViewListener {

	// Flecha de backup, si movemos a una posicion invalida e intentamos
	// fijarla, volveremos sobre esta flecha.
	protected MultipleArrow moveArrow = null;

	// Flecha de dibujo.
	protected MultipleArrow drawingArrow = null;

	// Guardamos la posición previa, para evitar recalcular si no cambiamos de
	// celda.
	protected CellPosition lastPosition;

	public MoveListener(SimulationView simulationView) {
		super(simulationView);
	}

	/**
	 * Tras pulsar el ratón comienza una acción de mover/arrastrar
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// No hay ninguna flecha en movimiento, luego debemos buscarla.
		if (drawingArrow == null) {

			// Obtenemos la posición a partir de la información del evento.
			IPosition pos = simulationView.getPosition(e);

			// Si la posicion es una celda
			if (pos instanceof CellPosition) {

				// Guardamos la posición en lastPosition
				lastPosition = (CellPosition) pos;

				// Eliminamos la flecha del model y la guardamos en la variable
				// temporal drawingArrow
				drawingArrow = simulationModel.deleteArrow(lastPosition);

			}
			// Si fuera una columna, no merece la pena buscar la flecha.
		}

		// Si existe una flecha, entonces la copiamos a move
		if (drawingArrow != null) {

			// hacemos una copia (backup) de la flecha que vamos a mover.
			// La razón es que al arrastrar cambiaremos los valores del extremo
			// de la flecha, y necesitamos una referencia nueva.
			moveArrow = drawingArrow.clone();

			// Indicamos a la flecha que estamos moviendo la posición
			// lastPosition
			drawingArrow.setMovingCell(lastPosition);

			// Establecemos la flecha en modo dibujo en el SimulationView
			simulationView.setDrawingArrow(drawingArrow);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		// Si estamos dibujando
		if (drawingArrow != null) {

			// Si la nueva posicion es valida la añadimos a la lista de flechas.
			if (drawingArrow.isValid(simulationModel)) {

				drawingArrow.add2Simulation(simulationModel);

			}
			// Si no es válida, y estábamos moviendo una flecha que ya existía,
			// volvemos a incluir la copia antigua que teniamos
			else if (moveArrow != null) {

				moveArrow.add2Simulation(simulationModel);

			}
		}

		// Quitamos la flecha de backup
		simulationView.setDrawingArrow(null);
		moveArrow = null;
		drawingArrow = null;
		lastPosition = null;

		// Actualizamos la posición de la SimulationView, de manera que
		// dibuje la iluminación cuando pasa el cursor por encima
		simulationView.setPosition(simulationView.getPosition(e),
				IPositionObserver.Mode.Over, true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		// Comprobamos si estamos moviendo una flecha
		if (drawingArrow != null) {

			CellPosition cell = null;

			// Obtenemos la posición a partir de la información del evento.
			IPosition pos = simulationView.getPosition(e);

			// Si la posición es una celda
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

				// Indicamos a la flecha en movimiento la nueva posición en la
				// que está el vertice que estábamos moviendo.
				drawingArrow.move(lastPosition);

				// Actualizamos la posicion de la SimulationView, de manera que
				// dibuje la iluminación cuando pasa el cursor por encima
				simulationView.setPosition(cell, IPositionObserver.Mode.Over,
						drawingArrow.isValid(simulationModel));
			}

		} else {
			// Actualizamos la posicion de la SimulationView, de manera que
			// dibuje la iluminación cuando pasa el cursor por encima
			simulationView.setPosition(simulationView.getPosition(e),
					IPositionObserver.Mode.Over, true);
		}
	}
}
