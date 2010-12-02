package simulation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import panchat.data.User;

import simulation.arrows.MessageArrow;
import simulation.arrows.MultipleArrow;
import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;
import simulation.view.Position;

/**
 * Clase que representa los datos del simulador :
 * 
 * - Lista de flechas
 * 
 * -
 */
@SuppressWarnings("serial")
public class SimulationModel extends Observable implements Serializable {

	/*
	 * Constantes por defecto
	 */
	public static final int DEFAULT_NUM_PROCESSES = 4;
	public static final int DEFAULT_NUM_TICKS = 14;

	/*
	 * Atributos
	 */
	private int numTicks = DEFAULT_NUM_TICKS;

	// Lista de cortes (usando un bitset, para evitar usar un array de
	// booleanos. Es dinamico e internamente permite usar operaciones a nivel de
	// bit).
	private BitSet cutList = new BitSet();

	// Flechas
	// Lista de flechas
	private ArrayList<MultipleArrow> listaFlechas = new ArrayList<MultipleArrow>();

	// Matriz de flechas, donde CellPosition almacena (Proceso,Tick)
	private HashMap<CellPosition, MultipleArrow> arrowMatrix = new HashMap<CellPosition, MultipleArrow>();

	// Lista de procesos/usuarios
	private ArrayList<User> listaProcesos = new ArrayList<User>();

	/**
	 * Construimos el objeto de datos de simulacion
	 */
	public SimulationModel() {
		setNumProcesses(DEFAULT_NUM_PROCESSES);
	}

	/**
	 * Rutina ayudante para setNumProcesses y setTimeTicks. Busca en las
	 * flechas, el proceso más lejano desde el cual salga o llegue una flecha, y
	 * el tick más lejano hasta donde llegue una flecha.
	 * 
	 * @return
	 */
	private CellPosition lastArrow() {

		CellPosition last = new CellPosition(0, 0);

		for (MultipleArrow arrow : listaFlechas) {
			for (CellPosition arrowCellPosition : arrow.getFinalPos()) {

				if (last.tick < arrowCellPosition.tick)
					last.tick = arrowCellPosition.tick;

				if (last.process < arrowCellPosition.process)
					last.process = arrowCellPosition.process;

				arrowCellPosition = arrow.getInitialPos();
				if (last.process < arrowCellPosition.process)
					last.process = arrowCellPosition.process;
			}
		}

		return last;
	}

	/*
	 * Número de procesos
	 */

	/**
	 * @return Obtenemos el número de procesos
	 */
	public int getNumProcesses() {
		return listaProcesos.size();
	}

	/**
	 * 
	 * @param pNumProcesses
	 * 
	 * @return Establecemos un nuevo número de procesos
	 */
	public int setNumProcesses(int pNumProcesses) {

		int numProcesses = pNumProcesses - getNumProcesses();

		// Si hay que añadir nuevos procesos :
		if (numProcesses > 0) {
			for (int i = getNumProcesses(); i < pNumProcesses; i++)
				listaProcesos.add(new User(null));

			this.hasChanged();
			this.notifyObservers();

		} // Si hay que eliminar nuevos procesos
		else if (numProcesses < 0) {

			/*
			 * Para no borrar ningun proceso que tenga una flecha buscamos el
			 * último proceso con una flecha y comprobamos que no estamos
			 * intentando eliminarlo.
			 */
			pNumProcesses = Math.max(pNumProcesses, lastArrow().process);

			for (int i = getNumProcesses(); i > pNumProcesses; i--) {
				arrowMatrix.remove(i);
				listaProcesos.remove(i);
			}
			super.setChanged();
			this.notifyObservers();
		}
		return getNumProcesses();
	}

	/**
	 * Obtener el número de ticks del canvas
	 * 
	 * @return
	 */
	public int getTimeTicks() {
		return this.numTicks;
	}

	/**
	 * Establecer el número de ticks
	 * 
	 * @param pTimeTicks
	 * @return
	 */
	public int setTimeTicks(int pTimeTicks) {

		if (pTimeTicks > numTicks) {
			this.numTicks = pTimeTicks;

			super.setChanged();
			this.notifyObservers();

		} else if (pTimeTicks < numTicks) {
			this.cutList.clear(pTimeTicks + 1, numTicks);
			/*
			 * Para no borrar ningun proceso que tenga una flecha buscamos el
			 * último tick con una flecha y comprobamos que no estamos
			 * intentando eliminarlo.
			 */
			this.numTicks = Math.max(pTimeTicks, lastArrow().tick);
			super.setChanged();
			this.notifyObservers();
		}
		return numTicks;
	}

