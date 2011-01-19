package simulation.arrows;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;

import order.Message.Type;

import simulation.model.SimulationArrowModel;
import simulation.view.CellPosition;
import simulation.view.SimulationView;

public class SingleArrow extends Arrow implements MessageArrow, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Color NORMAL_COLOR = Color.getHSBColor(2 * .4f % 1,
			.70f, 1f);
	private static final Color FIFO_COLOR = Color.getHSBColor(4 * .4f % 1,
			.70f, 1f);
	private static final Color CAUSAL_COLOR = Color.getHSBColor(6 * .4f % 1,
			.70f, 1f);
	private static final Color TOTAL_COLOR = Color.getHSBColor(8 * .4f % 1,
			.70f, 1f);

	// Posiciones inicial y final de la flecha
	protected CellPosition initialPos;
	protected CellPosition finalPos;

	// Propiedades de la flecha
	private EnumMap<Type, Boolean> properties;

	public SingleArrow(CellPosition initialPos, CellPosition finalPos) {
		this(initialPos, finalPos, new EnumMap<Type, Boolean>(Type.class));
	}

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			Color color) {
		super(0f, 0f, 0f, 0f, color);
		this.initialPos = initialPos;
		this.finalPos = finalPos;
		update();
	}

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			Color color, float strokeWidth) {
		super(0f, 0f, 0f, 0f, color, strokeWidth);
		this.initialPos = initialPos;
		this.finalPos = finalPos;
		update();
	}

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			EnumMap<Type, Boolean> properties) {
		super(0f, 0f, 0f, 0f);
		this.properties = properties;
		this.initialPos = initialPos;
		this.finalPos = finalPos;
		update();
	}

	private void update() {
		Point2D.Float pos1 = SimulationView.PositionCoords(initialPos);
		Point2D.Float pos2 = SimulationView.PositionCoords(finalPos);

		super.setLine(pos1, pos2);
	}

	/**
	 * @return the initialPos
	 */
	public CellPosition getInitialPos() {
		return initialPos;
	}

	/**
	 * @return the finalPos
	 */
	public CellPosition getFinalPos() {
		return finalPos;
	}

	public void setInitialPos(CellPosition pos) {
		initialPos = pos;
	}

	@Override
	public EnumMap<Type, Boolean> getProperties() {
		return properties;
	}

	public void setProperties(EnumMap<Type, Boolean> properties) {

		this.properties.putAll(properties);

		if (properties.containsKey(Type.TOTAL)) {
			this.color = TOTAL_COLOR;
		}
		if (properties.containsKey(Type.CAUSAL)) {
			this.color = CAUSAL_COLOR;
		}
		if (properties.containsKey(Type.FIFO)) {
			this.color = FIFO_COLOR;
		} else {
			this.color = NORMAL_COLOR;
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
	public boolean isValid(SimulationArrowModel simulationModel) {

		// Si estamos comprobando la validez de la flecha, es que estamos
		// moviendo la flecha, luego recalcular la pendiente.
		update();

		// Una flecha no puede ir de a el mismo proceso
		if (initialPos.process == finalPos.process)
			return isValid = false;

		// Una flecha no puede ir hacia atrás
		if (initialPos.tick >= finalPos.tick)
			return isValid = false;

		// Si el destino de la fecha apunta a una celda ya ocupada
		if (simulationModel.getArrow(finalPos) != null && listContains(simulationModel.getArrow(finalPos).getPositions(),finalPos))
			return isValid = false;

		return isValid = true;
	}

	/**
	 * Rutina para clonar MultipleArrows
	 */
	@Override
	public SingleArrow clone() {
		return new SingleArrow(initialPos.clone(), finalPos.clone());
	}

	@Override
	public String toString() {
		return "Flecha[ " + initialPos + finalPos + " ]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SingleArrow) {
			SingleArrow arrow = (SingleArrow) obj;
			return this.initialPos.equals(arrow.initialPos)
					&& this.finalPos.equals(arrow.finalPos);
		}
		return false;
	}

	private static boolean listContains(Collection<CellPosition> collection,
			CellPosition cell) {
		for (CellPosition pos : collection) {
			if (pos.equals(cell) && pos != cell)
				return true;
		}
		return false;
	}
}
