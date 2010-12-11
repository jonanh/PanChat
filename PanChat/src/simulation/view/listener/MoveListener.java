package simulation.view.listener;

import java.awt.event.MouseEvent;

import simulation.arrows.MessageArrow;
import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;
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
				drawingArrow = new MessageArrow(moveArrow.getInitialPos(),
						moveArrow.getFinalPos());
				simulationView.setDrawingArrow(drawingArrow);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		MessageArrow arrow = simulationView.getDrawingArrow();

		// Eliminamos la flecha en dibujo
		simulationView.setDrawingArrow(null);

		// si la nueva posicion es valida la añadimos a la lista de flechas.
		if (simulationModel.isValidArrow(arrow)) {

			simulationModel.addArrow(arrow);

		} // Si no es valida, y estabamos moviendo una flecha que ya existía,
		// volvemos a incluir la copia antigua que teniamos de la flecha
		// antes de moverla.
		else if (moveArrow != null) {

			simulationModel.addArrow(moveArrow);

			// Quitamos la flecha que estabamos moviendo
			moveArrow = null;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		// Obtenemos la posición
		Position pos = simulationView.getPosition(e);

		// Si la posicion es una celda
		if (pos instanceof CellPosition) {

			CellPosition cell = (CellPosition) pos;

			if (initialOrFinal) {
				drawingArrow.setInitialPos(cell);
			} else {
				drawingArrow.setFinalPos(cell);
			}
			// Actualizamos la posicion de la SimulationView, de manera que
			// dibuje la iluminación cuando pasa el cursor por encima
			super.mouseDragged(e);
		}
	}

	/**
	 * Verificamos si messageArrow es una flecha que se encuentra en un lugar
	 * válido y/o libre :
	 * 
	 * <ul>
	 * <li>Una flecha no puede ir de a el mismo proceso.</li>
	 * <li>Una flecha no puede ir hacia atrás.</li>
	 * <li>Una flecha no puede apuntar a una celda ya ocupada.</li>
	 * </ul>
	 * 
	 * @param messageArrow
	 * 
	 * @return Si es valida la flecha
	 */
	public boolean isValidArrow(SingleArrow messageArrow) {

		CellPosition initialPos = messageArrow.getInitialPos();
		CellPosition finalPos = messageArrow.getFinalPos();

		// Una flecha no puede ir de a el mismo proceso
		if (initialPos.process == finalPos.process)
			return false;

		// Una flecha no puede ir hacia atrás
		if (initialPos.tick >= finalPos.tick)
			return false;

		// Si el destino de la fecha apunta a una celda ya ocupada
		if (this.simulationModel.getMultipleArrow(finalPos) != null)
			return false;

		return true;
	}
}
