package simulation.arrows;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import order.Message.Type;

import simulation.model.SimulationArrowModel;
import simulation.view.CellPosition;

public class TotalArrow extends MultipleArrow implements Serializable, Observer {

	private static final long serialVersionUID = 1L;

	/*
	 * Flechas para cada una de las fases
	 */
	public class SendArrow extends SingleArrow {
		private static final long serialVersionUID = 1L;

		public SendArrow(CellPosition initialPos, CellPosition finalPos) {
			super(initialPos, finalPos, Type.FIFO, Type.TOTAL);
		}
	}

	public class ProposalArrow extends SingleArrow {
		private static final long serialVersionUID = 1L;

		public ProposalArrow(CellPosition initialPos, CellPosition finalPos) {
			super(initialPos, finalPos, Type.FIFO, Type.TOTAL);
		}
	}

	public class FinalArrow extends SingleArrow {
		private static final long serialVersionUID = 1L;

		public FinalArrow(CellPosition initialPos, CellPosition finalPos) {
			super(initialPos, finalPos, Type.FIFO, Type.TOTAL);
		}
	}

	/*
	 * Atributos
	 */
	private SimulationArrowModel simulationModel;

	private int numProcesses = 1;

	/*
	 * Posición central, cuando todos los procesos han respondido con sus
	 * propuestas.
	 */
	private CellPosition proposalPosition;

	/*
	 * Listas de cada fase : envio, propuesta, confirmación.
	 */
	private List<SendArrow> sendArrows = new LinkedList<SendArrow>();

	private List<ProposalArrow> proposalArrows = new LinkedList<ProposalArrow>();

	private List<FinalArrow> finalArrows = new LinkedList<FinalArrow>();

	public TotalArrow(CellPosition initialPos,
			SimulationArrowModel simulationModel) {
		this(initialPos, simulationModel, true);
	}

	// Used only for cloning
	private TotalArrow(CellPosition initialPos,
			SimulationArrowModel simulationModel, boolean recalculate) {

		super(initialPos);
		this.proposalPosition = initialPos;

		// Añadimos la posición inicial a la lista de posiciones.
		this.positionList.add(this.initialPos);

		this.simulationModel = simulationModel;
		this.numProcesses = simulationModel.getNumProcesses();

		// Las flechas totales observan el SimulationModel, ya que si la
		// aumentamos el número de procesos debemos recalcular la flecha.
		simulationModel.addObserver(this);

		if (recalculate)
			recalculate();
	}

	/**
	 * Recalcular
	 */
	public void recalculate() {

		// Crearemos las flechas de envio, buscando flechas libres desde la
		// posición inicial en cada proceso.

		int size = simulationModel.getNumProcesses();

		// Creamos una flecha de envio a todos.
		for (int i = 0; i < size; i++) {

			// Para cada proceso != del proceso de origen
			if (i != initialPos.process) {

				CellPosition finalPos;

				// Obtenemos la flecha del conjunto de flechas de la primera
				// etapa (sendArrows) que va al proceso i.
				SingleArrow arrow = getArrow(sendArrows, i);

				// Comprobamos si existía una flecha de envío
				if (arrow == null) {

					// Obtenemos una posición libre en el proceso i
					finalPos = new CellPosition(i, initialPos.tick);
					finalPos = simulationModel.freeCell(finalPos);

					// Creamos la flecha de envio y la añadimos a la lista de :
					// - De flechas (MultipleArrow)
					// - De posiciones (MultipleArrow)
					// - De flechas de envio
					SendArrow arrow2 = new SendArrow(initialPos, finalPos);
					this.arrowList.add(arrow2);
					this.sendArrows.add(arrow2);
					this.positionList.add(finalPos);

					// Creamos la flecha de respuesta a la de envio
					CellPosition resp = new CellPosition(initialPos.process,
							finalPos.tick);

					// Obtenemos una posición libre para la respuesta
					resp = simulationModel.freeCell(resp);

					while (this.positionList.contains(resp)) {
						resp = simulationModel.freeCell(resp);
					}

					ProposalArrow arrow3 = new ProposalArrow(finalPos, resp);
					this.arrowList.add(arrow3);
					this.proposalArrows.add(arrow3);
					this.positionList.add(resp);

					finalPos = resp;

				}
				// Si ya existía una flecha, debemos comprobar si sigue siendo
				// correcta.
				else {

					// Si la flecha es ahora inválida (va hacia atrás) o la
					// posición final está ahora ocupada, entonces debemos
					// buscar una nueva posición libre.

					if (!arrow.isValid(simulationModel, this)) {

						// Creamos las flechas de respuesta a las de envio
						CellPosition resp = new CellPosition(i,
								arrow.getFinalPos().tick);
						resp = simulationModel.freeCell(resp);

						arrow.getFinalPos().set(resp);
					}

					SingleArrow arrow2 = getArrow(proposalArrows, i);

					// Ahora debemos hacer que la flecha de propuesta apunte
					// hacía una posición vacía y debemos comprobar que ninguna
					// otra flecha esté ocupando dicha posición.

					CellPosition resp = new CellPosition(initialPos.process,
							arrow2.getFinalPos().tick);

					// Buscar una nueva posición vacía mientras exista otra
					// flecha de propuesta apuntnaod a esta posición.
					boolean repeat = true;
					while (repeat) {
						repeat = false;
						for (Arrow arrow3 : this.proposalArrows) {
							if (arrow3 != arrow2
									&& arrow3.getFinalPos().equals(resp)) {
								resp = simulationModel.freeCell(resp);
								repeat = true;
							}
						}
					}
					arrow2.getFinalPos().set(resp);

					if (!arrow2.isValid(simulationModel, this)) {

						resp = simulationModel.freeCell(resp);

						arrow2.getFinalPos().set(resp);
					}
					finalPos = arrow2.getFinalPos();
				}

				// // Guardamos el tick máximo
				if (finalPos.tick > proposalPosition.tick)
					proposalPosition = finalPos;

			}
		}

		// Creamos las flechas finales.
		for (int i = 0; i < size; i++) {

			// Para cada proceso != del proceso de origen
			if (i != initialPos.process) {

				SingleArrow arrow = getArrow(finalArrows, i);

				CellPosition finalPos;

				if (arrow == null) {

					finalPos = new CellPosition(i, proposalPosition.tick);
					finalPos = simulationModel.freeCell(finalPos);

					FinalArrow arrow2 = new FinalArrow(proposalPosition,
							finalPos);

					this.arrowList.add(arrow2);
					this.positionList.add(finalPos);
					this.finalArrows.add(arrow2);

				} else {

					SingleArrow arrow2 = getArrow(finalArrows, i);

					arrow2.setInitialPos(proposalPosition);

					if (!arrow2.isValid(simulationModel, this)) {

						// Creamos las flechas de respuesta a las de envio
						finalPos = new CellPosition(i, proposalPosition.tick);
						finalPos = simulationModel.freeCell(finalPos);

						arrow2.getFinalPos().set(finalPos);
					}
				}
			}
		}
	}

