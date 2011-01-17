package simulation3.static_order;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import simulation3.arrows.MessageArrow;
import simulation3.arrows.SingleArrow;
import simulation3.view.CellPosition;
import simulation3.view.SimulationView;

public class OrderDrawing implements Serializable {

	private static final long serialVersionUID = 1L;

	int numProcesses;

	int width;
	int height;
	int padX;
	int padY;

	private CausalOrderView causalLayer;
	private TotalOrderView totalLayer;

	private SingleArrow lastArrow;

	private boolean someoneWrong;

	// vectores que contienen las celdas disponibles para cada tipo de orden
	private Vector<Interval> fifoOrder;
	private Vector<Interval> causalOrder;
	private Vector<Interval> totalOrder;

	// tabla hash con los diferentes relojes de cada capa
	HashMap<CellPosition, VectorI> fifoClockTable;
	HashMap<CellPosition, VectorI> causalClockTable;
	HashMap<CellPosition, VectorI> totalClockTable;

	// para saber en que orden nos encontramos
	boolean isFifoOrder;
	boolean isCausalOrder;
	boolean isTotalOrder;

	/*
	 * cuando tenemos fifo y causal, solo se toma en cuenta causal puesto que es
	 * mas fuertefifo. Sin embargo, puede que al usuario le interese que se
	 * dibujes los vectores logicosde fifo o de causal.
	 */
	boolean drawFifoVector;

	// nos indican quien ha pedido ser dibujado
	boolean fifoRequestDrawing;
	boolean causalRequestDrawing;
	boolean totalRequestDrawing;

	boolean totalMiss;
	boolean causalMiss;

	// flecha que ha provocado el conflicto. es la misma para todas las capas
	CellPosition arrowOrigin;

	boolean ready;

