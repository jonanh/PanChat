package simulation.arrows;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import simulation.model.SimulationModel;
import simulation.view.CellPosition;

@SuppressWarnings("serial")
public class MultipleArrow implements MessageArrow, Serializable {

	// Position inicial
	private CellPosition initialPos;

	// Posiciones finales
	private ArrayList<CellPosition> finalPos = new ArrayList<CellPosition>();

	// Lista de posiciones finales
	private ArrayList<SingleArrow> arrowList = new ArrayList<SingleArrow>();

	public MultipleArrow(CellPosition initialPos) {
		this.initialPos = initialPos;
	}

	public MultipleArrow(CellPosition initialPos, SingleArrow arrow) {
		this.initialPos = initialPos;
		this.addArrow(arrow);
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

		for (SingleArrow arrow : arrowList)
			arrow.setInitialPos(initialPos);

		this.initialPos = initialPos;
	}

	/**
	 * @return the finalPos
	 */
	public List<CellPosition> getFinalPos() {
		return finalPos;
	}

	/**
	 * Devuelve la flecha en la posicion position
	 * 
	 * @param position
	 * @return
	 */
	public SingleArrow getArrow(CellPosition position) {
		int index = finalPos.indexOf(position);
		return arrowList.get(index);
	}

	/**
	 * Añade una flecha
	 * 
	 * @param arrow
	 */
	public CellPosition addArrow(SingleArrow arrow) {
		// Buscamos si existe una flecha que va al mismo proceso
		int i = 0, index = -1;
		CellPosition cell = null;
		for (CellPosition pos : finalPos) {
			if (arrow.getFinalPos().process == pos.process) {
				// Guardamos la posición
				index = i;
				cell = pos;
				break;
			}
			i++;
		}
		// Si no existía una flecha que vaya a ese proceso, la añadimos
		if (index == -1) {
			arrowList.add(arrow);
			finalPos.add(arrow.getFinalPos());
		} else {
			// Sino la reemplazamos
			arrowList.set(index, arrow);
			finalPos.set(index, arrow.getFinalPos());
		}
		return cell;
	}

	/**
	 * Elimina una flecha
	 * 
	 * @param finalPos
	 * @return
	 */
	public SingleArrow deleteArrow(CellPosition position) {
		int index = finalPos.indexOf(position);

		this.finalPos.remove(index);
		return arrowList.remove(index);
	}

	/**
	 * Dibuja las flechas que contiene el MultipleArrow.
	 * 
	 * @param g
	 */
	@Override
	public void draw(Graphics2D g) {
		for (SingleArrow arrow : arrowList)
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
	public boolean isValid(SimulationModel simulationModel) {

		// Una flecha no puede ir hacia atrás
		for (CellPosition pos : finalPos)
			if (initialPos.tick >= pos.tick)
				return false;

		// Si el destino de la fecha apunta a una celda ya ocupada
		if (simulationModel.getMultipleArrow(initialPos) != null)
			return false;

		return true;
	}

	/**
	 * Añadimos la flecha a la simulacion y borramos las flechas invalidas.
	 * 
	 * @param simulationModel
	 */
	public void move(SimulationModel simulationModel) {

		Iterator<SingleArrow> iter = arrowList.iterator();
		while (iter.hasNext()) {
			SingleArrow arrow = iter.next();

			if (arrow.isValid == false)
				deleteArrow(arrow.getFinalPos());
		}

		simulationModel.addArrow(this);
	}

	/**
	 * Rutina para clonar MultipleArrows
	 */
	@Override
	public MessageArrow clone() {
		MultipleArrow newArrow = new MultipleArrow(initialPos);
		for (SingleArrow arrow : arrowList)
			newArrow.addArrow(new SingleArrow(initialPos, arrow.getFinalPos()));

		return newArrow;
	}
}
