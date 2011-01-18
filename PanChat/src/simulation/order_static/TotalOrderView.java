package simulation.order_static;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;

public class TotalOrderView implements OrderI, Serializable {

	private static final long serialVersionUID = 1L;

	public OrderDrawing drawingServer;

	public static boolean debug = false;

	private final static boolean DEBUG = false;

	private HashMap<CellPosition, VectorI> clockTable;
	private Vector<Vector<VectorI>> messages;

	private SimulationModel simulationModel;

	boolean numProcessChanged;
	int size;
	int ticks;

	// se guardan las posiciones de una flecha no correcta a fin de dar una
	// explicacion
	// grafica de por que no se puede realizar una flecha
	private CellPosition arrowOrigin;
	private Vector<Interval> availableCell;

	public TotalOrderView(SimulationModel simulationModel) {
		this.simulationModel = simulationModel;
		drawingServer = simulationModel.drawingServer;
		drawingServer.setTotalOrder(this);
		clockTable = new HashMap<CellPosition, VectorI>();
		size = this.simulationModel.getNumProcesses();
		ticks = this.simulationModel.getTimeTicks();

		// hay que crear los vectore
		messages = new Vector<Vector<VectorI>>();
		for (int i = 0; i < size; i++) {
			messages.add(i, new Vector<VectorI>());
		}
	}

	@Override
	public boolean addLogicalOrder(SingleArrow arrow) {
		/*
		 * si se habia indicado marcas de explicacion de por que no se podian
		 * dibujar las flechas, se borran
		 */
		drawingServer.setLastArrow(arrow);
		arrowOrigin = null;
		availableCell = null;
		drawingServer.unsetTotalMiss();
		CellPosition finalPos = arrow.getFinalPos();
		CellPosition initPos = arrow.getInitialPos();

		arrowOrigin = initPos.clone();

		boolean correctness = true;

		correctness = addLogicalOrder(initPos, finalPos);

		arrowOrigin = initPos.clone();
		if (correctness == false) {
			drawingServer.setTotalMiss();
			getHelp(arrowOrigin, finalPos);
		} else {
			if (drawingServer.isSomeoneWrong())
				getHelp(arrowOrigin, finalPos);
		}

		return correctness;
	}

	private boolean addLogicalOrder(CellPosition origin, CellPosition position) {
		boolean correctness = true;
		// la correcion solo debe llevarse a cabo si es un mensajes de envio
		// multiple

		// si ya estaba insertado hay que coger el id del mensaje ya insertado
		TotalMessage or = (TotalMessage) clockTable.get(origin);
		TotalMessage msg;
		TotalMessage cpy;
		if (or == null) {
			if (!drawingServer.isSomeoneWrong())
				TotalMessage.incrNumMsg();

			msg = new TotalMessage(origin, position, true);
			cpy = new TotalMessage(origin, position, false);
			cpy.id = msg.id;
			clockTable.put(origin, msg);
		} else {
			correctness = checkCorrectness(origin, position, or.id);
			print("correctness: " + correctness);
			cpy = new TotalMessage(origin, position, false);
			cpy.id = or.id;

			// se aniade a la lista del origen, si ya se encontraba en la lista,
			// debe quitar el valor
			// anterior
			or.finalPos.remove(position);
			or.finalPos.add(position);
			clockTable.put(origin, or);

		}

		clockTable.put(position, cpy);
		messages.get(position.process).add(cpy);

		return correctness;
	}

	private boolean checkCorrectness(CellPosition origin,
			CellPosition position, int id) {
		/*
		 * para comprobar la correccion se realiza la resta del tick position
		 * con todos los demas mensajes entregados por el proceso. El signo de
		 * esta resta tendr� que corresponder con su respectivo mensaje en el
		 * resto de procesos
		 */
		boolean correctness = true;
		TotalMessage orVector = (TotalMessage) clockTable.get(origin);

		HashMap<Integer, Integer> diff;
		HashMap<Integer, Integer> tmp;
		Set<Entry<Integer, Integer>> values;
		Entry<Integer, Integer> entry;
		Iterator<Entry<Integer, Integer>> it;
		TotalMessage total = null;
		int difId;
		int difValue;

		diff = getDiference(position.process, position.tick);

		/*
		 * por cada diferencia con cada uno de mis procesos se comprueba si la
		 * restacoincide con los correspondientes mensajes en aquellos procesos
		 * que hayanrecibido el mensaje del cual se comprueba la correccion
		 */

		int i;
		// para todos los procesos
		wrong: for (CellPosition list : orVector.finalPos) {
			i = list.process;
			if (i != position.process) {
				// se busca el mensaje del que se busca correccion que
				// OBLIGATORIAMENTE
				// tiene que estar en el proceso
				for (VectorI vector : messages.get(i)) {
					total = (TotalMessage) vector;
					if (total.id == id)
						break;
				}
				// cuando se encuentra se calculan las diferencias con todos los
				// demas
				// mensajes
				tmp = getDiference(i, total.finalPos.firstElement().tick);

				// para los mensajes comunes, debe haber acuerdo en el orden de
				// entrega
				values = diff.entrySet();
				it = values.iterator();

				while (it.hasNext()) {
					entry = it.next();
					difId = entry.getKey();
					difValue = entry.getValue();
					if (tmp.containsKey(difId)) {
						int value = tmp.get(difId);
						if ((difValue > 0 && value < 0)
								|| (difValue < 0 && value > 0)) {
							correctness = false;
							break wrong;
						}
					}
				}

			}
		}

		return correctness;
	}

