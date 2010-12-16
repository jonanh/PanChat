package panchat.simulation.view.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EnumMap;

import panchat.messages.Message.Type;
import panchat.simulation.model.SimulationModel;
import panchat.simulation.view.SimulationView;

public class ViewListener implements MouseListener, MouseMotionListener {

	protected SimulationView simulationView;
	protected SimulationModel simulationModel;

	public ViewListener(SimulationView simulationView) {
		this.simulationView = simulationView;
		updateModel();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Sirve para indicarle a la vista en que posicion está el cursor para
		// que ilumine la celda correspondiente.
		simulationView.setPosition(simulationView.getPosition(e));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Hemos salido de la ventana, la posición es null.
		simulationView.setPosition(null);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Sirve para indicarle a la vista en que posicion está el cursor para
		// que ilumine la celda correspondiente.
		simulationView.setPosition(simulationView.getPosition(e));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Sirve para indicarle a la vista en que posicion está el cursor para
		// que ilumine la celda correspondiente.
		simulationView.setPosition(simulationView.getPosition(e));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Sirve para indicarle a la vista en que posicion está el cursor para
		// que ilumine la celda correspondiente.
		simulationView.setPosition(simulationView.getPosition(e));
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
