package panchat.simulation.arrows;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import panchat.messages.Message.Type;

import panchat.simulation.model.SimulationArrowModel;
import panchat.simulation.view.CellPosition;

@SuppressWarnings("serial")
public class MultipleArrow implements MessageArrow, Serializable {

	// Position inicial
	protected CellPosition initialPos;

	// Lista de flechas
	protected List<SingleArrow> arrowList = new LinkedList<SingleArrow>();

	// Lista de posiciones
	protected List<CellPosition> positionList = new LinkedList<CellPosition>();

	protected CellPosition moveCell;

	// Propiedades de la flecha
	protected EnumMap<Type, Boolean> properties = new EnumMap<Type, Boolean>(
			Type.class);

	public MultipleArrow(CellPosition initialPos) {
		this.initialPos = initialPos;
	}

	public MultipleArrow(CellPosition initialPos, SingleArrow arrow) {
		this.initialPos = initialPos;
		this.addArrow(arrow);
	}

	/**
	 * @return La posicion inicial
	 */
	public CellPosition getInitialPos() {
		return initialPos;
	}

	/**
	 * @return La lista de posiciones.
	 */
	public Collection<CellPosition> getPositions() {
		return positionList;
	}

	/**
	 * @return Devuelve el conjunto de flechas que contiene el MultipleArrow
	 */
	public Collection<SingleArrow> getArrowList() {
		return arrowList;
	}

	/**
	 * Devuelve la flecha en la posicion position
	 * 
	 * @param position
	 * 
	 * @return
	 */
	public List<SingleArrow> getInitialArrow(CellPosition position) {

		LinkedList<SingleArrow> list = new LinkedList<SingleArrow>();
		for (SingleArrow arrow : arrowList) {
			if (arrow.getInitialPos().equals(position))
				list.add(arrow);
		}

		return list;
	}

	/**
	 * Devuelve la flecha en la posicion position
	 * 
	 * @param position
	 * 
	 * @return
	 */
	public List<SingleArrow> getFinalArrow(CellPosition position) {

		LinkedList<SingleArrow> list = new LinkedList<SingleArrow>();
		for (SingleArrow arrow : arrowList) {
			if (arrow.getFinalPos().equals(position))
				list.add(arrow);
		}

		return list;
	}

	/**
	 * Añade una flecha
	 * 
	 */
	public void addArrow(MessageArrow arrow) {

		if (arrow instanceof SingleArrow) {
			SingleArrow singleArrow = (SingleArrow) arrow;

			CellPosition initialPos = singleArrow.getInitialPos();
			CellPosition finalPos = singleArrow.getFinalPos();

			int index = positionList.indexOf(initialPos);
			if (index != -1) {
				initialPos = positionList.get(index);
				singleArrow.initialPos = initialPos;
			} else {
				positionList.add(initialPos);
			}

			index = positionList.indexOf(finalPos);
			if (index != -1) {
				finalPos = positionList.get(index);
				singleArrow.finalPos = finalPos;
			} else {
				positionList.add(finalPos);
			}

			singleArrow.setProperties(this.properties);

			arrowList.add(singleArrow);

		} else if (arrow instanceof MultipleArrow) {

			MultipleArrow multipleArrow = (MultipleArrow) arrow;

			for (SingleArrow singleArrow : getArrowList())
				multipleArrow.addArrow(singleArrow);

		}
	}

	/**
	 * Elimina una flecha
	 * 
	 * @param finalPos
	 * @return
	 */
	public boolean deleteArrow(CellPosition position) {

		// List of initial point. We need to delete nodes recursively out of the
		// iterator (ConcurrentModificationException).
		LinkedList<CellPosition> delFinalPos = new LinkedList<CellPosition>();

		Iterator<SingleArrow> iter = arrowList.iterator();
		while (iter.hasNext()) {
			SingleArrow arrow = iter.next();

			if (arrow.initialPos.equals(position)) {
				delFinalPos.add(arrow.finalPos);
				iter.remove();
			} else if (arrow.finalPos.equals(position)) {
				iter.remove();
			}
		}

		// Borrado recursivo
		for (CellPosition pos : delFinalPos)
			this.deleteArrow(pos);

		// Borramos la posicion
		int index = positionList.indexOf(position);
		positionList.remove(index);

		return arrowList.size() <= 0;
	}

	/**
	 * Dibuja las flechas que contiene el MultipleArrow.
	 * 
	 * @param g
	 */
	@Override
	public void draw(Graphics2D g) {
		for (MessageArrow arrow : arrowList)
			arrow.draw(g);
	}

	@Override
	public String toString() {
		return "Flechas[[ " + arrowList + " ]]";
	}

	/**
	 * Verificamos si se encuentra en un lugar válido y/o libre :
	 * 
	 * <ul>
	 * <li>Una flecha no puede tener flechas que vayan hacia atrás.</li>
	 * <li>Una flecha no puede apuntar a una celda ya ocupada.</li>
	 * </ul>
	 * 
	 * @param messageArrow
	 * 
	 * @return Si es valida la flecha
	 */
	public boolean isValid(SimulationArrowModel simulationModel) {

		boolean isValid = true;

		// Activamos el isValid para cada una de las flechas
		for (SingleArrow arrow : arrowList)
			if (!arrow.isValid(simulationModel))
				isValid = false;

		return isValid;
	}

	@Override
	public EnumMap<Type, Boolean> getProperties() {
		return properties;
	}

	/**
	 * 
	 * @param newPosition
	 * @return
	 */
	public CellPosition move(CellPosition newPosition) {
		int index = positionList.indexOf(newPosition);
		this.moveCell = positionList.get(index);
		return moveCell;
	}

	/**
	 * Inicializar las flechas después de
	 */
	public void initialize() {
		for (SingleArrow arrow : arrowList)
			arrow.initialize();
	}

	/**
	 * Añadimos la flecha a la simulacion y borramos las flechas invalidas.
	 * 
	 * @param simulationModel
	 */
	public boolean add2Simulation(SimulationArrowModel simulationModel) {
		simulationModel.addArrow(this);
		moveCell = null;
		return true;
	}

	/**
	 * Rutina para clonar MultipleArrows
	 */
	@Override
	public MultipleArrow clone() {
		MultipleArrow newArrow = new MultipleArrow(initialPos);

		for (SingleArrow arrow : arrowList)
			newArrow.addArrow(arrow.clone());

		return newArrow;
	}
}