	public HashMap<Integer, Integer> getDiference(int i, int tick) {
		// se hace la diferencia de tick - x, donde x son los ticks de destino
		// de todos los mensajes del vector i
		HashMap<Integer, Integer> diff = new HashMap<Integer, Integer>();
		TotalMessage total = null;

		int diference;
		for (VectorI mess : messages.get(i)) {
			total = (TotalMessage) mess;
			diference = tick - total.drawingPos.tick;
			diff.put(total.id, diference);
		}
		return diff;
	}

	@Override
	public void removeFinalOrder(CellPosition finalPos) {
		// si falla aqui es pos que no se han tenido en cuenta los decrease
		TotalMessage removedVector;
		TotalMessage origin;
		removedVector = (TotalMessage) clockTable.remove(finalPos);
		messages.get(finalPos.process).remove(removedVector);
		if (removedVector != null) {
			origin = (TotalMessage) clockTable.get(removedVector.origin);
			if (!(origin.isMultiple()))
				clockTable.remove(removedVector.origin);
			else {
				// quitamos la posicion final del vector multiple
				origin.finalPos.remove(finalPos);
				clockTable.put(removedVector.origin, origin);
			}
		}
	}

	/**
	 * elimina solo el vector que se le pasas por parametro
	 * 
	 * @param finalPos
	 */
	@Override
	public void removeInitialOrder(CellPosition initPos) {
		debug("eliminado: " + initPos);
		TotalMessage removed;
		removed = (TotalMessage) clockTable.remove(initPos);
		// hay que disminuir en 1 la posicion correspondiente en el origne
		// ESTRICTAMENTE NECESARIO

		if (removed != null) {
			for (CellPosition finalPos : removed.finalPos) {
				clockTable.remove(finalPos);
				messages.get(finalPos.process).remove(removed);
			}
			debug("tamanio de la tabla de relojes: " + clockTable.size());
		}
	}

	public void getHelp(CellPosition origin, CellPosition finalPos) {
		TotalMessage orVector = (TotalMessage) clockTable.get(origin);
		// indican el orden del mensaje a insertar con respecto a los demas
		// mensajes
		// que entregan los procesos que tambien entregan el mensaje
		Vector<Integer> before = new Vector<Integer>();
		Vector<Integer> after = new Vector<Integer>();
		Vector<VectorI> vecProc;

		TotalMessage vec = null;
		TotalMessage actual;
		int tickFinal;
		CellPosition actualFinal;
		// vector para indicar que procesos no deben ser comprobados
		boolean visited[] = new boolean[this.size];
		Interval deliver;
		// variable global que tiene el vector de intervalos posibles
		availableCell = new Vector<Interval>();

		// inicializamos
		for (int i = 0; i < this.size; i++)
			visited[i] = false;
		// se pone a visitado el proceso origen de la flecha
		visited[origin.process] = true;

		for (CellPosition vector : orVector.finalPos) {
			// encontramos el mensaje en el vector
			if (vector.process != finalPos.process) {
				vecProc = messages.get(vector.process);
				visited[vector.process] = true;

				for (VectorI msg : vecProc) {
					vec = (TotalMessage) msg;
					if (vec.id == orVector.id)
						break;
				}

				// todos los mensajes del vector que se entreguen antes se
				// introducen
				// en before, los que se entreguen despues en after
				tickFinal = vec.finalPos.firstElement().tick;
				for (VectorI msg : vecProc) {
					actual = (TotalMessage) msg;
					actualFinal = actual.finalPos.firstElement();
					if (tickFinal > actualFinal.tick) {
						before.add(actual.id);
					} else if (tickFinal < actualFinal.tick) {
						after.add(actual.id);
					}
				}
			}
		}

		// para los vectores que no han entregado el mensaje, comprobamos cuando
		// es posible entregarlo
		for (int proc = 0; proc < this.size; proc++) {
			if (visited[proc] == false) {
				CellPosition pos = new CellPosition(size, ticks);
				pos.process = proc;
				deliver = new Interval(pos, pos.clone());
				deliver.start.tick = origin.tick + 1;
				deliver.end.tick = ticks - 1;

				for (VectorI procVector : messages.get(proc)) {
					actual = (TotalMessage) procVector;
					actualFinal = actual.finalPos.firstElement();
					if (before.contains(actual.id))
						deliver.start.tick = Math.max(deliver.start.tick,
								actualFinal.tick + 1);
					else if (after.contains(actual.id))
						deliver.end.tick = Math.min(deliver.end.tick,
								actualFinal.tick - 1);
				}
				availableCell.add(deliver);
			}
		}

	}

	public void setNumProcessChanged() {
		numProcessChanged = true;
	}

	private void debug(String out) {
		if (DEBUG)
			System.out.println(out);
	}

	@Override
	public void draw(Graphics2D g2) {
		drawingServer.draw(g2, clockTable, availableCell, arrowOrigin);

	}

	public Vector<Interval> getAvailableCell() {
		return availableCell;
	}

	public void print(String s) {
		System.out.println(s);
	}

	@Override
	public void recalculateVectors(int originalTick) {
	}

}
