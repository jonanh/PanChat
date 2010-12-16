package panchat.simulation.arrows;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import panchat.simulation.model.SimulationModel;
import panchat.simulation.view.CellPosition;

public class TotalArrow extends MultipleArrow implements Serializable, Observer {

	private static final long serialVersionUID = 1L;

	/*
	 * Flechas
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
	private CellPosition proposalPosition;

	private List<SendArrow> sendPositions = new LinkedList<SendArrow>();

	private List<ProposalArrow> proposalPositions = new LinkedList<ProposalArrow>();

	private List<FinalArrow> finalPositions = new LinkedList<FinalArrow>();

	private SimulationModel simulationModel;

	public TotalArrow(CellPosition initialPos, SimulationModel simulationModel) {

		super(initialPos);

		this.simulationModel = simulationModel;

		// simulationModel.addObserver(this);

		// Creamos las flechas de envio, buscando flechas libres desde la
		// posición actual en cada proceso.
		CellPosition iter = initialPos.clone();

		// Guardaremos el tick máximo de la última flecha de envio.
		int lastTick = 0;
		int size = simulationModel.getNumProcesses();

		// Creamos una flecha de envio a todos.
		for (int i = 0; i < size; i++) {

			// Es una flecha multicast, debemos crear una flecha que vaya a cada
			// uno de los otros procesos.
			if (i != initialPos.process) {
				iter.process = i;
				CellPosition finalPos = simulationModel.freeCell(iter);

				// Guardamos el tick máximo
				if (finalPos.tick > lastTick)
					lastTick = finalPos.tick;

				SendArrow arrow = new SendArrow(this.initialPos, finalPos);
				addArrow(arrow);
				sendPositions.add(arrow);
			}
		}

		iter.process = initialPos.process;
		iter.tick = lastTick;

		// Creamos la posición de propuesta
		proposalPosition = simulationModel.freeCell(iter);

		// Creamos las flechas de propuesta de los procesos.
		for (SendArrow arrow : sendPositions) {

			ProposalArrow arrow2 = new ProposalArrow(arrow.finalPos,
					proposalPosition);
			addArrow(arrow2);
			proposalPositions.add(arrow2);
		}

		iter = proposalPosition.clone();

		// Creamos las flechas finales.
		for (int i = 0; i < size; i++) {

			if (i != initialPos.process) {
				iter.process = i;
				CellPosition finalPos = simulationModel.freeCell(iter);

				FinalArrow arrow = new FinalArrow(proposalPosition, finalPos);
				addArrow(arrow);
				finalPositions.add(arrow);
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
	public boolean isValid(SimulationModel simulationModel) {

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

		// TODO este algoritmo debería ser más robusto

		int oldProcesses = sendPositions.size();
		int newProcesses = simulationModel.getNumProcesses();
		int num = newProcesses - oldProcesses;
		if (num > 0) {
			for (int i = 0; i < num; i++) {

				CellPosition finalPos = new CellPosition(initialPos.tick + 1, i
						+ oldProcesses);

				CellPosition finalPos2 = new CellPosition(
						proposalPosition.tick + 1, i + oldProcesses);

				SendArrow arrow = new SendArrow(this.initialPos, finalPos);
				addArrow(arrow);
				sendPositions.add(arrow);

				ProposalArrow arrow2 = new ProposalArrow(finalPos,
						proposalPosition);
				addArrow(arrow2);
				proposalPositions.add(arrow2);

				FinalArrow arrow3 = new FinalArrow(proposalPosition, finalPos2);
				addArrow(arrow3);
				finalPositions.add(arrow3);
			}
		}
	}
}
