package simulation.order_static;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;

@SuppressWarnings("serial")
public class CausalOrderView implements Serializable, OrderI {
	public OrderDrawing drawingServer;

	public boolean doPrint = true;
	public static boolean debug = false;

	private final static boolean DEBUG = false;

	private HashMap<CellPosition, VectorI> clockTable;

	// indica que el ultimo tick en el que hay un vector
	private int lastTick;

	private SimulationModel simulationModel;

	// indica si estamos recalculando, para que no se produzca un ciclo
	boolean isRecalculating;

	boolean numProcessChanged;

	// se guardan las posiciones de una flecha no correcta a fin de dar una
	// explicacion
	// grafica de por que no se puede realizar una flecha
	private CellPosition arrowOrigin;
	private CellPosition noCorrectOrigin;
	private CellPosition noCorrectFinal;

	private CellPosition conflictFinal;
	private CellPosition conflictInit;
	private Vector<Interval> availableCell;
	// Vector <VectorClock> posClock;

	// indica si la deteccion de la violacion de orden causal se ha detecta de
	// manera
	// especial ( de delante hace detras )
	private boolean specialDetection = false;
	CausalVectorClock cast;

	public CausalOrderView(SimulationModel simulationModel) {
		this.simulationModel = simulationModel;
		drawingServer = simulationModel.drawingServer;
		drawingServer.setCausalOrder(this);
		clockTable = new HashMap<CellPosition, VectorI>();
		lastTick = 1;
		isRecalculating = false;
		// posClock = new Vector<CausalVectorClock>();
	}

	@Override
	public boolean addLogicalOrder(SingleArrow arrow) {
		/*
		 * Dada una flecha, se aniade sus correspondientes marcas de tiempo
		 * tanto en origen como en destino El valor devuelto indica que hay
		 * inconsistencia en las marcas de tiempo(true) o que no (false)
		 */
		// copia por si alguna falla, causal o total
		drawingServer.setLastArrow(arrow);
		boolean correctness = true;
		specialDetection = false;
		availableCell = null;
		drawingServer.unsetCausalMiss();
		// se indica que ya no se visualice la ayuda
		removeHelp();
		CellPosition origin = arrow.getInitialPos();
		CellPosition finalPos = arrow.getFinalPos();

		addLogicalOrder(origin, finalPos, true);
		addLogicalOrder(origin, finalPos, false);

		recalculate(origin, finalPos);
		correctness = isCorrectArrow(origin, finalPos);
		// CausalVectorClock.print = true;
		// si los vectores no son correctos, se eliminan
		if (correctness == true) {
			/*
			 * puede que al aniadir la flecha esta no vulnere el orden en el
			 * momento de suentrada, pero puede hacer que flechas que se
			 * enviaron antes que la suya y lleguendespues en el mismo proceso
			 * queden mal colocadas
			 */
			correctness = restoreOrder(origin, finalPos);
			if (correctness == false) {
				specialDetection = true;
			} else {
				if (drawingServer.isSomeoneWrong())
					getHelp(origin, finalPos);
			}
		}
		if (correctness == false) {
			// se guardan los valores necesarios para la ayuda
			drawingServer.setCausalMiss();
			getHelp(origin, finalPos);
			removeFinalOrder(finalPos);

		}
		return correctness;
	}

	private boolean isCorrectArrow(CellPosition origin, CellPosition finalPos) {
		boolean correctness = true;
		CausalVectorClock originVector = (CausalVectorClock) clockTable
				.get(origin);
		CausalVectorClock finalVector = (CausalVectorClock) clockTable
				.get(finalPos);
		CausalVectorClock lastVector = locateVector(finalVector);

		if (lastVector != null) {
			correctness = lastVector.isCorrect(originVector);
		}

		if (correctness == false) {
			conflictFinal = lastVector.finalPos.firstElement().clone();
			CausalVectorClock temp = (CausalVectorClock) clockTable
					.get(conflictFinal);
			conflictInit = temp.origin;
		}
		return correctness;
	}

