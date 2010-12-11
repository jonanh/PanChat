package simulation.view.order;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.HashMap;

import simulation.arrows.SingleArrow;
import simulation.model.SimulationModel;
import simulation.view.CellPosition;

@SuppressWarnings("serial")
public class FifoOrderView implements Serializable, OrderI {

	public static boolean debug = false;

	private final static boolean DEBUG = false;

	private HashMap<CellPosition, VectorClock> clockTable;

	// indica que el ultimo tick en el que hay un vector
	private int lastTick;

	private SimulationModel simulationModel;

	// indica si estamos recalculando, para que no se produzca un ciclo
	boolean isRecalculating;

	// Vector <VectorClock> posClock;

	public FifoOrderView(SimulationModel simulationModel) {
		this.simulationModel = simulationModel;
		clockTable = new HashMap<CellPosition, VectorClock>();
		lastTick = 1;
		isRecalculating = false;
		// posClock = new Vector<VectorClock>();
	}

	@Override
	public boolean addLogicalOrder(SingleArrow arrow, boolean isMultiple) {
		/*
		 * Dada una flecha, se aniade sus correspondientes marcas de tiempo
		 * tanto en origen como en destino El valor devuelto indica que hay
		 * inconsistencia en las marcas de tiempo(true) o que no (false)
		 */
		boolean correctness = true;

		addLogicalOrder(arrow.getInitialPos(), arrow.getFinalPos(), true,
				isMultiple);
		correctness = addLogicalOrder(arrow.getInitialPos(), arrow
				.getFinalPos(), false, false);

		// VectorClock.print = true;
		debug("Correctness: " + correctness);

		return correctness;
	}

	public boolean addLogicalOrder(CellPosition origin, CellPosition position,
			boolean isOrigin, boolean isMultiple) {
		/*
		 * se encarga de aniadir en destino (position) un nuevo vector logico
		 * procedente de origin
		 */
		boolean correctness = true;
		VectorClock lastVector;
		VectorClock newVector;
		int size = this.simulationModel.getNumProcesses();
		newVector = new VectorClock(origin, position, isOrigin, isMultiple,
				size);

		// tenemos que encontrar el ultimo vector para ese proceso
		lastVector = locateVector(newVector);

		// si se ha encontrado un vector anterior, se copia e incrementa
		if (lastVector != null) {
			newVector.setVector(lastVector);
		} else {
			newVector.initialize();
		}

		/*
		 * se comprueba que el nuevo vector sea correcto si no es de origen,
		 * para ello tanto el vector de origen como de destino han de cumplir
		 * una serie de propiedades
		 */
		if (newVector.isOrigin == false)
			correctness = newVector.isCorrect(clockTable.get(origin));

		// si es correcto se introduce en la tabla
		if (correctness == true) {
			clockTable.put(newVector.drawingPos, newVector);
			/*
			 * si el origen o destino de esta flecha es anterior a la llegada de
			 * otros mensajes puede que al introducir esta flecha ser modifiquen
			 * los que llegan posteriormente Habra que recalcularlos esta
			 * comprobacion se realizara solo en el vector de destino
			 */
			if (newVector.isOrigin == false) {
				if (newVector.origin.tick < lastTick
						|| newVector.finalPos.tick < lastTick) {
					// solo se recalcula si no estamos recalculando ya
					recalculateVectors(newVector.origin.tick);
				} else
					lastTick = Math.max(newVector.origin.tick,
							newVector.finalPos.tick);
			}
		} else {
			// si la marca de tiempo no es correcta, se elimina el origen de la
			// misma
			clockTable.remove(newVector.origin);
		}
		return correctness;
	}

	public void recalculateVectors(int originalTick) {
		// solo se recalcula si no lo haciamos ya para evitar ciclos
		if (isRecalculating == false) {
			isRecalculating = true;
			/*
			 * se recalculan todos los relojes a partir del tickOrigen +1
			 */
			int originTick = originalTick + 1;
			CellPosition origin = new CellPosition(simulationModel
					.getNumProcesses(), 0);
			VectorClock actualVector;

			for (int i = originTick; i <= lastTick; i++) {
				origin.tick = i;
				for (int j = 0; j < simulationModel.getNumProcesses(); j++) {
					origin.process = j;
					/*
					 * si existe un vector con origen en el proceso j y tick i
					 * se eliminan y se pide que se calculen de nuevo
					 */
					actualVector = clockTable.get(origin);
					if (actualVector != null) {
						if (actualVector.isOrigin) {
							debug("Tamanio antes de anadir: "
									+ clockTable.size());

							addLogicalOrder(actualVector.origin,
									actualVector.finalPos, true,
									actualVector.isMultiple);

							debug("Tamanio despues de anadir: "
									+ clockTable.size());

							debug = true;

							addLogicalOrder(actualVector.origin,
									actualVector.finalPos, false, false);

							debug("Tamanio despues de la pos final de anadir: "
									+ clockTable.size());
						} else
							addLogicalOrder(actualVector.origin,
									actualVector.finalPos, false, false);
					}
				}
			}
			isRecalculating = false;
		}

	}

	public VectorClock locateVector(VectorClock newVector) {
		VectorClock vectorFound = null;
		CellPosition actualPosition;
		if (newVector.isMultiple)
			// si es multiple hay que contar el propio vector, para que no borre
			// los numeros ya obtenidos
			actualPosition = new CellPosition(newVector.drawingPos.process,
					newVector.drawingPos.tick);
		else
			actualPosition = new CellPosition(newVector.drawingPos.process,
					newVector.drawingPos.tick - 1);

		while (actualPosition.tick >= 0 && vectorFound == null) {
			if (clockTable.containsKey(actualPosition)) {
				vectorFound = clockTable.get(actualPosition);
				/*
				 * hay que comprobar que el encontrado sea de la misma
				 * naturaleza que el nuevo vector, es decir, que los dos sean de
				 * origen o no lo sea ninguno
				 */
				if (!(newVector.isOrigin == vectorFound.isOrigin))
					vectorFound = null;
			}
			actualPosition.tick--;
		}

		/*
		 * si no hemos encontrado el vector anterior es que esta es la primera
		 * que se recibe un mensaje, por lo que se devolvera null
		 */
		return vectorFound;
	}

	@Override
	public boolean moveLogicalOrder(SingleArrow arrow) {
		return false;
	}

	@Override
	public void removeLogicalOrder(CellPosition finalPos) {
		VectorClock removedVector;
		removedVector = clockTable.remove(finalPos);
		clockTable.remove(removedVector.origin);
		recalculateVectors(-1);
	}

	/**
	 * elimina solo el vector que se le pasas por parametro
	 * 
	 * @param finalPos
	 */
	@Override
	public void removeOnlyLogicalOrder(CellPosition finalPos) {
		debug("eliminado: " + finalPos);
		VectorClock origin;
		VectorClock removed;
		removed = clockTable.remove(finalPos);
		// hay que disminuir en 1 la posicion correspondiente en el origne
		// ESTRICTAMENTE NECESARIO
		origin = clockTable.get(removed.origin);
		// decrementamos el que quitamos y las otra flechas que habia
		origin.decrease(removed.finalPos.process);
		origin.decrease(origin.finalPos.process);
		clockTable.put(removed.origin, origin);

		recalculateVectors(-1);
		debug("tamanio de la tabla de relojes: " + clockTable.size());
	}

	private void debug(String out) {
		if (DEBUG)
			System.out.println(out);
	}

	@Override
	public void draw(Graphics2D g2) {
		for (VectorClock vector : this.clockTable.values()) {
			vector.draw(g2);
		}

	}
}
