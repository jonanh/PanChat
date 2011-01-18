package simulation.view.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EnumMap;

import order.Message.Type;

import simulation.model.SimulationArrowModel;
import simulation.view.IPositionObserver;
import simulation.view.SimulationView;

public class ViewListener implements MouseListener, MouseMotionListener {

	protected SimulationView simulationView;
	protected SimulationArrowModel simulationModel;

	public ViewListener(SimulationView simulationView) {
		this.simulationView = simulationView;
		updateModel();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		simulationView.setPosition(simulationView.getPosition(e),
				IPositionObserver.Mode.Click);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Sirve para indicarle a la vista en que posicion está el cursor para
		// que ilumine la celda correspondiente.
		simulationView.setPosition(simulationView.getPosition(e),
				IPositionObserver.Mode.Over);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Hemos salido de la ventana, la posición es null.
		simulationView.setPosition(null, IPositionObserver.Mode.Over);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Sirve para indicarle a la vista en que posicion está el cursor para
		// que ilumine la celda correspondiente.
		simulationView.setPosition(simulationView.getPosition(e),
				IPositionObserver.Mode.Over);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Sirve para indicarle a la vista en que posicion está el cursor para
		// que ilumine la celda correspondiente.
		simulationView.setPosition(simulationView.getPosition(e),
				IPositionObserver.Mode.Over);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Sirve para indicarle a la vista en que posicion está el cursor para
		// que ilumine la celda correspondiente.
		simulationView.setPosition(simulationView.getPosition(e),
				IPositionObserver.Mode.Over);
	}

	/**
	 * Permite cambiar el model.
	 */
	public void updateModel() {
		this.simulationModel = simulationView.getSimulationModel();
	}

	public void setProperties(EnumMap<Type, Boolean> properties) {
		// TODO Auto-generated method stub
	}
}
