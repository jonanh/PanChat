package simulation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import panchat.data.User;

import simulation.arrows.MessageArrow;
import simulation.arrows.MultipleArrow;
import simulation.arrows.SingleArrow;
import simulation.view.CellPosition;
import simulation.view.Position;
import simulation.view.order.CausalOrderView;
import simulation.view.order.FifoOrderView;
import simulation.view.order.OrderDrawing;
import simulation.view.order.OrderI;
import simulation.view.order.TotalOrderView;

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

	// Lista de cortes (usando un bitset, para evitar usar un array de
	// booleanos. Es dinamico e internamente permite usar operaciones a nivel de
	// bit).
	private BitSet cutList = new BitSet();

	// Flechas
	// Lista de flechas
	private ArrayList<MultipleArrow> arrowList = new ArrayList<MultipleArrow>();

	// Matriz de flechas, donde CellPosition almacena (Proceso,Tick)
	private HashMap<CellPosition, MultipleArrow> arrowMatrix = new HashMap<CellPosition, MultipleArrow>();

	// Lista de procesos/usuarios
	private ArrayList<User> listaProcesos = new ArrayList<User>();

	// capa que se encarga de la ordenacion
	public Vector<OrderI> orderLayerVector = new Vector<OrderI>();
	public OrderDrawing drawingServer = new OrderDrawing(DEFAULT_NUM_PROCESSES);

	/**
	 * Construimos el objeto de datos de simulacion
	 */
	public SimulationModel() {
		setNumProcesses(DEFAULT_NUM_PROCESSES);
	}

	/*
	 * Métodos para obtener y establecer el número procesos y ticks.
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
			for (int i = getNumProcesses(); i < pNumProcesses; i++)
				listaProcesos.add(new User(null));

			changeProcessesOrderLayers();
			
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
				listaProcesos.remove(i - 1);
			}
			// se pide que se recalculen los vectores y se avisa a la capa del cambio de numero de procesos
			changeProcessesOrderLayers();
			
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
			this.cutList.clear(pTimeTicks + 1, numTicks);
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
	public synchronized void addArrow(MessageArrow messageArrow) {

		// Si la flecha es una MultipleArrow
		if (messageArrow instanceof MultipleArrow) {

			// FIXME lo ideal sería no tener que añadir una a una cada flecha
			// :-P

			// Añadir una a una cada SingleArrow
			MultipleArrow mArrow = (MultipleArrow) messageArrow;
			for (CellPosition pos : mArrow.getFinalPos())
				addArrow(mArrow.getArrow(pos));

			super.setChanged();
			this.notifyObservers();
		}
		// Si es una SingleArrow llamar al método con un cast
		else if (messageArrow instanceof SingleArrow)
			addArrow((SingleArrow) messageArrow);
	}

	/**
	 * 
	 * @param messageArrow
	 *            Añadimos esta fecla
	 */
	public synchronized void addArrow(SingleArrow messageArrow) {
		Vector<Boolean> correctness = new Vector<Boolean>();

		CellPosition initialPos = messageArrow.getInitialPos();
		CellPosition finalPos = messageArrow.getFinalPos();

		MultipleArrow arrow = getMultipleArrow(initialPos);

		// Si no existe el MultipleArrow, lo creamos y añadimos la flecha
		if (arrow == null) {

			arrow = new MultipleArrow(initialPos, messageArrow);

			// Añadimos la flecha al comienzo y a el final
			arrowMatrix.put(initialPos, arrow);
			arrowMatrix.put(finalPos, arrow);

			arrowList.add(arrow);

			if (ADD_DEBUG) {
				System.out.println();
				System.out.println("addMultipleArrow:" + arrow);
				System.out.println("estado flechas:" + arrowList);
				System.out.println();
				System.out.println("estado:" + arrowMatrix);
			}
			// FIXME
			// se introduce el correspondiente vector logico
			//correctness = fifo.addLogicalOrder(messageArrow, false);

		} // Añadimos la flecha
		else {
			CellPosition removeArrow = arrow.addArrow(messageArrow);

			// Si al añadir eliminamos una flecha que va al mismo proceso
			if (removeArrow != null) {
				arrowMatrix.remove(removeArrow);

				// FIXME
				//fifo.removeOnlyLogicalOrder(removeArrow);
			}

			// Añadimos la flecha al final
			arrowMatrix.put(finalPos, arrow);

			if (ADD_DEBUG) {
				System.out.println();
				System.out.println("addArrow:" + messageArrow);
				System.out.println("estado MultipleArrow:" + arrow);
				System.out.println("estado flechas:" + arrowList);
				System.out.println();
				System.out.println("estado:" + arrowMatrix);
			}

			// se introduce el correspondiente vector logico
			//correctness = fifo.addLogicalOrder(messageArrow, true);
		}
		//se introduce el correspondiente vector logico en todas las capas
		addLogicalOrderLayers(correctness,messageArrow);

		// FIXME
		// si no es correcto de acuerdo a algun orden establecido se borra
		if (isCorrectOrderLayers(correctness) == false) {
			deleteArrow(finalPos);
		}

		super.setChanged();
		this.notifyObservers();
	}

	public synchronized MultipleArrow getMultipleArrow(CellPosition position) {
		return this.arrowMatrix.get(position);
	}

	public synchronized MessageArrow getArrow(Position position) {

		if (!(position instanceof CellPosition))
			return null;

		CellPosition pos = (CellPosition) position;

		MultipleArrow arrow = getMultipleArrow(pos);

		// Si no existe la flecha devolvemos null
		if (arrow == null)
			return null;

		// Si la posicion era la flecha inicial devolvemos todo el
		// MultipleArrow
		if (arrow.getInitialPos().equals(pos))
			return arrow;

		// Sino devolvemos la SingleArrow del destino
		else
			return arrow.getArrow(pos);
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
	public synchronized MessageArrow deleteArrow(CellPosition position) {
		MessageArrow arrow;

		MultipleArrow multipleArrow = arrowMatrix.remove(position);

		if (multipleArrow == null)
			return null;

		// Si la posicion es la posicion inicial debemos borrar además
		// las referencias desde los nodos finales.
		if (multipleArrow.getInitialPos().equals(position)) {

			for (CellPosition pos : multipleArrow.getFinalPos()) {
				arrowMatrix.remove(pos);

				// se borran los relojes correspondientes
				// FIXME
				// System.out.println("por aqui no paso");
				//fifo.removeOnlyLogicalOrder(pos);
			}
			//fifo.removeOriginLogicalOrder(multipleArrow.getInitialPos());
			/*
			 * al ser la posicion inicial, se debe borrar la posicion inicial del vector
			 * en todas las capas
			 */
			removeInitialOrderLayers(position);
			
			if (REMOVE_DEBUG) {
				System.out.println();
				System.out.println("deleteArrow (inicial):" + position);
				System.out.println();
				System.out.println("estado:" + arrowMatrix);
			}

			arrow = multipleArrow;

			arrowList.remove(multipleArrow);
		}
		// Si la posicion es la posicion de destino de una flecha entonces
		// eliminamos dicha flecha de la MultipleArrow
		else {
			arrow = multipleArrow.deleteArrow(position);

			// Si hemos borrado la ultima flecha del grupo, borrar también
			// el MultiArrow
			if (multipleArrow.getFinalPos().size() == 0) {
				arrowMatrix.remove(multipleArrow.getInitialPos());
				arrowList.remove(multipleArrow);
				//fifo.removeLogicalOrder(position);
			} //else {
				// se borran los relojes correspondientes
				// FIXME
				// System.out.println("eliminando");
				//fifo.removeOnlyLogicalOrder(position);
			//}
				
			//se borra la posicion de destino de todas las capas, 
			//si es el ultimo vector se borra tambien el inicial
				removeFinalOrderLayers(position);

			if (REMOVE_DEBUG) {
				System.out.println();
				System.out.println("deleteArrow (final):" + position);
				System.out.println();
				System.out.println("estado:" + arrowMatrix);
			}
		}
		
		//se recalcula para todas las capas
		recalculateVectorsOrderLayers(multipleArrow.getInitialPos().tick-1);
		
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
			for (CellPosition arrowCellPosition : arrow.getFinalPos()) {

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
	 * 
	 * @param tick
	 * 
	 * @return Si el tick es un corte o no.
	 */
	public boolean isCut(int tick) {
		return cutList.get(tick);
	}

	/**
	 * Devuelve el usuario correspondiente a un proceso determinado.
	 * 
	 * @param process
	 * @return
	 */
	public User getUser(int process) {
		return listaProcesos.get(process);
	}

	/**
	 * Rutina ayudante para setNumProcesses y setTimeTicks. Busca en las
	 * flechas, el proceso mÃ¡s lejano desde el cual salga o llegue una flecha,
	 * y el tick mÃ¡s lejano hasta donde llegue una flecha.
	 * 
	 * @return
	 */
	public CellPosition freeCell(CellPosition cell) {

		CellPosition freeCell = cell.clone();

		// Recorremos los ticks desde la posicion donde estamos buscando espacio
		// libres
		for (; freeCell.tick < this.getTimeTicks(); freeCell.tick++)
			if (!arrowMatrix.containsKey(freeCell))
				return freeCell;

		return null;
	}
	
	public void changeProcessesOrderLayers(){
		for(OrderI orderLayer:orderLayerVector){
			orderLayer.setNumProcessChanged();
			orderLayer.recalculateVectors(0);
		}
	}
	
	public void addLogicalOrderLayers(Vector<Boolean> correctness,SingleArrow messageArrow){
		for(OrderI orderLayer:orderLayerVector){
			correctness.add(orderLayer.addLogicalOrder(messageArrow));
		}
	}
	
	public boolean isCorrectOrderLayers(Vector<Boolean> correctness)
	{
		//sera correcto si todas las componentes del vector son correctas
		boolean correct = true;
		for(Boolean bool:correctness){
			correct = correct & bool;
		}
		return correct;
	}
	
	public void removeInitialOrderLayers (CellPosition initPos){
		for(OrderI orderLayer:orderLayerVector){
			orderLayer.removeInitialOrder(initPos);
		}
	}
	
	public void removeFinalOrderLayers (CellPosition finalPos){
		for(OrderI orderLayer:orderLayerVector){
			orderLayer.removeFinalOrder(finalPos);
		}
	}
	
	public void recalculateVectorsOrderLayers(int tick){
		for(OrderI orderLayer:orderLayerVector){
			orderLayer.recalculateVectors(tick);
		}
	}
	public void addFifoLayer (){
		FifoOrderView fifo = new FifoOrderView(this);
		
		//hay que aniadir aquellas flechas que ya existen
		addOrder(fifo);
		orderLayerVector.add(fifo);
		drawingServer.setFifoOrder(true);
	}
	public void addCausalLayer (){
		CausalOrderView causal = new CausalOrderView(this);
		addOrder(causal);
		orderLayerVector.add(causal);
		drawingServer.setCausalOrder(true);
	}
	
	public void addTotalLayer(){
		//no se debe permitir tener fifo, no causal y total
		TotalOrderView total = new TotalOrderView(this);
		int size = orderLayerVector.size();
		boolean isFifo = false;
		boolean isCausal = false;
		
		for(int i = 0; i< size; i++){
			if(orderLayerVector.get(i) instanceof FifoOrderView)
				isFifo = true;
			else if(orderLayerVector.get(i) instanceof CausalOrderView)
				isCausal = true;
		}
		
		//si no hay orden fifo o hay causal, se inserta la capa
		if (!isFifo || isCausal){
			addOrder(total);
			orderLayerVector.add(total);
			drawingServer.setTotalOrder(true);
		}
	}
	
	public void removeFifoLayer(){
		int size = orderLayerVector.size();
		for(int i = 0;i < size; i++){
			if(orderLayerVector.get(i) instanceof FifoOrderView){
				orderLayerVector.remove(i);
				break;
			}
		}
		drawingServer.unsetShowFifoVector();
		drawingServer.setFifoOrder(false);
	}
	
	public void removeCausalLayer(){
		int size = orderLayerVector.size();
		for(int i = 0;i < size; i++){
			if(orderLayerVector.get(i) instanceof CausalOrderView){
				orderLayerVector.remove(i);
				break;
			}
		}
		drawingServer.setCausalOrder(false);
	}
	
	public void removeTotalLayer(){
		int size = orderLayerVector.size();
		for(int i = 0;i < size; i++){
			if(orderLayerVector.get(i) instanceof TotalOrderView){
				orderLayerVector.remove(i);
				break;
			}
		}
		drawingServer.setTotalOrder(false);
	}
	
	public void addOrder (OrderI layer){
		for(MultipleArrow arrow:arrowList){
			for(SingleArrow single:arrow.getArrowList())
				layer.addLogicalOrder(single);
		}
	}
	
	public void setShowFifo(){
		drawingServer.setShowFifoVector();
	}
	public void unsetShowFifo(){
		drawingServer.unsetShowFifoVector();
	}
	/*public void addFifoLayer (){
		orderLayerVector.add(new FifoOrderView(this));
	}*/
}