	/**
	 * 
	 * @param tick
	 * 
	 * @return Si el tick es un corte o no.
	 */
	public boolean isCut(int tick) {
		return cutList.get(tick);
	}

	/**
	 * @return El listado de flechas
	 */
	public synchronized List<MultipleArrow> getArrowList() {
		return listaFlechas;
	}

	/**
	 * 
	 * @param messageArrow
	 *            Añadimos esta fecla
	 */
	public synchronized void addArrow(SingleArrow messageArrow) {
		CellPosition initialPos = messageArrow.getInitialPos();
		CellPosition finalPos = messageArrow.getFinalPos();

		MultipleArrow arrow = getMultipleArrow(initialPos);

		// Si no existe el MultipleArrow, lo creamos y añadimos la flecha
		if (arrow == null) {
			arrow = new MultipleArrow(initialPos, messageArrow);
			arrowMatrix.put(initialPos, arrow);
			listaFlechas.add(arrow);
		} // Añadimos la flecha
		else {
			CellPosition removeArrow = arrow.addArrow(messageArrow);
			// Si al añadir eliminamos una flecha que va al mismo proceso
			if (removeArrow != null)
				arrowMatrix.remove(removeArrow);
		}
		arrowMatrix.put(finalPos, arrow);
		super.setChanged();
		this.notifyObservers();
	}

	public MultipleArrow getMultipleArrow(CellPosition position) {
		return this.arrowMatrix.get(position);
	}

	public synchronized MessageArrow getArrow(Position position) {

		if (!(position instanceof CellPosition))
			return null;

		CellPosition pos = (CellPosition) position;

		MultipleArrow arrow = getMultipleArrow(pos);

		// Si no existe la flecha devolvemos null
		if (arrow == null)
			return null;

		// Si la posicion era la flecha inicial devolvemos todo el
		// MultipleArrow
		if (arrow.getInitialPos().equals(pos))
			return arrow;

		// Sino devolvemos la SingleArrow del destino
		else
			return arrow.getArrow(pos);
	}

	/**
	 * 
	 * Borramos una flecha de la posicion position. Si la posición es el origen
	 * de la flecha multiple se borra toda la flecha, en cambio si la posición
	 * es el destino de alguna de las flechas, se borra de la flecha multiple
	 * esa flecha.
	 * 
	 * @param position
	 */
	public synchronized MessageArrow deleteArrow(CellPosition position) {
		MessageArrow arrow;

		MultipleArrow multipleArrow = arrowMatrix.remove(position);

		if (multipleArrow == null)
			return null;

		// Si la posicion es la posicion inicial debemos borrar además
		// las referencias desde los nodos finales.
		if (multipleArrow.getInitialPos().equals(position)) {
			for (CellPosition pos : multipleArrow.getFinalPos())
				arrowMatrix.remove(pos);

			arrow = multipleArrow;

			listaFlechas.remove(multipleArrow);
		} // Si la posicion es la posicion de destino de una flecha entonces
		// eliminamos dicha flecha de la MultipleArrow
		else {
			arrow = multipleArrow.deleteArrow(position);

			// Si hemos borrado la ultima flecha del grupo, borrar también
			// el MultiArrow
			if (multipleArrow.getFinalPos().size() == 0) {
				arrowMatrix.remove(multipleArrow.getInitialPos());
				listaFlechas.remove(multipleArrow);
			}
		}
		super.setChanged();
		this.notifyObservers();
		return arrow;
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
	public synchronized boolean isValidArrow(SingleArrow messageArrow) {

		CellPosition initialPos = messageArrow.getInitialPos();
		CellPosition finalPos = messageArrow.getFinalPos();

		// Una flecha no puede ir de a el mismo proceso
		if (initialPos.process == finalPos.process)
			return false;

		// Una flecha no puede ir hacia atrás
		if (initialPos.tick >= finalPos.tick)
			return false;

		// Si el destino de la fecha apunta a una celda ya ocupada
		if (getMultipleArrow(finalPos) != null)
			return false;

		return true;

	}
}
