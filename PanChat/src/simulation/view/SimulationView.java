package simulation.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import simulation.arrows.Arrow;
import simulation.arrows.MessageArrow;
import simulation.model.SimulationModel;
import simulation.view.listener.ViewListener;

@SuppressWarnings("serial")
public class SimulationView extends JComponent implements Observer {

	public static enum State {
		OVER, CREATE, MOVE, DELETE
	}

	/*
	 * Constantes
	 */

	// Constantes del tamaño de las celdas
	public static final int cellWidth = 40;
	public static final int cellHeight = 25;

	public static final int paddingX = 40;
	public static final int paddingY = 30;

	// Constantes de color
	private static final Color tickLineCol = Color.getHSBColor(.7f, .08f, .9f);
	private static final Color evenTickCol = Color.getHSBColor(.7f, .05f, 1f);
	private static final Color oddTickCol = Color.getHSBColor(.4f, .02f, 1f);
	private static final Color evenCutCol = Color.getHSBColor(.7f, .06f, .95f);
	private static final Color oddCutCol = Color.getHSBColor(.4f, .06f, .95f);

	private static final Color overCellColor = new Color(1f, 1f, 1f, .8f);
	private static final Color overColColor = new Color(0f, 0f, 0f, .04f);
	private static final Color invalidOverColor = new Color(1f, .6f, .6f);

	/*
	 * Atributos
	 */

	// El modelo de datos
	private SimulationModel simulationModel;

	// Numero de procesos y de ticks
	private int processes = SimulationModel.DEFAULT_NUM_PROCESSES;
	private int ticks = SimulationModel.DEFAULT_NUM_TICKS;

	// Dimensiones del tablero (en pixels)
	private int width = ticks * cellWidth + paddingX + 1;
	private int height = processes * (cellHeight + paddingY) + 1;

	// Atributo que usamos para guardar sobre que estamos (celda, corte o fuera
	// de la pantalla)
	private Position overPosition;

	// Flecha de dibujo
	private MessageArrow drawingArrow;


	/**
	 * Crea un nuevo tablero con las dimensiones establecidas por defecto.
	 */
	public SimulationView(SimulationModel simulationModel) {

		this.simulationModel = simulationModel;
		this.simulationModel.addObserver(this);

		this.setState(State.OVER);

		update();
	}

