package simulation.arrows;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import simulation.model.SimulationArrowModel;
import simulation.view.CellPosition;

public class TotalArrow extends MultipleArrow implements Serializable, Observer {

	private static final long serialVersionUID = 1L;

	/*
	 * Flechas para cada una de las fases del
	 */
	public class SendArrow extends SingleArrow {
		private static final long serialVersionUID = 1L;

		public SendArrow(CellPosition initialPos, CellPosition finalPos) {
			super(initialPos, finalPos);
		}
	}

	public class ProposalArrow extends SingleArrow {
		private static final long serialVersionUID = 1L;

		public ProposalArrow(CellPosition initialPos, CellPosition finalPos) {
			super(initialPos, finalPos);
		}
	}

	public class FinalArrow extends SingleArrow {
		private static final long serialVersionUID = 1L;

		public FinalArrow(CellPosition initialPos, CellPosition finalPos) {
			super(initialPos, finalPos);
		}
	}

	/*
	 * Atributos
	 */
	private SimulationArrowModel simulationModel;

	/*
	 * Posición central, cuando todos los procesos han respondido con sus
	 * propuestas.
	 */
	private CellPosition proposalPosition = new CellPosition(-1, -1);

	/*
	 * Listas de cada fase : envio, propuesta, confirmación.
	 */
	private List<SendArrow> sendPositions = new LinkedList<SendArrow>();

	private List<ProposalArrow> proposalPositions = new LinkedList<ProposalArrow>();

	private List<FinalArrow> finalPositions = new LinkedList<FinalArrow>();

	public TotalArrow(CellPosition initialPos, SimulationArrowModel simulationModel) {

		super(initialPos);

		this.simulationModel = simulationModel;

		// Las flechas totales observan el SimulationModel, ya que si la
		// aumentamos el número de procesos debemos recalcular la flecha.
		// simulationModel.addObserver(this);

		recalculate();
	}

	/**
	 * @param lista
	 * 
	 * @param process
	 * 
	 * @return Obtenemos la flecha correspondiente a un proceso de una lista de
	 *         flechas.
	 */
	public SingleArrow getArrow(List<? extends SingleArrow> lista, int process) {
		for (SingleArrow arrow : lista) {
			if (arrow.getFinalPos().process == process)
				return arrow;
		}
		return null;
	}

	/**
	 * Recalcular
	 */
	public void recalculate() {

		// Creamos las flechas de envio, buscando flechas libres desde la
		// posición actual en cada proceso.
		CellPosition iter = initialPos.clone();

		// Guardaremos el tick máximo de la última flecha de envio.
		int lastTick = 0;
		int size = simulationModel.getNumProcesses();

		// Creamos una flecha de envio a todos.
		for (int i = 0; i < size; i++) {

			// Para cada proceso != del proceso de origen
			if (i != initialPos.process) {

				SingleArrow arrow = getArrow(sendPositions, i);
				CellPosition finalPos;

				if (arrow == null || !arrow.isValid(simulationModel)) {

					if (arrow != null) {
						arrowList.remove(arrow);
						sendPositions.remove(arrow);
					}

					iter.process = i;
					finalPos = simulationModel.freeCell(iter);

					System.out.println(iter);
					System.out.println(finalPos);

					SendArrow arrow2 = new SendArrow(initialPos, finalPos);
					addArrow(arrow2);
					sendPositions.add(arrow2);
					System.out.println(arrow2);

				} else {
					finalPos = arrow.getFinalPos();
				}

				// Guardamos el tick máximo
				if (finalPos.tick > lastTick)
					lastTick = finalPos.tick;
			}
		}

		// Si la posición de propuesta es inválida, debemos buscar una nueva
		// posición libre.
		if (proposalPosition.tick < lastTick) {
			iter.process = initialPos.process;
			iter.tick = lastTick;

			// Creamos la posición de propuesta
			proposalPosition.set(simulationModel.freeCell(iter));
		}

		// Creamos las flechas de propuesta de los procesos.
		for (int i = 0; i < size; i++) {

			// Para cada proceso != del proceso de origen
			if (i != initialPos.process) {

				SingleArrow arrow = getArrow(proposalPositions, i);

				CellPosition finalPos;

				if (arrow == null || !arrow.isValid(simulationModel)) {

					if (arrow != null) {
						arrowList.remove(arrow);
						proposalPositions.remove(arrow);
					}

					iter.process = i;
					finalPos = getArrow(sendPositions, i).getFinalPos();

					ProposalArrow arrow2 = new ProposalArrow(finalPos,
							proposalPosition);
					addArrow(arrow2);
					proposalPositions.add(arrow2);
				}
			}
		}

		iter = proposalPosition.clone();

		// Creamos las flechas finales.
		for (int i = 0; i < size; i++) {

			// Para cada proceso != del proceso de origen
			if (i != initialPos.process) {

				SingleArrow arrow = getArrow(finalPositions, i);

				CellPosition finalPos;

				if (arrow == null || !arrow.isValid(simulationModel)) {

					if (arrow != null) {
						arrowList.remove(arrow);
						finalPositions.remove(arrow);
					}

					iter.process = i;
					finalPos = simulationModel.freeCell(iter);

					FinalArrow arrow2 = new FinalArrow(proposalPosition,
							finalPos);
					addArrow(arrow2);
					finalPositions.add(arrow2);
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

		return true;
	}

	@Override
	public boolean deleteArrow(CellPosition position) {
		return true;
	}

	/**
	 * Si cambian el número de procesos, debemos añadir nuevas flechas.
	 */
	@Override
	public void update(Observable o, Object arg) {

		recalculate();
	}
}
