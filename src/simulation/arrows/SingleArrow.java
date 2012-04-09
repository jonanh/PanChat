package simulation.arrows;

import java.awt.Color;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;

import order.Message.Type;

import simulation.model.SimulationArrowModel;
import simulation.view.CellPosition;

public class SingleArrow extends Arrow implements MessageArrow, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Color NORMAL_COLOR = Color.getHSBColor(2 * .4f % 1,
			.50f, .8f);
	private static final Color FIFO_COLOR = Color.getHSBColor(4 * .4f % 1,
			.50f, .8f);
	private static final Color CAUSAL_COLOR = Color.getHSBColor(6 * .4f % 1,
			.50f, .8f);
	private static final Color TOTAL_COLOR = Color.getHSBColor(8 * .4f % 1,
			.50f, .8f);

	// Propiedades de la flecha
	private EnumMap<Type, Boolean> properties = new EnumMap<Type, Boolean>(
			Type.class);

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			Type... properties) {

		super(initialPos, finalPos, NORMAL_COLOR);

		setProperties(properties);
	}

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			Color color, Type... properties) {

		super(initialPos, finalPos, color);

		setProperties(properties);
	}

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			Color color, float strokeWidth, Type... properties) {

		super(initialPos, finalPos, color, strokeWidth);

		setProperties(properties);
	}

	public SingleArrow(CellPosition initialPos, CellPosition finalPos,
			Color color, EnumMap<Type, Boolean> properties) {

		super(initialPos, finalPos);

		setProperties(properties);
	}

	@Override
	public EnumMap<Type, Boolean> getProperties() {
		return properties;
	}

	public void setProperties(Type[] properties) {
		this.properties.clear();

		for (Type type : properties)
			setProperty(type, true);
	}

	public void setProperties(EnumMap<Type, Boolean> properties2) {
		Iterator<Entry<Type, Boolean>> iter = properties2.entrySet().iterator();

		while (iter.hasNext()) {
			Entry<Type, Boolean> entry = iter.next();
			setProperty(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Devuelve si el mensaje es del tipo property
	 * 
	 * @param property
	 * @return
	 */
	public Boolean isType(Type property) {
		return this.properties.containsKey(property)
				&& this.properties.get(property) == true;
	}

	public void setProperty(Type property, Boolean bool) {
		this.properties.put(property, bool);

		if (isType(Type.TOTAL))
			this.color = TOTAL_COLOR;

		else if (isType(Type.CAUSAL))
			this.color = CAUSAL_COLOR;

		else if (isType(Type.FIFO))
			this.color = FIFO_COLOR;

		else
			this.color = NORMAL_COLOR;
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
	public boolean isValid(SimulationArrowModel simulationModel,
			MultipleArrow arrow) {

		isValid = true;

		// Una flecha no puede ir hacia atrás
		if (initialPos.tick >= finalPos.tick)
			isValid = false;

		// Si el destino de la fecha apunta a una celda ya ocupada.
		// Debemos comprobar que la flecha que apunta no apunta sobre un nodo de
		// la propia flecha multiple, como por ejemplo en el caso de una flecha
		// total. Además una flecha total puede comprobar la validez de sus
		// flechas estando incluida dentro del model, razón por la que arrow2 y
		// arrow pueden ser iguales.
		MultipleArrow arrow2 = simulationModel.getArrow(finalPos);
		if (arrow2 != null && arrow2 != arrow)
			isValid = false;

		// else
		// for (CellPosition pos : arrow.getPositions())
		// if (pos.equals(finalPos) && finalPos != pos)
		// isValid = false;

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
		return new SingleArrow(initialPos.clone(), finalPos.clone(), color,
				properties.clone());
	}
}