	public void setState(State state) {
		ViewListener listener = new ViewListener(this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
	}

	/**
	 * Calcular el indice de la celda en funcion de la posicion del cursor
	 */
	public Position getPosition(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();

		// Calculamos donde cae dentro de una fila (donde una fila es el espacio
		// de un proceso y el espacio que tiene justo antes de el)
		int fila = y % (paddingY + cellHeight);

		// columna, que es la columna una vez hemos restado el espacio de la
		// izquierda
		int columna = (x - paddingX) / cellWidth;

		// // Si está en el margen izquierdo null
		if (x < paddingX || columna > ticks || y > height)
			return null;

		// Sino, puede ser o bien una celda o una columna.
		if (fila >= paddingY) {

			// Calculamos la fila
			fila = y / (paddingY + cellHeight);

			return new CellPosition(fila, columna);
		} // Estamos en una columna
		else
			return new CutPosition(columna);
	}

	/**
	 * Establecemos el position over
	 * 
	 * @param position
	 */
	public void setPosition(Position position) {
		if ((this.overPosition != null && position == null)
				|| (position != null && !position.equals(this.overPosition))) {
			this.overPosition = position;
			this.repaint();
		}
	}

	public static Point2D.Float PositionCoords(CellPosition position) {
		// Espacio de la izquierda + celda * tick + mitad celda
		int x = paddingX + position.tick * cellWidth + cellWidth / 2;

		// Espacio de arriba + celda * proceso + mitad celda
		int y = paddingY + position.process * cellHeight + cellHeight / 2;

		return new Point2D.Float(x, y);
	}

	/*
	 * Funciones de dibujo
	 */

	/**
	 * Dibujar el tablero.
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		paintColumns(g2);
		paintProcesses(g2);
		paintArrows(g);
	}

	/**
	 * Dibujar las lineas verticales del tablero.
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	private void paintColumns(Graphics2D g) {

		int x0, x1;
		int y0 = 0;
		int y1 = height;
		x1 = cellWidth;

		boolean even = true;

		for (int i = 0; i <= ticks; i++) {
			x0 = i * cellWidth + paddingX;

			if (!simulationModel.isCut(i)) {
				g.setColor(even ? evenTickCol : oddTickCol);
			} else {
				g.setColor(even ? evenCutCol : oddCutCol);
			}
			even = !even;
			g.fillRect(x0, y0, x1, y1);
			g.setPaint(tickLineCol);
			g.drawRect(x0, y0, x1, y1);
		}

		if (overPosition instanceof CutPosition) {

			CutPosition pos = (CutPosition) overPosition;
			x0 = pos.tick * cellWidth + paddingX;
			g.setColor(overColColor);
			g.fillRect(x0, y0, x1, y1);

		}
	}

	/**
	 * Dibujar las lineas horizontales del tablero.
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	private void paintProcesses(Graphics g) {

		int x1 = cellWidth;
		int y1 = cellHeight;

		for (int i = 0; i <= processes; i++) {
			int y0 = i * (cellHeight + paddingY) + paddingY;

			for (int j = 0; j <= ticks; j++) {
				int x0 = paddingX + cellWidth * j;

				g.setColor(Color.getHSBColor(i * .10f % 1, .20f, 1f));
				g.fillRect(x0, y0, x1, y1);
				g.setColor(Color.getHSBColor(i * .10f % 1, .50f, .6f));
				g.drawRect(x0, y0, x1, y1);
			}
		}

		if (overPosition instanceof CellPosition) {

			CellPosition pos = (CellPosition) overPosition;
			int x0 = pos.tick * cellWidth + paddingX;
			int y0 = pos.process * (cellHeight + paddingY) + paddingY;

			// Si estamos dibujando una flecha, la posicion final de la flecha
			// es la misma que la celda iluminada y la flecha es esta siendo
			// colocada sobre una fila no valida.
			if (drawingArrow != null && drawingArrow.getFinalPos().equals(pos)
					&& !simulationModel.isValidArrow(drawingArrow))
				g.setColor(invalidOverColor);
			else
				g.setColor(overCellColor);

			g.fillRect(x0 + 1, y0 + 1, x1 - 1, y1 - 1);

		}
	}

	/**
	 * Dibuja las flechas
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	private void paintArrows(Graphics g) {
		for (Arrow flecha : simulationModel.getArrowList()) {
			flecha.draw((Graphics2D) g);
		}
	}

	/**
	 * Dibuja el simbolo de snapshot
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	public void drawSnapshotPoint(Graphics g) {
		// Dibujamos la celda donde comienza el snapshot con otro color
	}

	/*
	 * Getters y setters
	 */

	/**
	 * Devuelve la clase modelo
	 */
	public SimulationModel getSimulationModel() {
		return this.simulationModel;
	}

	/**
	 * Cambia el modelo (patron MVC)
	 * 
	 * @param simulationModel
	 */
	public void setSimulationModel(SimulationModel simulationModel) {
		this.simulationModel = simulationModel;
		update();
	}

	/*
	 * Patron MVC
	 */

	/**
	 * Actualizar la vista cuando se actualiza el modelo
	 */
	@Override
	public void update(Observable o, Object arg) {
		update();
	}

	public void update() {
		ticks = simulationModel.getTimeTicks();
		processes = simulationModel.getNumProcesses();

		width = (ticks + 1) * cellWidth + 2 * paddingX;
		height = (processes + 1) * (cellHeight + paddingY) + paddingY;

		this.setPreferredSize(new Dimension(width, height));
		this.repaint();
	}
}