	private boolean restoreOrder(CellPosition origin, CellPosition finalPos) {
		/*
		 * tenemos que buscar hacia delante
		 */
		CausalVectorClock finalClock = (CausalVectorClock) clockTable
				.get(finalPos);
		CausalVectorClock originClock;
		boolean correctness = true;
		Vector<Vector<CellPosition>> pendingPositions = new Vector<Vector<CellPosition>>();

		CellPosition it = finalPos.clone();
		it.tick++;
		while (it.tick <= lastTick && correctness == true) {
			originClock = (CausalVectorClock) clockTable.get(it);
			if (originClock != null) {
				if (originClock.isOrigin) {
					// hay transitividad
					pendingPositions.add(originClock.finalPos);
				} else {
					originClock = (CausalVectorClock) clockTable
							.get(originClock.origin);
					correctness = finalClock.isCorrect(originClock);
				}
			}
			it.tick++;
		}
		// si la flecha es correcta y hay transitividades se comprueban todas
		if (correctness == true && !pendingPositions.isEmpty()) {
			exit: for (Vector<CellPosition> vector : pendingPositions) {
				for (CellPosition end : vector) {
					cast = (CausalVectorClock) clockTable.get(end);
					correctness = restoreOrder(cast.origin, end);
					if (correctness == false) {
						break exit;
					}
				}
			}
			// cuando se ejecute la sentencia de break se continua desde aqui
		} else {
			it.tick--;
			CausalVectorClock v = (CausalVectorClock) clockTable.get(it);
			if (v != null)
				conflictFinal = v.finalPos.firstElement();
		}
		return correctness;
	}

	private void recalculate(CellPosition origin, CellPosition finalPos) {
		if (origin.tick < lastTick) {
			// solo se recalcula si no estamos recalculando ya
			lastTick = Math.max(lastTick, finalPos.tick);
			recalculateVectors(origin.tick);
		} else if (finalPos.tick < lastTick) {
			// solo se recalcula si no estamos recalculando ya
			recalculateVectors(finalPos.tick);
		} else
			lastTick = Math.max(origin.tick, finalPos.tick);
	}

	private boolean addLogicalOrder(CellPosition origin, CellPosition position,
			boolean isOrigin) {
		/*
		 * se encarga de aniadir en destino (position) un nuevo vector logico
		 * procedente de origin
		 */
		boolean correctness = true;
		CausalVectorClock lastVector;
		CausalVectorClock newVector;
		int size = this.simulationModel.getNumProcesses();
		newVector = new CausalVectorClock(origin, position, isOrigin, size);

		// tenemos que encontrar el ultimo vector para ese proceso
		lastVector = locateVector(newVector);

		// si se ha encontrado un vector anterior, se copia e incrementa
		if (lastVector != null) {
			newVector.vector = lastVector.vector.clone();
		} else {
			newVector.initialize();
		}
		// hay que incrementar el valor correspondiente
		if (newVector.isOrigin)
			newVector.incrPos(origin.process);
		else {
			/*
			 * si es destino, hay que calcular el maximo entre el que se tenia y
			 * elrecibido e incrementar en 1 el valor correspondiente a la
			 * posicion del proceso
			 */
			CellPosition or = newVector.origin;
			newVector.setVector((CausalVectorClock) clockTable.get(or));
			newVector.incrPos(newVector.finalPos.firstElement().process);
		}

		/*
		 * se comprueba que el nuevo vector sea correcto si no es de origen,
		 * para ello tanto el vector de origen como de destino han de cumplir
		 * una serie de propiedades
		 */

		aniadirVector(newVector);
		return correctness;
	}

	public void recalculateVectors(int originalTick) {
		int size = simulationModel.getNumProcesses();

		CellPosition origin = new CellPosition(size, 0);

		recalculateLoop(origin, originalTick);
		if (numProcessChanged == true)
			numProcessChanged = false;

	}

	// hace la pasada para insertar origenes o destinos. Si originOrFinal ==
	// true
	// en la pasada se calculan origenes, false se calculan destinos
	public void recalculateLoop(CellPosition origin, int originalTick) {
		CausalVectorClock actualVector;
		CausalVectorClock lastVector;
		for (int i = originalTick; i <= lastTick; i++) {
			origin.tick = i;
			for (int j = 0; j < simulationModel.getNumProcesses(); j++) {
				origin.process = j;
				actualVector = (CausalVectorClock) clockTable.get(origin);
				if (actualVector != null) {
					lastVector = locateVector(actualVector);
					if (lastVector == null) {
						actualVector.initialize();
					} else
						actualVector.vector = lastVector.vector.clone();

					// se actualizan los valores del vector
					if (actualVector.isOrigin) {
						// for(CellPosition finalPos: actualVector.finalPos)
						actualVector.incrPos(actualVector.origin.process);
					} else {
						/*
						 * si es destino, hay que calcular el maximo entre el
						 * que se tenia y elrecibido e incrementar en 1 el valor
						 * correspondiente a la posicion del proceso
						 */
						CellPosition or = actualVector.origin;
						actualVector.setVector((CausalVectorClock) clockTable
								.get(or));
						actualVector.incrPos(actualVector.finalPos
								.firstElement().process);
					}

					// si ha cambiado el numero de procesos, hay que cambiar el
					// tama�o de los arrays
					if (numProcessChanged == true) {
						actualVector.newDimension(simulationModel
								.getNumProcesses());
					}
					clockTable.put(actualVector.drawingPos, actualVector);
				}
			}
		}
	}

