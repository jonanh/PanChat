package simulation.view.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import simulation.view.SimulationView;

public class ViewListener implements MouseListener, MouseMotionListener {

	SimulationView simulationView;

	public ViewListener(SimulationView simulationView) {
		this.simulationView = simulationView;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		simulationView.setPosition(simulationView.getPosition(e));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		simulationView.setPosition(null);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		simulationView.setPosition(simulationView.getPosition(e));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		simulationView.setPosition(simulationView.getPosition(e));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		simulationView.setPosition(simulationView.getPosition(e));
	}
}
