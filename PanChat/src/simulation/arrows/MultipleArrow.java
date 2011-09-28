package simulation.arrows;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import order.Message.Type;

import simulation.model.SimulationArrowModel;
import simulation.view.CellPosition;

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
	 * Devuelve la lista de flechas que tienen como posición inicial position
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
	 * Devuelve la lista de flechas que tiene como posición final position
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

			// Como intentamos tener una única instancia de cada posición,
			// buscamos en nuestra lista de posiciones e intentamos usar la
			// misma instancia de la posición si esta ya existía.

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

			// La single arrow heredará las propiedades de esta flecha
			// Nota: Usamos la misma referencia de propiedades, con lo cual si
			// cambiamos las propiedades de la flecha multiple, cambiaremos las
			// propiedades de las flechas que la componen.
			singleArrow.setProperties(this.properties);

			// La añadimos a la lista de flechas
			arrowList.add(singleArrow);

		}
		// Si es una flecha multiple añadimos cada SingleArrows que la componen
		else if (arrow instanceof MultipleArrow) {

			MultipleArrow multipleArrow = (MultipleArrow) arrow;

			for (SingleArrow singleArrow : multipleArrow.getArrowList())
				this.addArrow(singleArrow);
		}
	}

	/**
	 * Elimina las flechas de la posición position. Si position es la posición
	 * inicial de un grupo de flechas dentro de la flecha multiple entonces
	 * realizamos un borrado recursivo de dichas flechas.
	 * 
	 * @param position
	 * 
	 * @return Devuelve si la flecha multiple resultante tras la eliminación
	 *         contiene más de una subflecha
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
		if (index != -1)
			positionList.remove(index);

		return arrowList.size() <= 0;
	}

	private void setProperty(Type property, Boolean bool) {
		this.properties.put(property, bool);
	}

	public void setProperties(Type[] properties) {
		this.properties.clear();

		for (Type type : properties)
			setProperty(type, true);

		for (SingleArrow arrow : this.getArrowList())
			arrow.setProperties(this.properties);
	}

	public void setProperties(EnumMap<Type, Boolean> properties2) {
		Iterator<Entry<Type, Boolean>> iter = properties2.entrySet().iterator();

		while (iter.hasNext()) {
			Entry<Type, Boolean> entry = iter.next();
			setProperty(entry.getKey(), entry.getValue());
		}

		for (SingleArrow arrow : this.getArrowList())
			arrow.setProperties(this.properties);
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

	/**
	 * Dibuja las flechas que contiene el MultipleArrow.
	 * 
	 * @param g
	 * @param color
	 */
	@Override
	public void draw(Graphics2D g, Color color) {
		for (MessageArrow arrow : arrowList)
			arrow.draw(g, color);
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

		// Si el destino de la fecha apunta a una celda ya ocupada
		if (simulationModel.getArrow(moveCell) != null)
			isValid = false;

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
	 * Método para indicar al multiple arrow que estamos moviendo uno de sus
	 * vertices.
	 * 
	 * @param newPosition
	 */
	public void setMovingCell(CellPosition newPosition) {
		int index = positionList.indexOf(newPosition);
		if (index != -1)
			this.moveCell = positionList.get(index);
		else
			throw new RuntimeException("Flecha no encontrada:"
					+ newPosition.toString());
	}

	/**
	 * Método que sirve para indicar al multiple arrow que el vertice en
	 * movimiento ahora está en newPosition.
	 * 
	 * @param newPosition
	 * 
	 * @return
	 */
	public void move(CellPosition newPosition) {
		moveCell.set(newPosition);
	}

	/**
	 * Añadimos la flecha a la simulacion y borramos las flechas invalidas.
	 * 
	 * @param simulationModel
	 */
	public boolean add2Simulation(SimulationArrowModel simulationModel) {

		moveCell = null;

		/*
		 * Comprobamos si añadimos la flecha a una posición vacia o a una
		 * posición que contiene otra flecha.
		 * 
		 * Si tuviera una flecha, añadimos la flecha actual a la flecha
		 * existente.
		 */
		if (simulationModel.getArrow(moveCell) == null)
			simulationModel.addArrow(this);
		else
			simulationModel.getArrow(moveCell).addArrow(this);

		return true;
	}

	/**
	 * Rutina para clonar MultipleArrows
	 */
	@Override
	public MultipleArrow clone() {
		MultipleArrow newArrow = new MultipleArrow(initialPos);

		newArrow.properties = this.properties.clone();

		for (SingleArrow arrow : arrowList)
			newArrow.addArrow(arrow.clone());

		return newArrow;
	}
}