	public CausalVectorClock locateVector(CausalVectorClock newVector) {
		CausalVectorClock vectorFound = null;
		CellPosition actualPosition;

		actualPosition = new CellPosition(newVector.drawingPos.process,
				newVector.drawingPos.tick - 1);

		while (actualPosition.tick >= 0 && vectorFound == null) {
			if (clockTable.containsKey(actualPosition)) {
				vectorFound = (CausalVectorClock) clockTable
						.get(actualPosition);
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
	public void removeFinalOrder(CellPosition finalPos) {
		// si falla aqui es pos que no se han tenido en cuenta los decrease
		CausalVectorClock removedVector;
		CausalVectorClock origin;
		removedVector = (CausalVectorClock) clockTable.remove(finalPos);
		if (removedVector != null) {
			origin = (CausalVectorClock) clockTable.get(removedVector.origin);
			if (!(origin.isMultiple()))
				clockTable.remove(removedVector.origin);
			else {
				// quitamos la posicion final del vector multiple
				origin.finalPos.remove(finalPos);
				// origin.decrease(finalPos.process);
				clockTable.put(removedVector.origin, origin);
			}
			recalculateVectors(removedVector.origin.tick);
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
		CausalVectorClock removed;
		removed = (CausalVectorClock) clockTable.remove(initPos);

		if (removed != null) {
			for (CellPosition finalPos : removed.finalPos) {
				clockTable.remove(finalPos);
			}
			debug("tamanio de la tabla de relojes: " + clockTable.size());
		}
		recalculateVectors(initPos.tick);
	}

	private void aniadirVector(CausalVectorClock vector) {
		// si es posicional final se aniade
		if (!vector.isOrigin)
			clockTable.put(vector.drawingPos, vector);
		else {
			/*
			 * si es origen, el vector puede ser multiple. En tal caso, solo
			 * habra que aniadir la posicion final a la lista y actualizar el
			 * vector
			 */
			CausalVectorClock origin = (CausalVectorClock) clockTable
					.get(vector.origin);
			if (origin == null) {
				clockTable.put(vector.drawingPos, vector);
			} else {
				// si ya habia un vector que iba desde el proceso origen y mismo
				// tick
				// hasta el proceso final en otro tick, se elimina
				boolean found = false;
				int index = 0;
				for (CellPosition finalPos : origin.finalPos) {
					if (finalPos.process == vector.finalPos.firstElement().process) {
						found = true;
						break;
					}
					index++;
				}
				if (found == true) {
					clockTable.remove(origin.finalPos.elementAt(index));
					origin.finalPos.remove(index);
				}
				// si el vector no ha sido sustituido es que era un vector final
				// mas de un vector multiple
				// por lo tanto, hay que incrementar la posicion correspondiente
				// del vector inicial
				if (found == false)
					origin.incrPos(vector.finalPos.firstElement().process);

				origin.finalPos.add(vector.finalPos.firstElement());
				clockTable.put(origin.origin, origin);
			}
		}
	}

	private void removeHelp() {
		arrowOrigin = null;
		noCorrectOrigin = null;
		noCorrectFinal = null;
	}

	public void getHelp(CellPosition origin, CellPosition finalPos) {
		noCorrectFinal = finalPos.clone();
		arrowOrigin = origin.clone();
		availableCell = new Vector<Interval>();
		if (specialDetection == false) {
			normalHelp(origin, finalPos);
		} else
			specialHelp(origin, finalPos);

	}

	private void normalHelp(CellPosition origin, CellPosition finalPos) {
		int ticks = simulationModel.getTimeTicks();
		int processes = simulationModel.getNumProcesses();
		CausalVectorClock ant = null;
		CellPosition it = null;
		CausalVectorClock arrowOrigin = (CausalVectorClock) clockTable
				.get(origin);
		CausalVectorClock conflictVector = (CausalVectorClock) clockTable
				.get(conflictFinal);
		Vector<Integer> visitedProcesses = new Vector<Integer>();
		Vector<CellPosition> multipleLines = new Vector<CellPosition>();
		Vector<CellPosition> treatedMultiple = new Vector<CellPosition>();
		Vector<CellPosition> nextPosition = new Vector<CellPosition>();
		CausalVectorClock multiple;
		boolean havetodo = true;
		boolean last = false;

		CellPosition itOrigin = origin.clone();
		CellPosition incrProc;
		CausalVectorClock itVector = null;
		Vector<Integer> deadLine = new Vector<Integer>();

		// inicializamos el vector deadLine
		for (int i = 0; i < processes; i++)
			deadLine.add(ticks - 1);

		while (nextPosition.size() > 0 || havetodo) {
			havetodo = false;
			while (itVector == null && itOrigin.tick <= ticks) {
				itOrigin.tick++;
				itVector = (CausalVectorClock) clockTable.get(itOrigin);
				if (itVector != null && itVector.isOrigin) {
					int element;
					// al poner incrProc se va todo
					for (CellPosition multipleLine : itVector.finalPos) {
						nextPosition.add(multipleLine.clone());
						incrProc = multipleLine;
						element = deadLine.get(incrProc.process);
						deadLine.set(incrProc.process, Math.min(
								incrProc.tick - 1, element));
					}
					last = false;
				}
				itVector = null;
			}
			if (nextPosition.size() > 0) {
				itOrigin = nextPosition.firstElement();
				nextPosition.removeElementAt(0);
			}
			if (nextPosition.size() == 0 && last == false) {
				last = true;
				havetodo = true;
			}
		}

		// se parara cuando hayamos mirado todos los procesos hasta el nuestro
		while (conflictFinal.process != origin.process) {
			visitedProcesses.add(conflictFinal.process);
			it = conflictFinal.clone();
			it.tick--;
			while (it.tick > origin.tick) {
				ant = (CausalVectorClock) clockTable.get(it);
				if (ant != null) {
					if (ant.isCorrect(arrowOrigin) == false) {
						conflictFinal = it.clone();
						conflictVector = ant;
					}
				}
				it.tick--;
			}
			// se han encontrado posibles valores
			if (conflictFinal.tick > origin.tick) {
				CellPosition start = origin.clone();
				CellPosition end = conflictFinal.clone();
				end.tick--;
				start.tick++;
				start.process = conflictFinal.process;
				end.tick = Math.min(end.tick, deadLine.elementAt(end.process));
				availableCell.add(new Interval(start, end));

				// comprobamos si la linea es multiple ya que de ser asi hay que
				// aniadirlos
				// a revision si no ha sido tratado previamente
				multiple = (CausalVectorClock) clockTable.get(conflictFinal);
				multiple = (CausalVectorClock) clockTable.get(multiple.origin);
				if (multiple.isMultiple()
						&& !treatedMultiple.contains(multiple.origin)) {
					treatedMultiple.add(multiple.origin);
					for (CellPosition posMul : multiple.finalPos) {
						if (!posMul.equals(conflictFinal))
							multipleLines.add(posMul);
					}
				}
			}

			// se coge el inicio del vector en conflicto
			conflictFinal = conflictVector.origin;

			// si hay algun punto final por revisar y se ha acabado con los
			// anteriores, se pone somo siguiente
			if (conflictFinal.process == origin.process
					&& multipleLines.size() > 0) {
				conflictFinal = multipleLines.firstElement();
				multipleLines.removeElementAt(0);
			}
		}

		// introducimos en la lista aquellos procesos que no hayamos visitado
		int num = simulationModel.getNumProcesses();
		for (int i = 0; i < num; i++)
			if (i != origin.process
					&& !(visitedProcesses.contains(new Integer(i)))) {
				CellPosition start = new CellPosition(num, simulationModel
						.getTimeTicks());
				CellPosition end;

				start.process = i;
				end = start.clone();
				start.tick = origin.tick + 1;
				end.tick = deadLine.get(i);
				availableCell.add(new Interval(start, end));
			}

	}

	private void specialHelp(CellPosition origin, CellPosition finalPos) {
		int processes = simulationModel.getNumProcesses();
		int ticks = simulationModel.getTimeTicks();

		Vector<Integer> deadLine = new Vector<Integer>();
		CellPosition start = new CellPosition(processes, ticks);
		CellPosition end = new CellPosition(processes, ticks);
		CellPosition it = origin.clone();
		CausalVectorClock itVector;
		Vector<Boolean> visitedProcesses = new Vector<Boolean>();
		Vector<Boolean> globalVisited = new Vector<Boolean>();
		Vector<Integer> forcedStart = new Vector<Integer>();
		int numProcessesVisited = 0;
		int numGlobalVisited = 0;

		// como muy pronto todos podran empezar un tick despues del origen del
		// proceso
		for (int i = 0; i < processes; i++)
			forcedStart.add(origin.tick + 1);

		// primero habra que localizar los valores iniciales obligados debido a
		// las
		// transiones
		CausalVectorClock forcedVector;
		CausalVectorClock originVector;
		CausalVectorClock localize = null;
		forcedVector = (CausalVectorClock) clockTable.get(conflictFinal);
		originVector = (CausalVectorClock) clockTable.get(forcedVector.origin);

		/*
		 * la primera vez podemos no entrar por el hecho de que la flecha nueva
		 * incorrecta y la flechaen conflicto pertenezcan al mismo proceso
		 */
		boolean firstTime = true;

		while (forcedVector.origin.process != origin.process || firstTime) {
			firstTime = false;
			while (localize == null) {
				localize = locateVector(forcedVector);
				if (!localize.isCorrect(originVector) && !localize.isOrigin) {
					forcedVector = localize;
				}
			}

			forcedStart.set(localize.origin.process, localize.origin.tick + 1);
			forcedVector = (CausalVectorClock) clockTable.get(localize.origin);
			localize = null;
		}

		// inicializamos el vector deadLine con el numero maximo de ticks
		for (int i = 0; i < processes; i++)
			deadLine.add(new Integer(ticks));

		// inicializamos el vector global de visitados
		for (int i = 0; i < processes; i++)
			globalVisited.add(false);

		// aniadimos los elementos del vector de visitados local
		for (int i = 0; i < processes; i++)
			visitedProcesses.add(false);

		/*
		 * la primera posicion la marca la posicion en conflicto se recorreran
		 * la lista de vectores hasta haber visitado todos las transiciones
		 * posibles o haber agotado los procesos
		 */
		while (numGlobalVisited < processes) {
			it.tick++;
			// inicializamos el vector de visitados
			for (int i = 0; i < processes; i++)
				visitedProcesses.set(i, false);
			while (it.tick < ticks && numProcessesVisited < processes) {
				itVector = (CausalVectorClock) clockTable.get(it);
				if (itVector != null) {
					if (itVector.isOrigin == true) {
						// los valores deadLine seran los minimos entre los que
						// habia y las
						// posiciones finales de la flecha
						for (CellPosition endPos : itVector.finalPos) {
							int lastDeadLine = deadLine.get(endPos.process);
							deadLine.set(endPos.process, Math.min(lastDeadLine,
									endPos.tick));
							if (visitedProcesses.elementAt(endPos.process) == false) {
								visitedProcesses.set(endPos.process, true);
								numProcessesVisited++;
							}
						}
					}
				}
				// obtenemos el siguiente proceso que vamos a comprobar
				it.tick++;
			}
			numGlobalVisited++;
			globalVisited.set(it.process, true);
			int min = Integer.MAX_VALUE;
			int newProcess = 0;
			for (int i = 0; i < processes; i++) {
				if (globalVisited.get(i) == false) {
					int valor = deadLine.get(i);
					if (valor < min) {
						min = valor;
						newProcess = i;
					}
				}
			}

			it.process = newProcess;
			it.tick = min;

		}

		// obtenemos los intervalos disponibles
		for (int i = 0; i < processes; i++) {
			if (i != origin.process) {
				start.process = i;
				if (start.process == conflictFinal.process)
					start.tick = conflictFinal.tick + 1;
				else
					start.tick = forcedStart.elementAt(i);
				end.process = i;
				end.tick = deadLine.get(i) - 1;
				availableCell.add(new Interval(start.clone(), end.clone()));
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

}
