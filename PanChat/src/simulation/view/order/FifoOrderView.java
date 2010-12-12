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
	public boolean addLogicalOrder(SingleArrow arrow) {
		/*
		 * Dada una flecha, se aniade sus correspondientes marcas de tiempo
		 * tanto en origen como en destino El valor devuelto indica que hay
		 * inconsistencia en las marcas de tiempo(true) o que no (false)
		 */
		boolean correctness = true;

		addLogicalOrder(arrow.getInitialPos(), arrow.getFinalPos(), true);
		correctness = addLogicalOrder(arrow.getInitialPos(), arrow.getFinalPos(),false);

		// VectorClock.print = true;
		debug("Correctness: " + correctness);

		return correctness;
	}

	private boolean addLogicalOrder(CellPosition origin, CellPosition position,
			boolean isOrigin) {
		/*
		 * se encarga de aniadir en destino (position) un nuevo vector logico
		 * procedente de origin
		 */
		boolean correctness = true;
		VectorClock lastVector;
		VectorClock newVector;
		int size = this.simulationModel.getNumProcesses();
		newVector = new VectorClock(origin, position, isOrigin,size);

		// tenemos que encontrar el ultimo vector para ese proceso
		lastVector = locateVector(newVector);

		// si se ha encontrado un vector anterior, se copia e incrementa
		if (lastVector != null) {
			newVector.setVector(lastVector);
		} else {
			newVector.initialize();
		}
		//hay que incrementar el valor correspondiente
		if(newVector.isOrigin)
			newVector.incrPos(position.process);
		else
			newVector.incrPos(origin.process);

		/*
		 * se comprueba que el nuevo vector sea correcto si no es de origen,
		 * para ello tanto el vector de origen como de destino han de cumplir
		 * una serie de propiedades
		 */
		if (newVector.isOrigin == false)
			correctness = newVector.isCorrect(clockTable.get(origin));

		// si es correcto se introduce en la tabla
		if (correctness == true) {
			aniadirVector(newVector);
			/*
			 * si el origen o destino de esta flecha es anterior a la llegada de
			 * otros mensajes puede que al introducir esta flecha ser modifiquen
			 * los que llegan posteriormente Habra que recalcularlos esta
			 * comprobacion se realizara solo en el vector de destino
			 */
			if (newVector.isOrigin == false) {
				if (newVector.origin.tick < lastTick
						|| newVector.finalPos.get(0).tick < lastTick) {
					// solo se recalcula si no estamos recalculando ya
					recalculateVectors2(newVector.origin.tick);
				} else
					lastTick = Math.max(newVector.origin.tick,
							newVector.finalPos.get(0).tick);
			}
		} else {
			// si la marca de tiempo no es correcta, se elimina el origen de la
			// misma
			clockTable.remove(newVector.origin);
		}
		return correctness;
	}

	public void recalculateVectors2(int originalTick) {
		int size = simulationModel.getNumProcesses();
		
		CellPosition origin = new CellPosition(size, 0);
		recalculateLoop(origin,originalTick,true);
		recalculateLoop(origin,originalTick,false);	
	}
	
	//hace la pasada para insertar origenes o destinos. Si originOrFinal == true
	//en la pasada se calculan origenes, false se calculan destinos
	public void recalculateLoop(CellPosition origin,int originalTick,boolean originOrFinal){
		VectorClock actualVector;
		VectorClock lastVector;
		for (int i = originalTick; i <= lastTick; i++) {
			origin.tick = i;
			for (int j = 0; j < simulationModel.getNumProcesses(); j++) {
				origin.process = j;
				actualVector = clockTable.get(origin);
				if(actualVector != null && actualVector.isOrigin==originOrFinal){
					lastVector = locateVector(actualVector);
					if(lastVector == null){
						actualVector.initialize();	
					}
					else
						actualVector.setVector(lastVector);
					
					//se actualizan los valores del vector
					if(actualVector.isOrigin){
						for(CellPosition finalPos: actualVector.finalPos)
							actualVector.incrPos(finalPos.process);
					}
					else
						actualVector.incrPos(actualVector.origin.process);
					
					clockTable.put(origin, actualVector);
				}
			}
		}
	}
	public void recalculateVectors(int originalTick) {
		int size = simulationModel.getNumProcesses();
		// solo se recalcula si no lo haciamos ya para evitar ciclos
		if (isRecalculating == false) {
			isRecalculating = true;
			/*
			 * se recalculan todos los relojes a partir del tickOrigen +1
			 */
			int originTick = originalTick + 1;
			CellPosition origin = new CellPosition(size, 0);
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
							System.out.println("number elements:"+clockTable.size());
							/*
							 * se aniade el primer elemento somo si fuera un vector
							 * unico. Si es un vector multiple, los demas se aniaden 
							 * uno a uno.
							 */
							addLogicalOrder(actualVector.origin,
									actualVector.finalPos.firstElement(), true);
							//se aniade el final tambien
							addLogicalOrder(actualVector.origin,
									actualVector.finalPos.firstElement(), false);
							if(actualVector.isMultiple()){
								actualVector.finalPos.removeElementAt(0);
								VectorClock rest = new VectorClock(actualVector.origin,
										null,true,size);
								for(CellPosition finalPos: actualVector.finalPos){
									rest.setUniqueFinalPos(finalPos);
									aniadirVector(rest);
									//se aniade el final tambien
									addLogicalOrder(actualVector.origin,
											finalPos, false);
								}
							}

							debug("Tamanio despues de anadir: "
									+ clockTable.size());

							debug = true;
							
							

							debug("Tamanio despues de la pos final de anadir: "
									+ clockTable.size());
						} else
							addLogicalOrder(actualVector.origin,
									actualVector.finalPos.firstElement(), false);
					}
				}
			}
			isRecalculating = false;
		}

	}

	public VectorClock locateVector(VectorClock newVector) {
		VectorClock vectorFound = null;
		CellPosition actualPosition;
		
		actualPosition = new CellPosition(newVector.drawingPos.process,
				newVector.drawingPos.tick-1);
		//si la posicion inicial es una flecha multiple, estara en la tabla
		/*boolean isMultiple;
		isMultiple = clockTable.containsKey(newVector.drawingPos);
		if (isMultiple)
			// si es multiple hay que contar el propio vector, para que no borre
			// los valores ya obtenidos
			actualPosition = new CellPosition(newVector.drawingPos.process,
					newVector.drawingPos.tick);
		else
			actualPosition = new CellPosition(newVector.drawingPos.process,
					newVector.drawingPos.tick - 1);
		*/
		

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
	public void removeFinalOrder(CellPosition finalPos) {
		//si falla aqui es pos que no se han tenido en cuenta los decrease
		VectorClock removedVector;
		VectorClock origin;
		removedVector = clockTable.remove(finalPos);
		if(removedVector!=null){
			origin = clockTable.get(removedVector.origin);
			if(!(origin.isMultiple()))
				clockTable.remove(removedVector.origin);
			else{
				//quitamos la posicion final del vector multiple
				origin.finalPos.remove(finalPos);
				origin.decrease(finalPos.process);
				clockTable.put(removedVector.origin, origin);
			}
			recalculateVectors2(removedVector.origin.tick);
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
		VectorClock removed;
		removed = clockTable.remove(initPos);
		// hay que disminuir en 1 la posicion correspondiente en el origne
		// ESTRICTAMENTE NECESARIO
		
		if(removed!=null){
			for(CellPosition finalPos:removed.finalPos){
				clockTable.remove(finalPos);
			}
			debug("tamanio de la tabla de relojes: " + clockTable.size());
		}
		recalculateVectors2(initPos.tick);
	}
	
	private void aniadirVector(VectorClock vector){
		//si es posicional final se aniade
		if(!vector.isOrigin)
			clockTable.put(vector.drawingPos, vector);
		else{
			/*
			 * si es origen, el vector es multiple. En tal caso, solo
			 * habra que aniadir la posicion final a la lista y actualizar el vector
			 */
			VectorClock origin = clockTable.get(vector.origin);
			if(origin == null){
				clockTable.put(vector.drawingPos, vector);
			}
			else{
				//si ya habia un vector que iba desde el proceso origen y tick origen
				//hasta el proceso final en otro  tick, se elimina
				boolean found = false;
				int index = 0;
				/*if(origin.finalPos.contains(vector.finalPos.firstElement())){
					origin.finalPos.remove(vector.finalPos.firstElement());
					clockTable.remove(vector.finalPos.firstElement());
				}*/
				
				for (CellPosition finalPos:origin.finalPos){
					if(finalPos.process==vector.finalPos.firstElement().process){
						found = true;
						break;
					}
					index++;
				}
				if(found == true){
					clockTable.remove(origin.finalPos.elementAt(index));
					origin.finalPos.remove(index);	
				}
				origin.finalPos.add(vector.finalPos.firstElement());
				origin.incrPos(vector.finalPos.firstElement().process);
			}
		}
	}
	
	/*public void removeOriginLogicalOrder(CellPosition initPos) {
		debug("eliminado: " + initPos);
		clockTable.remove(initPos);
		// hay que disminuir en 1 la posicion correspondiente en el origne
		// ESTRICTAMENTE NECESARIO
	}*/
	

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
