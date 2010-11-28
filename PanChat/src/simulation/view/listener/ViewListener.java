package simulation.view.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import simulation.model.SimulationModel;

public class ViewListener implements MouseListener, MouseMotionListener {

	SimulationModel simulationData;

	public ViewListener(SimulationModel simulationData) {
		this.simulationData = simulationData;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// clickeado(calcularPosicion(e));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// over = true;
		// setCursorAt(e.getX(), e.getY());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// over = false;
		// repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// clickeado(calcularPosicion(e));
		// setCursorAt(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// clickeado(calcularPosicion(e));
		// setCursorAt(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// setCursorAt(e.getX(), e.getY());
	}
}
