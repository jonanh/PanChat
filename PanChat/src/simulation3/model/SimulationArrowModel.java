package simulation3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import panchat.data.User;

import simulation3.arrows.MultipleArrow;
import simulation3.view.CellPosition;
import simulation3.view.Position;

/**
 * Clase que representa los datos del simulador :
 * 
 * - Lista de flechas
 * 
 * -
 */
@SuppressWarnings("serial")
public class SimulationArrowModel extends Observable implements Serializable {

	/*
	 * Constantes por defecto
	 */
	public static final int DEFAULT_NUM_PROCESSES = 4;
	public static final int DEFAULT_NUM_TICKS = 14;

	public static final boolean ADD_DEBUG = false;
	public static final boolean REMOVE_DEBUG = false;

	/*
	 * Atributos
	 */
	private int numTicks = DEFAULT_NUM_TICKS;

	// Lista de flechas
	private ArrayList<MultipleArrow> arrowList = new ArrayList<MultipleArrow>();

	// Matriz de flechas, donde CellPosition almacena (Proceso,Tick)
	private HashMap<CellPosition, MultipleArrow> arrowMatrix = new HashMap<CellPosition, MultipleArrow>();

	// Lista de procesos/usuarios
	private List<User> processList = new ArrayList<User>();

	/**
	 * Construimos el objeto de datos de simulacion
	 */
	public SimulationArrowModel() {
		setNumProcesses(DEFAULT_NUM_PROCESSES);
	}

	/*
	 * Métodos para obtener y establecer el número procesos y ticks.
	 */

	/**
	 * @return Obtenemos el número de procesos
	 */
	public int getNumProcesses() {
		return processList.size();
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
				processList.add(new User(String.valueOf(i)));

			this.setChanged();
			this.notifyObservers();

		} // Si hay que eliminar nuevos procesos
		else if (numProcesses < 0) {

			/*
			 * Para no borrar ningun proceso que tenga una flecha buscamos el
			 * último proceso con una flecha y comprobamos que no estamos
			 * intentando eliminarlo.
			 */
			pNumProcesses = Math.max(pNumProcesses, lastArrow().process + 1);

			for (int i = getNumProcesses(); i > pNumProcesses; i--) {
				arrowMatrix.remove(i);
				processList.remove(i - 1);
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
			/*
			 * Para no borrar ningun proceso que tenga una flecha buscamos el
			 * último tick con una flecha y comprobamos que no estamos
			 * intentando eliminarlo.
			 */
			this.numTicks = Math.max(pTimeTicks, lastArrow().tick + 1);
			super.setChanged();
			this.notifyObservers();
		}
		return numTicks;
	}

	/*
	 * Métodos para añadir, obtener y eliminar flechas
	 */

	/**
	 * @return El listado de flechas
	 */
	public synchronized List<MultipleArrow> getArrowList() {
		return arrowList;
	}

	/**
	 * 
	 * @param messageArrow
	 *            Añadimos esta fecla
	 */
	public synchronized void addArrow(MultipleArrow messageArrow) {

		for (CellPosition pos : messageArrow.getPositions())
			arrowMatrix.put(pos, messageArrow);

		arrowList.add(messageArrow);

		super.setChanged();
		this.notifyObservers();
	}

	public synchronized MultipleArrow getArrow(Position position) {

		MultipleArrow arrow = arrowMatrix.get(position);

		return arrow;
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
	public synchronized MultipleArrow deleteArrow(CellPosition position) {

		MultipleArrow arrow = arrowMatrix.remove(position);

		if (arrow != null) {
			for (CellPosition pos : arrow.getPositions())
				arrowMatrix.remove(pos);

			arrowList.remove(arrow);
		}

		super.setChanged();
		this.notifyObservers();
		return arrow;
	}

	/*
	 * Rutinas ayudantes
	 */

	/**
	 * Rutina ayudante para setNumProcesses y setTimeTicks. Busca en las
	 * flechas, el proceso más lejano desde el cual salga o llegue una flecha, y
	 * el tick más lejano hasta donde llegue una flecha.
	 * 
	 * @return
	 */
	private CellPosition lastArrow() {

		CellPosition last = new CellPosition(0, 0);

		for (MultipleArrow arrow : arrowList) {
			for (CellPosition arrowCellPosition : arrow.getPositions()) {

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
	 * Rutinas ayudantes
	 */

	/**
	 * @param process
	 * 
	 * @return Devuelve el usuario correspondiente a un proceso determinado.
	 */
	public User getUser(int process) {
		return processList.get(process);
	}

	/**
	 * 
	 * @return Devuelve la lista de usuarios
	 */
	public List<User> getUserList() {
		return processList;
	}

	/**
	 * Busca una celda vacia a partir de la posición cell.
	 * 
	 * Si no existe una posicion vacia en ese proceso, aumentamos el número de
	 * ticks.
	 * 
	 * @param cell
	 * 
	 * @return Devuelve una celda vacia a partir de la posicion cell.
	 */
	public CellPosition freeCell(CellPosition cell) {

		CellPosition freeCell = cell.clone();

		freeCell.tick++;

		// Recorremos los ticks desde la posicion donde estamos hasta el final
		for (; freeCell.tick < this.getTimeTicks(); freeCell.tick++)
			if (!arrowMatrix.containsKey(freeCell))
				return freeCell;

		// Si no hemos encontrado una, tenemos que aumentar el tamaño.
		setTimeTicks(getTimeTicks() + 1);

		return freeCell;
	}
}
