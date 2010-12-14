package simulation.view.order;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Vector;

import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;
import simulation.view.SimulationView;

public class OrderDrawing {
	int numProcesses;
	
	int width;
	int height;
	int padX;
	int padY;
	
	//vectores que contienen las celdas disponibles para cada tipo de orden
	Vector<Interval> fifoOrder;
	Vector<Interval> causalOrder;
	Vector<Interval> totalOrder;
	
	//tabla hash con los diferentes relojes de cada capa
	HashMap<CellPosition,VectorI> fifoClockTable;
	HashMap<CellPosition,VectorI> causalClockTable;
	
	//para saber en que orden nos encontramos
	boolean isFifoOrder;
	boolean isCausalOrder;
	boolean isTotalOrder;
	
	/*cuando tenemos fifo y causal, solo se toma en cuenta causal puesto que es mas fuerte
	*fifo. Sin embargo, puede que al usuario le interese que se dibujes los vectores logicos
	*de fifo o de causal.
	*/
	boolean drawFifoVector;
	
	//nos indican quien ha pedido ser dibujado
	boolean fifoRequestDrawing;
	boolean causalRequestDrawing;
	boolean totalRequestDrawing;
	
	//flecha que ha provocado el conflicto. es la misma para todas las capas
	CellPosition arrowOrigin;
	
	boolean ready;
	
	public OrderDrawing(int numProcesses){
		width = SimulationView.cellWidth;
		height = SimulationView.cellHeight;
		padX = SimulationView.paddingX;
		padY = SimulationView.paddingY;
		
		this.numProcesses = numProcesses;
		
		isFifoOrder = false;
		isCausalOrder = false;
		isTotalOrder = false;
		
		fifoRequestDrawing = false;
		causalRequestDrawing = false;
		totalRequestDrawing = false;
		
		drawFifoVector = false;
		
		ready = false;
	}
	public void draw(Graphics2D g2,HashMap<CellPosition,VectorI> clocks,
			Vector<Interval> intervals,CellPosition orig){
		if(orig != null)
			arrowOrigin = orig.clone();
		//detecta quien pide el dibujado y registra sus datos
		saveData(clocks,intervals);
		
		/*
		 * solo se procede a dibujar cuando el ultimo de los ordenes ha pedido ser dibujado
		 * y, en consecuencia, tenemos los datos de todas las capas para saber que dibujar
		 */
		ready = isReady();
		if(ready == true){
			Vector<HashMap<CellPosition,VectorI>> drawingClockTable;
			VectorClock fifoVector;
			CausalVectorClock causalVector;
			/*
			 * hay que decidir que relojes se van a dibujar
			 */
			drawingClockTable = selectDrawingClocks();
			for(HashMap<CellPosition,VectorI> clockTable:drawingClockTable){
				for (Object vector : clockTable.values()) {
					if(vector instanceof VectorClock){
						fifoVector = (VectorClock) vector;
						fifoVector.draw(g2);
					}
					else if(vector instanceof CausalVectorClock){
						causalVector = (CausalVectorClock) vector;
						causalVector.draw(g2);
					}
				}
			}
			
			/*if(doPrint){
			if(availableCell != null){
				for (Interval inter:availableCell)
					print("disponibles: "+inter);
				doPrint = false;
			}
			}*/
			
			//por si es necesario dibujar la ayuda
			drawHelp(g2);
			
			fifoRequestDrawing = false;
			causalRequestDrawing = false;
			totalRequestDrawing = false;
		}
	}
	
	
	private void saveData(HashMap<CellPosition, VectorI> clocks,Vector<Interval> intervals){
		//si es una tabla hash de relojes fifo
		if(!clocks.isEmpty()){
			VectorI unknownVector = clocks.values().iterator().next();
			if(unknownVector instanceof VectorClock ){
				fifoClockTable =  clocks;
				fifoOrder = intervals;
				fifoRequestDrawing = true;
			}
			else if(unknownVector instanceof CausalVectorClock){
				causalClockTable = clocks;
				causalOrder = intervals;
				causalRequestDrawing = true;
			}
		}
	}
	
