package simulation.view.order;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


import simulation.arrows.SingleArrow;
import simulation.model.SimulationModel;
import simulation.view.CellPosition;

public class FifoOrderView implements OrderI{
	HashMap <CellPosition,VectorClock> clockTable;
	
	//indica que el ultimo tick en el que hay un vector
	int lastTick;
	
	//indica si estamos recalculando, para que no se produzca un ciclo
	boolean isRecalculating;
	//Vector <VectorClock> posClock;
	
	public FifoOrderView (){
		clockTable = new HashMap<CellPosition,VectorClock>();
		lastTick = 1;
		isRecalculating = false;
		//posClock = new Vector<VectorClock>();
	}
	@Override
	public boolean addLogicalOrder(SingleArrow arrow) {
		/*
		 * Dada una flecha, se aniade sus correspondientes marcas de tiempo
		 * tanto en origen como en destino
		 * El valor devuelto indica que hay inconsistencia en las marcas de 
		 * tiempo(true) o que no (false)
		 */
		boolean correctness = true;
		
		addLogicalOrder(arrow.getInitialPos(),arrow.getFinalPos(),true);
		correctness = addLogicalOrder(arrow.getInitialPos(),arrow.getFinalPos(),false);
		VectorClock.print = true;
		System.out.println("Correctness: "+correctness);
		return correctness;
	}
	
	
	
	public boolean addLogicalOrder (CellPosition origin,CellPosition position,boolean isOrigin){
		/*
		 * se encarga de añadir en destino (position) un nuevo vector logico
		 * procedente de origin
		 */
		boolean correctness = true;
		VectorClock lastVector;
		VectorClock newVector;
		newVector = new VectorClock(origin,position,isOrigin);
		
		//tenemos que encontrar el ultimo vector para ese proceso
		lastVector = locateVector(newVector);
		
		//si se ha encontrado un vector anterior, se copia e incrementa
		if(lastVector!=null){
			newVector.setVector(lastVector);
		}
		else{
			newVector.initialize();
		}
		
		/*
		 * se comprueba que el nuevo vector sea correcto si no es de origen, para ello
		 * tanto el vector de origen como de destino han de cumplir una serie
		 * de propiedades
		 */
		if(newVector.isOrigin == false)
			correctness = newVector.isCorrect(clockTable.get(origin));
		
		//si es correcto se introduce en la tabla
		if(correctness == true){
			clockTable.put(newVector.drawingPos,newVector);
			/*
			 * si el origen o destino de esta flecha es anterior a la llegada de otros mensajes
			 * puede que al introducir esta flecha ser modifiquen los que llegan posteriormente
			 * Habra que recalcularlos
			 * esta comprobacion se realizara solo en el vector de destino
			 */
			if(newVector.isOrigin == false){
				if(newVector.origin.tick < lastTick || newVector.finalPos.tick < lastTick){
					//solo se recalcula si no estamos recalculando ya
					recalculateVectors(newVector);
				}
				else
					lastTick = Math.max(newVector.origin.tick , newVector.finalPos.tick );
			}
		}
		else{
			//si la marca de tiempo no es correcta, se elimina el origen de la misma
			clockTable.remove(newVector.origin);
		}
		return correctness;
	}
	
	public void recalculateVectors(VectorClock newVector){
		//solo se recalcula si no lo haciamos ya para evitar ciclos
		if(isRecalculating == false){
			isRecalculating = true;
			/*
			 * se recalculan todos los relojes a partir del tickOrigen +1
			 */
			int originTick = newVector.origin.tick + 1;
			CellPosition origin = new CellPosition(SimulationModel.numProcesses,0);
			VectorClock actualVector;
			
			for (int i = originTick;i<= lastTick;i++){
				origin.tick = i;
				for(int j = 0;j<SimulationModel.numProcesses;j++){
					origin.process = j;
					/*
					 *si existe un vector con origen en el proceso j y tick i
					 *se eliminan y se pide que se calculen de nuevo 
					 */
					actualVector = clockTable.get(origin);
					if(actualVector != null && actualVector.isOrigin){	
						addLogicalOrder(actualVector.origin,actualVector.finalPos,true);
						addLogicalOrder(actualVector.origin,actualVector.finalPos,false);
					}
				}
			}
			isRecalculating = false;
		}
		
	}
	
	
	public VectorClock locateVector(VectorClock newVector){
		VectorClock vectorFound = null;
		CellPosition actualPosition = new CellPosition(newVector.drawingPos.process,newVector.drawingPos.tick-1);
		
		while(actualPosition.tick>=0 && vectorFound == null){
			if(clockTable.containsKey(actualPosition)){
				vectorFound = clockTable.get(actualPosition);
				/*
				 * hay que comprobar que el encontrado sea de la misma naturaleza
				 * que el nuevo vector, es decir, que los dos sean de origen o no
				 * lo sea ninguno
				 */
				if(!(newVector.isOrigin==vectorFound.isOrigin))
					vectorFound = null;
			}
			actualPosition.tick--;
		}
		
		/*si no hemos encontrado el vector anterior es que esta es la primera
		 *que se recibe un mensaje, por lo que se devolvera null
		 */
		return vectorFound;
	}

	@Override
	public boolean moveLogicalOrder(SingleArrow arrow) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeLogicalOrder(SingleArrow arrow) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Vector<VectorClock> getVectorClocks(){
		Collection<VectorClock> colec = clockTable.values();
		Iterator<VectorClock> iter = colec.iterator();
		Vector<VectorClock> vectorClock = new Vector<VectorClock>();
		System.out.println("el tamanio del vector en clock es: "+clockTable.size());
		
		while(iter.hasNext()){
			vectorClock.add(iter.next());
		}
		return vectorClock;
	}

}
