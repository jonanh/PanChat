package simulation.order_static;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Vector;

import simulation.view.CellPosition;
import simulation.view.SimulationView;

public class TotalMessage implements VectorI, Serializable {

	private static final long serialVersionUID = 1L;

	public static final int X_PAINT_ORIGIN = 3;
	public static final int Y_PAINT_ORIGIN = 11;
	public static final int X_PAINT_FINAL = 6 * X_PAINT_ORIGIN;
	public static final int Y_PAINT_FINAL = SimulationView.cellHeight
			- Y_PAINT_ORIGIN;
	public static final int CHARATER_WIDTH = 4;
	public static final int SPECIAL_CHARATER_WIDTH = 2;
	public static final int EVEN_ELEVATION = 14;

	public static int numberMessages = 0;
	int id;

	CellPosition origin;
	Vector<CellPosition> finalPos;
	CellPosition drawingPos;

	// public static boolean print = false;

	// indica si este vector corresponde a origen o a destino
	boolean isOrigin;

	public TotalMessage() {
		id = numberMessages;
		finalPos = new Vector<CellPosition>();
	}

	public static void incrNumMsg() {
		numberMessages++;
	}

	public TotalMessage(CellPosition origin, CellPosition destiny,
			boolean isOrigin) {
		this();
		this.origin = origin;
		finalPos.add(destiny);
		this.isOrigin = isOrigin;

		/*
		 * si es origen la posicion de dibujo sera la posicion de origen si es
		 * destino la posicion e dibujo sera la posicion de destino
		 */
		if (this.isOrigin == true)
			drawingPos = origin;
		else
			drawingPos = destiny;
	}

	public void setUniqueFinalPos(CellPosition finalPos) {
		this.finalPos.clear();
		this.finalPos.add(finalPos);
	}

	public void draw(Graphics2D g) {
		int process = drawingPos.process;
		int tick = drawingPos.tick;
		String mens = "M" + String.valueOf(id);

		int xDraw;
		int yDraw;
		if (isOrigin) {
			xDraw = tick * SimulationView.cellWidth + SimulationView.paddingX
					+ X_PAINT_ORIGIN;
			yDraw = process
					* (SimulationView.cellHeight + SimulationView.paddingY)
					+ SimulationView.paddingY + Y_PAINT_ORIGIN;
		} else {
			xDraw = (tick + 1) * SimulationView.cellWidth
					+ SimulationView.paddingX - X_PAINT_FINAL;
			// -2 es para que no quede justo en la linea
			yDraw = process
					* (SimulationView.cellHeight + SimulationView.paddingY)
					+ SimulationView.paddingY + Y_PAINT_ORIGIN + Y_PAINT_FINAL
					- 2;
		}

		FontServer.boldFont(g);
		if (isOrigin)
			g.setColor(Color.BLUE);
		else
			g.setColor(Color.RED);

		g.drawString(mens, xDraw, yDraw);
	}

	public boolean isMultiple() {
		return finalPos.size() > 1;
	}

	@Override
	public String toString() {
		String s = new String();
		s = "[id: " + id + "]";
		return s;
	}

}
