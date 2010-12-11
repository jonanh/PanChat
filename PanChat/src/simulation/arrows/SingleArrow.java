package simulation.arrows;

import java.awt.geom.Point2D;
import java.io.Serializable;

import simulation.model.SimulationModel;
import simulation.view.CellPosition;
import simulation.view.SimulationView;

@SuppressWarnings("serial")
public class SingleArrow extends Arrow implements MessageArrow, Serializable {

	private CellPosition initialPos;
	private CellPosition finalPos;

	public SingleArrow(CellPosition initialPos, CellPosition finalPos) {
		super(0f, 0f, 0f, 0f);
		update(initialPos, finalPos);
	}

	private void update(CellPosition initialPos, CellPosition finalPos) {
		this.initialPos = initialPos;
		this.finalPos = finalPos;

		Point2D.Float pos1 = SimulationView.PositionCoords(initialPos);
		Point2D.Float pos2 = SimulationView.PositionCoords(finalPos);

		super.setLine(pos1, pos2);
	}

	/**
	 * @return the finalPos
	 */
	public CellPosition getFinalPos() {
		return finalPos;
	}

	/**
	 * @param finalPos
	 *            the finalPos to set
	 */
	public void setFinalPos(CellPosition finalPos) {
		update(initialPos, finalPos);
	}

	/**
	 * @return the initialPos
	 */
	public CellPosition getInitialPos() {
		return initialPos;
	}

	/**
	 * @param initialPos
	 *            the finalPos to set
	 */
	public void setInitialPos(CellPosition initialPos) {
		update(initialPos, finalPos);
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
	public boolean isValid(SimulationModel simulationModel) {

		// Una flecha no puede ir de a el mismo proceso
		if (initialPos.process == finalPos.process)
			return isValid = false;

		// Una flecha no puede ir hacia atrás
		if (initialPos.tick >= finalPos.tick)
			return isValid = false;

		// Si el destino de la fecha apunta a una celda ya ocupada
		if (simulationModel.getMultipleArrow(finalPos) != null)
			return isValid = false;

		return isValid = true;
	}

	@Override
	public String toString() {
		return "Flecha[ " + initialPos + finalPos + " ]";
	}

	/**
	 * Rutina para clonar MultipleArrows
	 */
	@Override
	public MessageArrow clone() {
		return new SingleArrow(initialPos, finalPos);
	}
}
