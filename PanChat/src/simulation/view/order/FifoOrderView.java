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
	
	boolean numProcessChanged;

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
				if (newVector.origin.tick < lastTick) {
					// solo se recalcula si no estamos recalculando ya
					recalculateVectors(newVector.origin.tick);
					lastTick = Math.max(lastTick,newVector.finalPos.firstElement().tick);
				}
				else if(newVector.finalPos.firstElement().tick < lastTick){
					// solo se recalcula si no estamos recalculando ya
					recalculateVectors(newVector.finalPos.firstElement().tick);	
				}
				else
					lastTick = Math.max(newVector.origin.tick,
							newVector.finalPos.firstElement().tick);
			}
		} else {
			/* si la marca de tiempo no es correcta, se elimina el origen de la
			* misma si no tiene flechas finales
			* hay que eliminar de la lista de posiciones final de origen la que corresponde
			* a la flecha erronea
			*/
			VectorClock originalClock = clockTable.get(newVector.origin);
			originalClock.finalPos.remove(newVector.finalPos.firstElement());
			
			if(originalClock.finalPos.size()<1){
				clockTable.remove(newVector.origin);
			}
			else
				clockTable.put(newVector.origin, originalClock);
		}
		return correctness;
	}

	public void recalculateVectors(int originalTick) {
		int size = simulationModel.getNumProcesses();
		
		CellPosition origin = new CellPosition(size, 0);
		
		recalculateLoop(origin,originalTick,true);
		recalculateLoop(origin,originalTick,false);
		if(numProcessChanged == true)
			numProcessChanged = false;
		
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
					
					//si ha cambiado el numero de procesos, hay que cambiar el tamaño de los arrays
					if(numProcessChanged == true){
						actualVector.newDimension(simulationModel.getNumProcesses());
					}
					clockTable.put(origin, actualVector);
				}
			}
		}
	}
		public VectorClock locateVector(VectorClock newVector) {
		VectorClock vectorFound = null;
		CellPosition actualPosition;
		
		actualPosition = new CellPosition(newVector.drawingPos.process,
				newVector.drawingPos.tick-1);

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
		recalculateVectors(initPos.tick);
	}
	
	private void aniadirVector(VectorClock vector){
		//si es posicional final se aniade
		if(!vector.isOrigin)
			clockTable.put(vector.drawingPos, vector);
		else{
			/*
			 * si es origen, el vector puede ser multiple. En tal caso, solo
			 * habra que aniadir la posicion final a la lista y actualizar el vector
			 */
			VectorClock origin = clockTable.get(vector.origin);
			if(origin == null){
				clockTable.put(vector.drawingPos, vector);
			}
			else{
				//si ya habia un vector que iba desde el proceso origen y mismo tick
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
				//si el vector no ha sido sustituido es que era un vector final mas de un vector multiple
				//por lo tanto, hay que incrementar la posicion correspondiente del vector inicial
				if(found == false)
					origin.incrPos(vector.finalPos.firstElement().process);
				
				origin.finalPos.add(vector.finalPos.firstElement());
				clockTable.put(origin.origin, origin);
			}
		}
	}
	
	public void setNumProcessChanged(){
		numProcessChanged = true;
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
	
	public void print(String s){
		System.out.println(s);
	}

	
}
