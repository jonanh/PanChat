package simulation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import panchat.data.User;

import simulation.arrows.MessageArrow;
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
	 * Numero de casillas del tablero
	 */
	public static final int DEFAULT_NUM_PROCESSES = 4;
	public static final int DEFAULT_NUM_TICKS = 14;

	/*
	 * Atributos
	 */
	private int numTicks = DEFAULT_NUM_TICKS;

	// Lista de cortes
	private BitSet cutList = new BitSet();

	// Lista de flechas
	private ArrayList<MessageArrow> listaFlechas = new ArrayList<MessageArrow>();
	private HashMap<CellPosition, MessageArrow> arrowHastTable = new HashMap<CellPosition, MessageArrow>();

	// Lista de procesos/usuarios
	private ArrayList<User> listaProcesos = new ArrayList<User>();

	private Object mutex = new Object();

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

		CellPosition arrowCellPosition;

		for (MessageArrow arrow : listaFlechas) {
			arrowCellPosition = arrow.getFinalPos();
			if (last.tick < arrowCellPosition.tick)
				last.tick = arrowCellPosition.tick;

			if (last.process < arrowCellPosition.process)
				last.process = arrowCellPosition.process;

			arrowCellPosition = arrow.getInitialPos();
			if (last.process < arrowCellPosition.process)
				last.process = arrowCellPosition.process;
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
			for (int i = 0; i < numProcesses; i++) {
				listaProcesos.add(new User(null));
			}
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

			for (int i = getNumProcesses(); i > pNumProcesses; i++)
				listaProcesos.remove(i);

			this.hasChanged();
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

			this.hasChanged();
			this.notifyObservers();

		} else if (pTimeTicks < numTicks) {
			this.cutList.clear(pTimeTicks + 1, numTicks);
			/*
			 * Para no borrar ningun proceso que tenga una flecha buscamos el
			 * último tick con una flecha y comprobamos que no estamos
			 * intentando eliminarlo.
			 */
			this.numTicks = Math.max(pTimeTicks, lastArrow().tick);
			this.hasChanged();
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
	public List<? extends MessageArrow> getArrowList() {
		synchronized (mutex) {
			return listaFlechas;
		}
	}

	/**
	 * 
	 * @param messageArrow
	 *            Añadimos esta fecla
	 */
	public void addArrow(MessageArrow messageArrow) {
		synchronized (mutex) {
			listaFlechas.add(messageArrow);
			arrowHastTable.put(messageArrow.getInitialPos(), messageArrow);
			arrowHastTable.put(messageArrow.getFinalPos(), messageArrow);
		}
	}

	/**
	 * 
	 * @param position
	 *            Borramos una flecha de esta posicion.
	 */
	public MessageArrow getArrow(Position position) {
		synchronized (mutex) {

			if (!(position instanceof CellPosition))
				return null;

			return arrowHastTable.get((CellPosition) position);
		}
	}

	/**
	 * 
	 * @param position
	 *            Borramos una flecha de esta posicion.
	 */
	public MessageArrow deleteArrow(CellPosition position) {
		synchronized (mutex) {
			MessageArrow arrow = arrowHastTable.remove(position);
			if (arrow != null) {
				// Borramos de la tabla hash la flecha referenciada desde el
				// otro extemo (inicio o final)
				if (!arrow.getInitialPos().equals(position))
					arrowHastTable.remove(arrow.getFinalPos());
				else
					arrowHastTable.remove(arrow.getFinalPos());

				listaFlechas.remove(arrow);
			}
			return arrow;
		}
	}

	/**
	 * 
	 * @param messageArrow
	 * 
	 * @return Verificamos si messageArrow es una flecha que se encuentra en un
	 *         lugar válido y/o libre.
	 */
	public boolean isValidArrow(MessageArrow messageArrow) {
		synchronized (mutex) {
			CellPosition initialPos = messageArrow.getInitialPos();
			CellPosition finalPos = messageArrow.getFinalPos();

			// Una flecha no puede ir de a el mismo proceso
			if (initialPos.process == finalPos.process)
				return false;

			// Una flecha no puede ir hacia atrás
			if (initialPos.tick >= finalPos.tick)
				return false;

			// Si el destino de la fecha apunta a una celda ya ocupada
			if (arrowHastTable.containsKey(messageArrow.getFinalPos()))
				return false;

			return true;
		}
	}
}
