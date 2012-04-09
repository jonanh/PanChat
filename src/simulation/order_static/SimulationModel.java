package simulation.order_static;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Vector;

import simulation.arrows.MultipleArrow;
import simulation.arrows.SingleArrow;
import simulation.model.SimulationArrowModel;
import simulation.view.CellPosition;
import simulation.view.ISimulator;
import simulation.view.SimulationView;

/**
 * Clase que representa los datos del simulador :
 * 
 * - Lista de flechas
 * 
 * -
 */
@SuppressWarnings("serial")
public class SimulationModel extends SimulationArrowModel implements
		Serializable, ISimulator {

	// capa que se encarga de la ordenacion
	public Vector<OrderI> orderLayerVector = new Vector<OrderI>();
	public OrderDrawing drawingServer = new OrderDrawing(DEFAULT_NUM_PROCESSES);

	/**
	 * Construimos el objeto de datos de simulacion
	 */
	public SimulationModel() {
		super();
	}

	/**
	 * Construimos el objeto de datos de simulacion
	 */
	public SimulationModel(SimulationArrowModel simul) {
		this.setTimeTicks(simul.getTimeTicks());
		this.setNumProcesses(simul.getNumProcesses());

		for (MultipleArrow arrow : simul.getArrowList())
			this.addArrow(arrow);
	}

	/**
	 * 
	 * @param pNumProcesses
	 * 
	 * @return Establecemos un nuevo número de procesos
	 */
	@Override
	public int setNumProcesses(int pNumProcesses) {

		if (orderLayerVector == null)
			orderLayerVector = new Vector<OrderI>();

		int result = super.setNumProcesses(pNumProcesses);

		changeProcessesOrderLayers();

		return result;
	}

	/**
	 * 
	 * @param messageArrow
	 *            Añadimos esta fecla
	 */
	@Override
	public synchronized void addArrow(MultipleArrow messageArrow) {

		super.addArrow(messageArrow);

		Vector<Boolean> correctness = new Vector<Boolean>();

		// se introduce el correspondiente vector logico en todas las capas
		addLogicalOrderLayers(correctness, messageArrow);

		// si no es correcto de acuerdo a algun orden establecido se borra
		if (isCorrectOrderLayers(correctness) == false) {
			deleteArrow(messageArrow.getInitialPos());
		}
	}

	@Override
	public synchronized MultipleArrow deleteArrow(CellPosition position) {

		MultipleArrow multipleArrow = super.getArrow(position);

		if (multipleArrow == null)
			return null;

		// Si la posicion es la posicion inicial debemos borrar además
		// las referencias desde los nodos finales.
		if (multipleArrow.getInitialPos().equals(position)) {

			removeInitialOrderLayers(position);

		}
		// Si la posicion es la posicion de destino de una flecha entonces
		// eliminamos dicha flecha de la MultipleArrow
		else {

			// se borra la posicion de destino de todas las capas,
			// si es el ultimo vector se borra tambien el inicial
			removeFinalOrderLayers(position);
		}

		// se recalcula para todas las capas
		recalculateVectorsOrderLayers(multipleArrow.getInitialPos().tick - 1);

		return super.deleteArrow(position);
	}

	/*
	 * Rutinas ayudantes
	 */

	public void changeProcessesOrderLayers() {

		System.out.println(orderLayerVector);

		for (OrderI orderLayer : orderLayerVector) {
			orderLayer.setNumProcessChanged();
			orderLayer.recalculateVectors(0);
		}
	}

	public void addLogicalOrderLayers(Vector<Boolean> correctness,
			MultipleArrow messageArrow) {
		for (OrderI orderLayer : orderLayerVector) {
			for (SingleArrow arrow : messageArrow.getArrowList()) {
				correctness.add(orderLayer.addLogicalOrder(arrow));
			}
		}
	}

	public boolean isCorrectOrderLayers(Vector<Boolean> correctness) {
		// sera correcto si todas las componentes del vector son correctas
		boolean correct = true;
		for (Boolean bool : correctness) {
			correct = correct & bool;
		}
		return correct;
	}

	public void removeInitialOrderLayers(CellPosition initPos) {
		for (OrderI orderLayer : orderLayerVector) {
			orderLayer.removeInitialOrder(initPos);
		}
	}

	public void removeFinalOrderLayers(CellPosition finalPos) {
		for (OrderI orderLayer : orderLayerVector) {
			orderLayer.removeFinalOrder(finalPos);
		}
	}

	public void recalculateVectorsOrderLayers(int tick) {
		for (OrderI orderLayer : orderLayerVector) {
			orderLayer.recalculateVectors(tick);
		}
	}

	public void addFifoLayer() {
		FifoOrderView fifo = new FifoOrderView(this);

		// hay que aniadir aquellas flechas que ya existen
		addOrder(fifo);
		orderLayerVector.add(fifo);
		drawingServer.setFifoOrder(true);
	}

	public void addCausalLayer() {
		CausalOrderView causal = new CausalOrderView(this);
		addOrder(causal);
		orderLayerVector.add(causal);
		drawingServer.setCausalOrder(true);
	}

	public void addTotalLayer() {
		// no se debe permitir tener fifo, no causal y total
		TotalOrderView total = new TotalOrderView(this);
		int size = orderLayerVector.size();
		boolean isFifo = false;
		boolean isCausal = false;

		for (int i = 0; i < size; i++) {
			if (orderLayerVector.get(i) instanceof FifoOrderView)
				isFifo = true;
			else if (orderLayerVector.get(i) instanceof CausalOrderView)
				isCausal = true;
		}

		// si no hay orden fifo o hay causal, se inserta la capa
		if (!isFifo || isCausal) {
			addOrder(total);
			orderLayerVector.add(total);
			drawingServer.setTotalOrder(true);
		}
	}

	public void removeFifoLayer() {
		int size = orderLayerVector.size();
		for (int i = 0; i < size; i++) {
			if (orderLayerVector.get(i) instanceof FifoOrderView) {
				orderLayerVector.remove(i);
				break;
			}
		}
		drawingServer.unsetShowFifoVector();
		drawingServer.setFifoOrder(false);
	}

	public void removeCausalLayer() {
		int size = orderLayerVector.size();
		for (int i = 0; i < size; i++) {
			if (orderLayerVector.get(i) instanceof CausalOrderView) {
				orderLayerVector.remove(i);
				break;
			}
		}
		drawingServer.setCausalOrder(false);
	}

	public void removeTotalLayer() {
		int size = orderLayerVector.size();
		for (int i = 0; i < size; i++) {
			if (orderLayerVector.get(i) instanceof TotalOrderView) {
				orderLayerVector.remove(i);
				break;
			}
		}
		drawingServer.setTotalOrder(false);
	}

	public void addOrder(OrderI layer) {
		for (MultipleArrow arrow : super.getArrowList()) {
			for (SingleArrow single : arrow.getArrowList())
				layer.addLogicalOrder(single);
		}
	}

	public void setShowFifo() {
		drawingServer.setShowFifoVector();
	}

	public void unsetShowFifo() {
		drawingServer.unsetShowFifoVector();
	}

	@Override
	public void drawSimulation(Graphics2D g) {
		for (OrderI orderLayer : orderLayerVector) {
			orderLayer.draw(g);
		}
	}

	@Override
	public void simulate(SimulationView simulation) {
	}
}
