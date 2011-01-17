package panchat.simulation.view;

import java.awt.Graphics2D;

public interface ISimulator {

	/**
	 * Metodo llamado desde el simulationView después de redibujar
	 * 
	 * @param simulation
	 */
	public void simulate(SimulationView simulation);

	/**
	 * Dibuja la simulación en el contexto gráfico g
	 * 
	 * @param g
	 */
	public void drawSimulation(Graphics2D g);

}
