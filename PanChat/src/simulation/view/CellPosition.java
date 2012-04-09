package simulation.view;

import java.io.Serializable;

/**
 * Clase que representa las posiciones en el tablero de la simulación.
 */
public class CellPosition implements Serializable, IPosition {

	private static final long serialVersionUID = 1L;

	public int process;
	public int tick;

	public CellPosition(int numProcess, int tick) {
		this.process = numProcess;
		this.tick = tick;
	}

	/**
	 * Método para establecer las posición a partir de otra celda.
	 * 
	 * De este modo evitamos tener que reemplazar las celdas en flechas. Si una
	 * posición es referenciada desde varias flechas, por ejemplo 2 flechas
	 * vinculadas, estableciendo las nuevas coordenadas de la posición afecta a
	 * ambas flechas.
	 * 
	 * @param pos
	 */
	public void set(CellPosition pos) {
		this.process = pos.process;
		this.tick = pos.tick;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + process;
		result = prime * result + tick;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CellPosition) {
			CellPosition other = (CellPosition) obj;
			return (tick == other.tick && process == other.process);
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + process + "," + tick + ")";
	}

	@Override
	public CellPosition clone() {
		return new CellPosition(process, tick);
	}
}