	/**
	 * Verificamos si se encuentra en un lugar válido y/o libre :
	 * 
	 * <ul>
	 * <li>Una flecha no puede tener flechas que vayan hacia atrás.</li>
	 * <li>Una flecha no puede apuntar a una celda ya ocupada.</li>
	 * </ul>
	 * 
	 * @param messageArrow
	 * 
	 * @return Si es valida la flecha
	 */
	public boolean isValid(SimulationArrowModel simulationModel) {

		if (!super.isValid(simulationModel))
			return false;

		if (moveCell != null && simulationModel.getArrow(moveCell) != null)
			return false;

		return true;
	}

	@Override
	public boolean deleteArrow(CellPosition position) {

		this.simulationModel.deleteObserver(this);

		// No se puede borrar una flecha interior de una flecha Total
		return true;
	}

	@Override
	public void addArrow(MessageArrow arrow) {
		// No se puede añadir una flecha a una flecha Total
	}

	@Override
	public boolean add2Simulation(SimulationArrowModel simulationModel) {

		this.moveCell = null;

		simulationModel.addArrow(this);

		return true;
	}

	@Override
	public void move(CellPosition newPosition) {

		newPosition.process = moveCell.process;

		super.move(newPosition);

		recalculate();
	}

	@Override
	public Collection<CellPosition> getPositions() {

		return this.positionList;
	}

	/**
	 * Si cambian el número de procesos, debemos añadir nuevas flechas.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (numProcesses != simulationModel.getNumProcesses()) {

			simulationModel.deleteArrow(initialPos, false);
			numProcesses = simulationModel.getNumProcesses();

			recalculate();

			// El método isValid también actualiza las pendientes de las
			// flechas.
			this.isValid(simulationModel);

			simulationModel.addArrow(this);
		}
	}

	/*
	 * Funciones ayudantes
	 */

	/**
	 * @param lista
	 * 
	 * @param process
	 * 
	 * @return Obtenemos la flecha correspondiente a un proceso de una lista de
	 *         flechas.
	 */
	private static SingleArrow getArrow(List<? extends SingleArrow> lista,
			int process) {
		for (SingleArrow arrow : lista) {
			if (arrow instanceof SendArrow || arrow instanceof FinalArrow) {
				if (arrow.getFinalPos().process == process)
					return arrow;
			} else if (arrow.getInitialPos().process == process)
				return arrow;
		}
		return null;
	}

	@Override
	public MultipleArrow clone() {

		TotalArrow clone = new TotalArrow(this.initialPos.clone(),
				simulationModel, false);

		clone.properties = this.properties.clone();
		clone.proposalPosition = proposalPosition.clone();

		int size = simulationModel.getNumProcesses();

		// Creamos una flecha de envio a todos.
		for (int i = 0; i < size; i++) {

			// Para cada proceso != del proceso de origen
			if (i != initialPos.process) {
				// Get send arrow
				SingleArrow arrow = getArrow(sendArrows, i);

				// Clone objects
				CellPosition nPos = arrow.getFinalPos().clone();
				SendArrow nArrow = new SendArrow(this.initialPos, nPos);
				// Adds new elements
				clone.arrowList.add(nArrow);
				clone.sendArrows.add(nArrow);
				clone.positionList.add(nPos);

				// Get proposal arrow
				SingleArrow arrow2 = getArrow(proposalArrows, i);

				// Clone objects
				CellPosition nPos2 = arrow2.getFinalPos().clone();
				ProposalArrow nArrow2 = new ProposalArrow(nPos, nPos2);
				// Adds new elements
				clone.arrowList.add(nArrow2);
				clone.proposalArrows.add(nArrow2);
				clone.positionList.add(nPos2);

				// Get final arrow
				SingleArrow arrow3 = getArrow(finalArrows, i);

				// Clone objects
				CellPosition nPos3 = arrow3.getFinalPos().clone();
				FinalArrow nArrow3 = new FinalArrow(clone.proposalPosition,
						nPos3);
				// Adds new elements
				clone.arrowList.add(nArrow3);
				clone.finalArrows.add(nArrow3);
				clone.positionList.add(nPos3);
			}
		}

		return clone;
	}
}