	private boolean isReady(){
		/*
		 * se comprueba si todos los ordenes disponibles actualmente nos han pedido que les
		 * dibujemos, si es asi, se devuelve true, si no false
		 */
		return fifoRequestDrawing == isFifoOrder && causalRequestDrawing == isCausalOrder
			&& totalRequestDrawing == isTotalOrder;
	}
	
	private Vector<HashMap<CellPosition,VectorI>> selectDrawingClocks(){
		Vector<HashMap<CellPosition,VectorI>> drawing = 
			new Vector<HashMap<CellPosition,VectorI>>();
		/*si esta seleccionado fifo y causal se se eligen los vectores de causal a menos
		 * que el usuario quiera los de fifo explicitamente
		 */
		if(isFifoOrder && ( drawFifoVector == true || isCausalOrder == false)){
			drawing.add(fifoClockTable);
		}
		if(isCausalOrder && drawFifoVector == false){
			drawing.add(causalClockTable);
		}
		
		
		//no se debe permitir orden total, fifo no causal
		if(isTotalOrder && (isCausalOrder == true || 
							(isCausalOrder == false && isFifoOrder == false))){
			//aniadir el vector de total;
		}
		return drawing;
	}
	
	public void drawHelp(Graphics2D g2){
		if(isCausalOrder){
			if(causalOrder != null){
				for(Interval inter: causalOrder)
					draw(g2,inter.start,inter.end,causalClockTable);
			}
		}
		else if(isFifoOrder){
			if(fifoOrder != null){
				for(Interval inter: fifoOrder)
					draw(g2,inter.start,inter.end,fifoClockTable);
			}
		}
	}
	private void draw(Graphics2D g2,CellPosition start,CellPosition end,
			HashMap<CellPosition,VectorI> clockTable){
		//el intervalo (start, end) esta disponible si no esta ocupado por otras flechas
		int x,y;
		int difX;
		int initX;
		int numberCell = 0;
		boolean found;
		CellPosition drawingPos = new CellPosition(numProcesses, 0);
		
		//dibujamos un recuadro alrededor del origen
		x = padX + (arrowOrigin.tick)*width;
		y = padY + arrowOrigin.process*(height+padY);
		g2.setColor(Color.RED);
		g2.drawRect(x, y, width, height);
		
		/*
		 * los posibles destinos de la flecha pueden, en el proceso destino,
		 * desde start hasta end
		 */
		g2.setColor(Color.GREEN);
		difX =  end.tick - start.tick;
		drawingPos.process = end.process;
		drawingPos.tick = start.tick;
		y = padY + end.process*(height+padY);
		while(difX >= 0){
			initX = drawingPos.tick;
			numberCell = 0;
			while(!(found = clockTable.containsKey(drawingPos)) && difX >=0){
				numberCell++;
				difX--;
				drawingPos.tick++;
			}
			x = padX + initX*width;
			if(numberCell != 0)
				g2.drawRect(x, y, numberCell*width, height);
			//si se encontro algun elemento hay que decrementar el control aqui
			if(found){
				difX--;
				drawingPos.tick++;
			}
			
		}
		
		g2.setColor(Color.BLACK);
	}
	
	/*public void setFifoOrderVector(Vector<Interval> fifo){
		fifoOrder = fifo;
	}
	
	public void setCausalOrderVector(Vector<Interval> causal){
		causalOrder = causal;
	}
	
	public void setTotalOrderVector(Vector<Interval> total){
		totalOrder = total;
	}*/
	
	public void setFifoOrder (boolean isFifo){
		isFifoOrder = isFifo;
	}
	
	public void setCausalOrder(boolean isCausal){
		isCausalOrder = isCausal;
	}
	
	public void setTotalOrder(boolean isTotal){
		isTotalOrder = isTotal;
	}
	
	public void setNumProcesses (int numProcesses){
		this.numProcesses = numProcesses;
	}
	
	public void setShowFifoVector(){
		drawFifoVector = true;
	}
	
	public void unsetShowFifoVector(){
		drawFifoVector = false;
	}
	/*public void setFifoClockTable(HashMap<CellPosition,VectorClock> fifoTable){
		fifoClockTable = fifoTable;
	}
	
	public void setCausalClockTable(HashMap<CellPosition,CausalVectorClock> causalTable){
		causalClockTable = causalTable;
	}*/
}