	public OrderDrawing(int numProcesses) {
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

	public void draw(Graphics2D g2, HashMap<CellPosition, VectorI> clocks,
			Vector<Interval> intervals, CellPosition orig) {

		if (orig != null)
			arrowOrigin = orig.clone();
		// detecta quien pide el dibujado y registra sus datos
		saveData(clocks, intervals);

		/*
		 * solo se procede a dibujar cuando el ultimo de los ordenes ha pedido
		 * ser dibujado y, en consecuencia, tenemos los datos de todas las capas
		 * para saber que dibujar
		 */
		ready = isReady();
		if (ready == true) {
			someoneWrong = true;
			if (isTotalOrder && isCausalOrder) {
				CellPosition init = lastArrow.getInitialPos();
				CellPosition end = lastArrow.getFinalPos();
				if (totalMiss && !causalMiss) {
					causalLayer.addLogicalOrder(new SingleArrow(init, end));
					causalOrder = causalLayer.getAvailableCell();
					causalLayer.removeFinalOrder(end);
				} else if (!totalMiss && causalMiss) {
					totalLayer.addLogicalOrder(new SingleArrow(init, end));
					totalOrder = totalLayer.getAvailableCell();
					totalLayer.removeFinalOrder(end);
				}
			}

			Vector<HashMap<CellPosition, VectorI>> drawingClockTable;
			VectorClock fifoVector;
			CausalVectorClock causalVector;
			TotalMessage totalVector;

			/*
			 * hay que decidir que relojes se van a dibujar
			 */
			drawingClockTable = selectDrawingClocks();

			for (HashMap<CellPosition, VectorI> clockTable : drawingClockTable) {
				for (Object vector : clockTable.values()) {
					if (vector instanceof VectorClock) {
						fifoVector = (VectorClock) vector;
						fifoVector.draw(g2);
					} else if (vector instanceof CausalVectorClock) {
						causalVector = (CausalVectorClock) vector;
						causalVector.draw(g2);
					} else if (vector instanceof TotalMessage) {
						totalVector = (TotalMessage) vector;
						totalVector.draw(g2);
					}
				}
			}

			/*
			 * if(doPrint){ if(availableCell != null){ for (Interval
			 * inter:availableCell) print("disponibles: "+inter); doPrint =
			 * false; } }
			 */

			drawHelp(g2);

			fifoRequestDrawing = false;
			causalRequestDrawing = false;
			totalRequestDrawing = false;

			causalOrder = null;
			totalOrder = null;
			fifoOrder = null;

			someoneWrong = false;
		}

		System.gc();
	}

	private void saveData(HashMap<CellPosition, VectorI> clocks,
			Vector<Interval> intervals) {
		// si es una tabla hash de relojes fifo
		if (!clocks.isEmpty()) {
			VectorI unknownVector = clocks.values().iterator().next();
			if (unknownVector instanceof VectorClock) {
				fifoClockTable = clocks;
				fifoOrder = intervals;
				fifoRequestDrawing = true;
			} else if (unknownVector instanceof CausalVectorClock) {
				causalClockTable = clocks;
				causalOrder = intervals;
				causalRequestDrawing = true;
			} else if (unknownVector instanceof TotalMessage) {
				totalClockTable = clocks;
				totalOrder = intervals;

				totalRequestDrawing = true;
			}
		}
	}

	private boolean isReady() {
		/*
		 * se comprueba si todos los ordenes disponibles actualmente nos han
		 * pedido que les dibujemos, si es asi, se devuelve true, si no false
		 */
		return fifoRequestDrawing == isFifoOrder
				&& causalRequestDrawing == isCausalOrder
				&& totalRequestDrawing == isTotalOrder;
	}

	private Vector<HashMap<CellPosition, VectorI>> selectDrawingClocks() {
		Vector<HashMap<CellPosition, VectorI>> drawing = new Vector<HashMap<CellPosition, VectorI>>();
		/*
		 * si esta seleccionado fifo y causal se se eligen los vectores de
		 * causal a menos que el usuario quiera los de fifo explicitamente
		 */
		if (isFifoOrder && (drawFifoVector == true || isCausalOrder == false)) {
			drawing.add(fifoClockTable);
		}
		if (isCausalOrder && drawFifoVector == false) {
			drawing.add(causalClockTable);
		}

		if (isTotalOrder == true) {
			drawing.add(totalClockTable);
		}
		return drawing;
	}

	public void drawHelp(Graphics2D g2) {

		Color color = Color.GREEN;
		if (isCausalOrder && isTotalOrder && totalOrder != null
				&& causalOrder != null) {
			// si estan los dos activados es necesario hacer la interseccion
			Intersection intersection = intersect(causalOrder, totalOrder);

			for (Interval inter : intersection.causal) {
				draw(g2, inter.start, inter.end, causalClockTable, Color.BLUE);
			}
			for (Interval inter : intersection.total) {
				draw(g2, inter.start, inter.end, totalClockTable, Color.GRAY);
			}
			for (Interval inter : intersection.causalTotal) {
				draw(g2, inter.start, inter.end, causalClockTable, Color.GREEN);
			}

		} else if (isCausalOrder) {
			if (causalOrder != null) {
				for (Interval inter : causalOrder)
					draw(g2, inter.start, inter.end, causalClockTable, color);
			}
		} else if (isFifoOrder) {
			if (fifoOrder != null) {
				for (Interval inter : fifoOrder)
					draw(g2, inter.start, inter.end, fifoClockTable,
							Color.GREEN);
			}
		} else if (isTotalOrder) {
			if (totalOrder != null)
				for (Interval inter : totalOrder)
					draw(g2, inter.start, inter.end, totalClockTable, color);
		}

	}

	private Intersection intersect(Vector<Interval> causal,
			Vector<Interval> total) {
		// el primer vector sera el de causal y el segundo el de total
		Intersection inter = new Intersection();
		int proc;
		// para cada elemento de vec 1 se comprueba si hay un intervalo en vec 2
		// y se calcula
		// la interseccion
		int startCausal;
		int startTotal;

		int finalCausal;
		int finalTotal;

		for (Interval section1 : causal) {
			for (Interval section2 : total) {
				proc = section1.start.process;
				if (proc == section2.start.process) {
					int start = Math.max(section1.start.tick,
							section2.start.tick);
					int end = Math.min(section1.end.tick, section2.end.tick);
					inter.causalTotal.add(getInterval(proc, start, end));

					// calculamos los intervalos iniciales
					if (section1.start.tick < section2.start.tick) {
						startCausal = section1.start.tick;
						finalCausal = section2.start.tick - 1;
						inter.causal.add(getInterval(proc, startCausal,
								finalCausal));
					} else {
						startCausal = section2.start.tick;
						finalCausal = section1.start.tick - 1;
						inter.total.add(getInterval(proc, startCausal,
								finalCausal));
					}

					// calculamos los intervalos finales
					if (section1.end.tick > section2.end.tick) {
						startTotal = section2.end.tick + 1;
						finalTotal = section1.end.tick;
						inter.causal.add(getInterval(proc, startTotal,
								finalTotal));
					} else {
						startTotal = section1.end.tick + 1;
						finalTotal = section2.end.tick;
						inter.total.add(getInterval(proc, startTotal,
								finalTotal));
					}

				}
			}
		}
		return inter;
	}

	private Interval getInterval(int proc, int initTick, int finalTick) {
		CellPosition initCell;
		CellPosition finalCell;
		Interval inter;
		initCell = new CellPosition(0, 0);
		initCell.process = proc;
		finalCell = initCell.clone();
		initCell.tick = initTick;
		finalCell.tick = finalTick;
		inter = new Interval(initCell, finalCell);
		return inter;
	}

	private void draw(Graphics2D g2, CellPosition start, CellPosition end,
			HashMap<CellPosition, VectorI> clockTable, Color color) {
		// el intervalo (start, end) esta disponible si no esta ocupado por
		// otras flechas
		int x, y;
		int difX;
		int initX;
		int numberCell = 0;
		boolean found;
		CellPosition drawingPos = new CellPosition(numProcesses, 0);

		// dibujamos un recuadro alrededor del origen
		x = padX + (arrowOrigin.tick) * width;
		y = padY + arrowOrigin.process * (height + padY);
		g2.setColor(Color.RED);
		g2.drawRect(x, y, width, height);

		/*
		 * los posibles destinos de la flecha pueden, en el proceso destino,
		 * desde start hasta end
		 */
		g2.setColor(color);
		difX = end.tick - start.tick;
		drawingPos.process = end.process;
		drawingPos.tick = start.tick;
		y = padY + end.process * (height + padY);
		while (difX >= 0) {
			initX = drawingPos.tick;
			numberCell = 0;
			while (!(found = clockTable.containsKey(drawingPos)) && difX >= 0) {
				numberCell++;
				difX--;
				drawingPos.tick++;
			}
			x = padX + initX * width;
			if (numberCell != 0)
				g2.drawRect(x, y, numberCell * width, height);
			// si se encontro algun elemento hay que decrementar el control aqui
			if (found) {
				difX--;
				drawingPos.tick++;
			}

		}

		g2.setColor(Color.BLACK);
	}

	/*
	 * public void setFifoOrderVector(Vector<Interval> fifo){ fifoOrder = fifo;
	 * }
	 * 
	 * public void setCausalOrderVector(Vector<Interval> causal){ causalOrder =
	 * causal; }
	 * 
	 * public void setTotalOrderVector(Vector<Interval> total){ totalOrder =
	 * total; }
	 */

	public void setFifoOrder(boolean isFifo) {
		isFifoOrder = isFifo;
	}

	public void setCausalOrder(boolean isCausal) {
		isCausalOrder = isCausal;
	}

	public void setTotalOrder(boolean isTotal) {
		isTotalOrder = isTotal;
	}

	public void setNumProcesses(int numProcesses) {
		this.numProcesses = numProcesses;
	}

	public void setShowFifoVector() {
		drawFifoVector = true;
	}

	public void unsetShowFifoVector() {
		drawFifoVector = false;
	}

	public void setTotalMiss() {
		totalMiss = true;

	}

	public void unsetTotalMiss() {
		totalMiss = false;
	}

	public boolean getTotalMiss() {
		return totalMiss;
	}

	public void setCausalMiss() {
		causalMiss = true;
	}

	public void unsetCausalMiss() {
		causalMiss = false;
	}

	public boolean getCausalMiss() {
		return causalMiss;
	}

	public boolean isTotalOrder() {
		return isTotalOrder;
	}

	public boolean isCausalOrder() {
		return isCausalOrder;
	}

	public boolean isSomeoneWrong() {
		return someoneWrong;
	}

	public CausalOrderView getCausalOrder() {
		return causalLayer;
	}

	public void setCausalOrder(CausalOrderView causal) {
		causalLayer = causal;
	}

	public TotalOrderView getTotalOrder() {
		return totalLayer;
	}

	public void setTotalOrder(TotalOrderView total) {
		totalLayer = total;
	}

	public void setLastArrow(SingleArrow arrow) {
		lastArrow = (SingleArrow) arrow.clone();
	}

	public void print(String out) {
		System.out.println(out);
	}

	/*
	 * public void setFifoClockTable(HashMap<CellPosition,VectorClock>
	 * fifoTable){ fifoClockTable = fifoTable; }
	 * 
	 * public void setCausalClockTable(HashMap<CellPosition,CausalVectorClock>
	 * causalTable){ causalClockTable = causalTable; }
	 */
}
