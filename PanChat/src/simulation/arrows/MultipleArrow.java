package simulation.arrows;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import simulation.view.CellPosition;

@SuppressWarnings("serial")
public class MultipleArrow implements MessageArrow, Serializable {

	// Position inicial
	private CellPosition initialPos;

	// Posiciones finales
	private ArrayList<CellPosition> finalPos = new ArrayList<CellPosition>();

	// Lista de posiciones finales
	private ArrayList<SingleArrow> arrowList = new ArrayList<SingleArrow>();

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
			arrow.initialPos = initialPos;

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
			if (arrow.finalPos.process == pos.process) {
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
			finalPos.add(arrow.finalPos);
		} else {
			// Sino la reemplazamos
			arrowList.set(index, arrow);
			finalPos.set(index, arrow.finalPos);
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
	public void draw(Graphics2D g) {
		for (SingleArrow arrow : arrowList)
			arrow.draw(g);
	}

	@Override
	public String toString() {
		return "Flechas[[ " + arrowList + " ]]";
	}
}
