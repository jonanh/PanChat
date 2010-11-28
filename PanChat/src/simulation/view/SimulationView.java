package simulation.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import simulation.arrows.Arrow;
import simulation.model.SimulationModel;

@SuppressWarnings("serial")
public class SimulationView extends JComponent implements Observer {

	public static enum state {
		CUT, EVENT, START, MOVE
	}

	/*
	 * Numero de casillas del tablero
	 */
	private int processes = SimulationModel.DEFAULT_NUM_PROCESSES;
	private int ticks = SimulationModel.DEFAULT_NUM_TICKS;

	/*
	 * Dimensiones de cada casilla (en pixels)
	 */
	public static final int cellWidthPX = 40;
	public static final int cellHeightPX = 40;

	/*
	 * Dimensiones del tablero (en pixels)
	 */
	private int width = ticks * cellWidthPX + 1;
	private int height = processes * cellHeightPX + 1;

	/*
	 * Colores usados para el dibujo del tablero
	 */
	private static final Color backgroundColor = Color.yellow;
	private static final Color timeLineColor = Color.blue;
	private static final Color cellColor = Color.getHSBColor(10, 10, 10);
	private static final Color overCellColor = Color.getHSBColor(10, 10, 30);
	private static final Color invalidCellColor = Color.getHSBColor(10, 10, 10);

	/*
	 * Posicion del cursor (si esta situado)
	 */
	private int cursor_x = -1;
	private int cursor_y = -1;

	/*
	 * Variable para guardar si el cursor esta dentro o fuera del tablero
	 */
	private boolean over = false;

	ArrayList<Arrow> listaFlechas = new ArrayList<Arrow>();

	private int availableColors[][] = {
	//
			{ 255, 0, 0, 100 }, //
			{ 0, 255, 0, 100 }, //
			{ 0, 0, 255, 100 }, //
			{ 234, 184, 86, 100 }, //
			{ 0, 255, 255, 100 }, //
			{ 255, 0, 255, 100 }, //
			{ 255, 255, 0, 100 },// naranja
			{ 234, 19, 255, 100 },// rosa
			{ 30, 90, 200, 100 },// morado
	};

	/**
	 * Crea un nuevo tablero con las dimensiones establecidas por defecto.
	 */
	public SimulationView() {
		/*
		 * Para establecer las dimensiones de este componente en una ventana
		 * 
		 * Dimension es modificable!!!
		 */
		setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));

		this.addMouseListener();
	}

	/**
	 * Calcular el indice de la celda en funcion de la posicion del cursor
	 */
	private int calcularPosicion(MouseEvent e) {
		if (e.getY() < (height - 1) && e.getX() < (width - 1))
			return (e.getY() / cellHeightPX) * ticks + (e.getX() / cellWidthPX);
		else
			return -1;
	}

	/**
	 * Colocar el cursor en una posicion del tablero.
	 * 
	 * @param x
	 *            la abscisa de la posicion
	 * @param y
	 *            la ordenada de la posicion
	 */
	public void setCursorAt(int x, int y) {
		if (0 <= x && x < width && 0 <= y && y < height) {
			cursor_x = x;
			cursor_y = y;
		}
		/*
		 * Para provocar el redibujado del tablero
		 */
		super.repaint();
	}

	/*
	 * Funciones de pintar
	 */

	/**
	 * Dibujar el tablero.
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	@Override
	public void paint(Graphics g) {
		paintBackground(g);
		pintarLineasVerticales(g);
		pintarLineasHorizontales(g);

		if (over)
			drawOverCell(g);

		paintArrows(g);
	}

	/**
	 * Dibujar el fondo del tablero.
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	private void paintBackground(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * * Dibuja las lineas de tiempo
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	// public void paintTimeLine(Graphics g) {
	// Graphics2D g2 = (Graphics2D) g;
	// g2.setStroke(new BasicStroke(0.0f));
	// int y = yLength - 10;
	// int contadorX = xLength / 5;
	// int contadorY = yLength / 10;
	// int longX = xLength - xLength / 10;
	// int indice = 0;
	//
	// g.drawLine(contadorX, y, longX, y);
	//
	// g.setColor(timeLineColor);
	//
	// while (contadorX < longX) {
	// g.drawLine(contadorX, y, contadorX, contadorY);
	// g.drawString(String.valueOf(indice), contadorX + timeUnit / 4, y);
	// indice++;
	// contadorX += timeUnit;
	// }
	//
	// }

	/**
	 * Dibujar las lineas verticales del tablero.
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	private void pintarLineasVerticales(Graphics g) {
		g.setColor(timeLineColor);
		for (int i = 0; i <= ticks; i++) {
			int x0 = i * cellWidthPX;
			int y0 = 0;
			int x1 = x0;
			int y1 = processes * cellHeightPX;

			g.drawLine(x0, y0, x1, y1);
		}
	}

	/**
	 * Dibujar las lineas horizontales del tablero.
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	private void pintarLineasHorizontales(Graphics g) {
		g.setColor(timeLineColor);
		for (int i = 0; i <= processes; i++) {
			int x0 = 0;
			int y0 = i * cellHeightPX;
			int x1 = ticks * cellWidthPX;
			int y1 = y0;

			g.drawLine(x0, y0, x1, y1);
		}
	}

	/**
	 * Dibujar las lineas horizontales del tablero.
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	private void drawOverCell(Graphics g) {
		g.setColor(cellColor);
		int x0 = (cursor_x / cellWidthPX) * cellWidthPX + 1;
		int y0 = (cursor_y / cellHeightPX) * cellHeightPX + 1;
		int x1 = cellWidthPX - 1;
		int y1 = cellHeightPX - 1;

		g.fillRect(x0, y0, x1, y1);
	}

	/**
	 * Dibuja las flechas
	 * 
	 * @param g
	 *            el contexto grafico en el cual se pinta.
	 */
	private void paintArrows(Graphics g) {
		for (Arrow flecha : listaFlechas) {
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

	/**
	 * Actualizar la vista cuando se actualiza el modelo
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.repaint();
	}

}