package simulation.arrows;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Clase Arrow
 * 
 * Esta permite dibujar flechas usando como base una Linea2D de Java.
 * 
 */
@SuppressWarnings("serial")
public class Arrow extends Line2D.Float implements Serializable {

	/*
	 * Constantes
	 */
	private transient static final Point2D.Float arrowPoint = new Point2D.Float(
			10, 5);

	private transient static Path2D.Float arrowPath;

	/*
	 * Optimizaciones
	 */
	private AffineTransform tx = new AffineTransform();
	private static transient BasicStroke stroke = new BasicStroke(2.0f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	/*
	 * Atributos
	 */
	private Color color = Color.black;

	/*
	 * Constructores
	 */
	public Arrow(Point2D p1, Point2D p2) {
		this(p1, p2, Color.black);
	}

	public Arrow(Point2D p1, Point2D p2, Color color) {
		super(p1, p2);
		this.color = color;
		inicializar();
	}

	public Arrow(float X1, float Y1, float X2, float Y2) {
		this(X1, Y1, X2, Y2, Color.black);
	}

	public Arrow(float X1, float Y1, float X2, float Y2, Color color) {
		super(X1, Y1, X2, Y2);
		this.color = color;
		inicializar();
	}

	public void inicializar() {
		if (arrowPath == null) {
			arrowPath = new Path2D.Float();
			arrowPath.moveTo(-arrowPoint.x, arrowPoint.y);
			arrowPath.lineTo(0.0f, 0.0f);
			arrowPath.lineTo(-arrowPoint.x, -arrowPoint.y);
		}
		actualizar();
	}

	/**
	 * Actualizamos la transformacion
	 */
	private void actualizar() {
		tx.setToIdentity();
		double angle = Math.atan2(y2 - y1, x2 - x1);
		tx.translate(x2, y2);
		tx.rotate(angle);
	}

	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(stroke);

		g.draw(this);

		// Intercambiamos las transformaciones
		AffineTransform txold = g.getTransform();
		g.setTransform(tx);

		g.draw(arrowPath);

		// Intercambiamos las transformaciones
		g.setTransform(txold);
	}

	@Override
	public void setLine(Line2D l) {
		super.setLine(l);
		actualizar();
	}

	@Override
	public void setLine(Point2D p1, Point2D p2) {
		super.setLine(p1, p2);
		actualizar();
	}
}
