package simulation.arrows;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import simulation.view.CellPosition;
import simulation.view.SimulationView;

/**
 * Clase Arrow
 * 
 * Esta permite dibujar flechas usando como base una Linea2D de Java.
 * 
 */
@SuppressWarnings("serial")
public class Arrow implements Serializable {

	// Path que dibuja la cabeza
	private static Path2D.Float arrowHeadPath;

	// Punto que determina la forma de la cabeza de la flecha. El punto
	// representa el offset x e y respecto del final de la linea.
	private static final Point2D.Float arrowPoint = new Point2D.Float(10, 5);

	// Color por defecto de flecha inválida
	private static final Color defaultColor = new Color(.3f, .3f, 1f, .6f);
	private static final Color redColor = new Color(1f, 0f, 0f, .6f);

	// Pincel por defecto de una flecha.
	private static final BasicStroke defaultStroke = new BasicStroke(2.0f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	/*
	 * Atributos
	 */

	protected CellPosition initialPos;
	protected CellPosition finalPos;
	protected Color color;
	protected float strokeWidth;
	protected boolean isValid = true;

	/*
	 * Atributos no persistentes
	 */
	private transient Path2D.Float arrowPath;
	private transient AffineTransform tx;
	private transient BasicStroke stroke;

	/*
	 * Constructores
	 */
	public Arrow(CellPosition initialPos, CellPosition finalPos) {
		this(initialPos, finalPos, Arrow.defaultColor);
	}

	public Arrow(CellPosition initialPos, CellPosition finalPos, Color color) {
		this(initialPos, finalPos, color, 2.0f);
	}

	public Arrow(CellPosition initialPos, CellPosition finalPos, Color color,
			float strokeWidth) {
		this.initialPos = initialPos;
		this.finalPos = finalPos;
		this.color = color;
		this.strokeWidth = strokeWidth;
		initialize();
	}

	/**
	 * @return the initialPos
	 */
	public CellPosition getInitialPos() {
		return this.initialPos;
	}

	/**
	 * @return the finalPos
	 */
	public CellPosition getFinalPos() {
		return this.finalPos;
	}

	public void setInitialPos(CellPosition pos) {
		this.initialPos = pos;
	}

	public void setFinalPos(CellPosition pos) {
		this.finalPos = pos;
	}

	/**
	 * Always treat de-serialization as a full-blown constructor, by validating
	 * the final state of the de-serialized object.
	 */
	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();
		initialize();
	}

	protected void initialize() {
		if (arrowHeadPath == null) {
			arrowHeadPath = new Path2D.Float();
			arrowHeadPath.moveTo(-arrowPoint.x, arrowPoint.y);
			arrowHeadPath.lineTo(0.0f, 0.0f);
			arrowHeadPath.lineTo(-arrowPoint.x, -arrowPoint.y);
		}

		this.arrowPath = new Path2D.Float();
		this.tx = new AffineTransform();

		if (this.strokeWidth == 2.0f) {
			this.stroke = Arrow.defaultStroke;
		} else {
			this.stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER);
		}

		update();
	}

	/**
	 * Actualizamos la transformacion
	 */
	protected void update() {

		Point2D.Float pos1 = SimulationView.PositionCoords(initialPos);
		Point2D.Float pos2 = SimulationView.PositionCoords(finalPos);

		if (!isValid || this.initialPos.process != this.finalPos.process) {

			arrowPath.reset();
			arrowPath.moveTo(pos1.x, pos1.y);
			arrowPath.lineTo(pos2.x, pos2.y);

			double angle = Math.atan2(pos2.y - pos1.y, pos2.x - pos1.x);
			tx.setToIdentity();
			tx.translate(pos2.x, pos2.y);
			tx.rotate(angle);

		}
		// Si estamos dibujando una flecha en un mismo proceso, dibujar una
		// flecha bezier cuadrática para que no se superpongan.
		else {

			double dist = pos2.x - pos1.x;
			double distx = dist * .25;
			double disty = dist * .16;
			double px1 = pos1.x + distx;
			double py1 = pos1.y - disty;
			double px2 = pos2.x - distx;

			arrowPath.reset();
			arrowPath.moveTo(pos1.x, pos2.y);
			arrowPath.curveTo(px1, py1, px2, py1, pos2.x, pos2.y);

			double angle = Math.atan2(pos2.y - py1, pos2.x - px2);
			tx.setToIdentity();
			tx.translate(pos2.x, pos2.y);
			tx.rotate(angle);

		}
	}

	public void draw(Graphics2D g) {
		draw(g, this.color);
	}

	public void draw(Graphics2D g, Color color) {

		// Copiamos la antigua transformación y el pincel
		AffineTransform tx_old = g.getTransform();
		Stroke stroke_old = g.getStroke();

		if (isValid)
			g.setColor(color);
		else
			g.setColor(redColor);

		g.getStroke();

		g.setStroke(stroke);

		g.draw(arrowPath);

		g.setTransform(tx);

		g.draw(arrowHeadPath);

		// Restauramos la antigua transformación y el antiguo pincel
		g.setTransform(tx_old);
		g.setStroke(stroke_old);
	}

	@Override
	public String toString() {
		return "Flecha[ " + initialPos + finalPos + " ]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Arrow) {
			Arrow arrow = (Arrow) obj;
			return this.initialPos.equals(arrow.initialPos)
					&& this.finalPos.equals(arrow.finalPos);
		}
		return false;
	}
}
