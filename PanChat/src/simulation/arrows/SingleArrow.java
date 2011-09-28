package simulation.arrows;

import java.awt.Color;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;

import order.Message.Type;

import simulation.model.SimulationArrowModel;
import simulation.view.CellPosition;

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

	// Propiedades de la flecha
	private EnumMap<Type, Boolean> properties;

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			Type... properties) {

		this(initialPos, finalPos, new EnumMap<Type, Boolean>(Type.class));

		for (Type type : properties) {
			this.properties.put(type, true);
		}
	}

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			Color color, Type... properties) {

		super(initialPos, finalPos, color);

		for (Type type : properties) {
			this.properties.put(type, true);
		}
	}

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			Color color, float strokeWidth, Type... properties) {

		super(initialPos, finalPos, color, strokeWidth);

		for (Type type : properties) {
			this.properties.put(type, true);
		}
	}

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			EnumMap<Type, Boolean> properties) {

		super(initialPos, finalPos);

		this.properties = properties;
	}

	@Override
	public EnumMap<Type, Boolean> getProperties() {
		return properties;
	}

	public void setProperties(EnumMap<Type, Boolean> properties) {
	}

	public void setProperties(Type[] properties) {
		this.properties.clear();
	}

	public void setProperty(Type property, Boolean bool) {
		this.properties.put(property, bool);

		if (properties.containsKey(Type.TOTAL)) {
			this.color = TOTAL_COLOR;
		} else if (properties.containsKey(Type.CAUSAL)) {
			this.color = CAUSAL_COLOR;
		} else if (properties.containsKey(Type.FIFO)) {
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

		isValid = true;

		// Una flecha no puede ir hacia atrás
		if (initialPos.tick >= finalPos.tick)
			isValid = false;

		// Si el destino de la fecha apunta a una celda ya ocupada
		else if (simulationModel.getArrow(finalPos) != null
				&& listContains(simulationModel.getArrow(finalPos)
						.getPositions(), finalPos))
			isValid = false;

		// Si estamos comprobando la validez de la flecha, es que estamos
		// moviendo la flecha, luego recalcular la pendiente.
		update();

		return isValid;
	}

	/**
	 * Rutina para clonar MultipleArrows
	 */
	@Override
	public SingleArrow clone() {
		return new SingleArrow(initialPos.clone(), finalPos.clone());
	}

	/**
	 * 
	 * @param collection
	 * @param cell
	 * @return Devuelve si en "collection" existe una instancia de celda
	 *         diferente pero equivalente a "cell".
	 */
	private static boolean listContains(Collection<CellPosition> collection,
			CellPosition cell) {
		for (CellPosition pos : collection) {
			if (pos.equals(cell) && pos != cell)
				return true;
		}
		return false;
	}
}
