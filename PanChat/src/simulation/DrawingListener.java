package simulation;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.Vector;

public class DrawingListener implements MouseMotionListener, MouseListener {
	private int PINTAR = 7;

	private InformationCanvas canvas;

	private Line last;

	private int iniX;
	private int iniY;
	private int finX;
	private int finY;

	private int tempX;
	private int tempY;
	private int paint;

	public DrawingListener(InformationCanvas canvas) {
		this.canvas = canvas;

		// el atributo first sirve para diferenciar el momento en el que se
		// hace click por primera vez y el de arrastrar
		canvas.setFirst(true);

		// para no estar pintando todo el rato. Nos indica cu�ndo pintar
		paint = 0;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (canvas.getState() == InformationCanvas.EVENT) {
			if (canvas.isFirst() == true) {
				// se indica que ya se ha pinchado y se crea una nueva linea
				// con puntos de origen el lugar pinchado
				canvas.setFirst(false);
				canvas.getFlechasMensajes().add(new Line(e.getX(), e.getY()));

			} else {
				last = canvas.getFlechasMensajes().lastElement();
				finX = e.getX();
				finY = e.getY();

				iniX = last.getInitX();
				iniY = last.getInitY();

				last.setFinalX(finX);
				last.setFinalY(finY);

				if (paint != PINTAR)
					paint++;
				else {
					if (finX < iniX) {
						tempX = finX;
						finX = iniX;
						iniX = tempX;
					}
					if (finY < iniY) {
						tempY = finY;
						finY = iniY;
						iniY = tempY;
					}
					/*
					 * se dibuja solo la parte nueva Puede provocar problemas de
					 * dibujado si se empieza a mover la flecha. Se supone que
					 * esto no va a suceder.
					 */

					canvas.repaint(iniX, iniY, finX - iniX, finY - iniY);
					paint = 0;
				}
			}
		} else if (canvas.getState() == InformationCanvas.MOVE) {
			if (canvas.isSelectedLine() == false) {
				canvas.locateLine(e.getX(), e.getY());
				canvas.setIsSelectedLine(true);
			} else {
				canvas.getMovingLine().setFinalX(e.getX());
				canvas.getMovingLine().setFinalY(e.getY());
				canvas.repaint();
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (canvas.getState() == InformationCanvas.EVENT)
			canvas.terminarFlecha(canvas.getFlechasMensajes().lastElement());
		else if (canvas.getState() == InformationCanvas.MOVE) {
			canvas.getMovingLine().setFinalX(arg0.getX());
			canvas.getMovingLine().setFinalY(arg0.getY());
			canvas.terminarFlecha(canvas.getMovingLine());
			canvas.setIsSelectedLine(false);
			canvas.setForcedValue(canvas.getMoveLineOrigin(), canvas
					.getMoveLineDestiny(), arg0.getX());
			canvas.recalculateSnapshot();
			canvas.repaint();
		}

		else if (canvas.getState() == InformationCanvas.SNAPSHOT) {
			canvas.setIsFixSnapshot(true);
			canvas.setState(InformationCanvas.EVENT);
			canvas.setSnapshot(arg0.getX(), arg0.getY());
			canvas.startSnapshot();
			canvas.repaint();
		} else if (canvas.getState() == InformationCanvas.CUT) {
			canvas.setState(InformationCanvas.EVENT);
		}
	}

	/*
	 * public void terminarFlechar (MouseEvent arg0){ int factor; int
	 * longX,longY,longFlechaX,longFlechaY; double alfa,beta; double
	 * sinBeta,cosBeta; Iterator<Line> process; Line element;
	 * canvas.setFirst(true);
	 * 
	 * //hay que calcular los puntos de la flecha last =
	 * canvas.getFlechasMensajes().lastElement(); iniX = last.getInitX(); iniY =
	 * last.getInitY(); finX = last.getFinalX(); finY = last.getFinalY();
	 * 
	 * factor = 1;
	 * 
	 * 
	 * 
	 * //y_0 - y_1 longY=iniY-finY;
	 * 
	 * //x_1 - x_0 longX=finX-iniX;
	 * 
	 * alfa = Math.atan((double)longY/(double)longX); if (iniY<finY){ alfa =-
	 * alfa; factor = -1; }
	 * 
	 * //2pi rad = 360�, para pasar grados a radianes, multiplicar por 0.01745
	 * beta = alfa - (last.getAngle()*0.01745);
	 * 
	 * sinBeta = Math.sin(beta); cosBeta = Math.cos(beta);
	 * 
	 * longFlechaX = last.getLengthX(); longFlechaY = last.getLengthY();
	 * 
	 * last.xFN = (int)(finX - cosBeta * longFlechaX); last.yFN = (int)(finY +
	 * factor*sinBeta * longFlechaY);
	 * 
	 * beta = Math.PI/2 - alfa - (last.getAngle()+10)*0.01745; sinBeta =
	 * Math.sin(beta); cosBeta = Math.cos(beta);
	 * 
	 * 
	 * last.xFS = (int)(finX - sinBeta * longFlechaX); last.yFS = (int)(finY +
	 * factor*cosBeta * longFlechaY);
	 * 
	 * process = canvas.getProcessLine().iterator();
	 * 
	 * while (process.hasNext()){ element = process.next();
	 * 
	 * if((iniY>(element.getInitY()-canvas.getGap()/2)) && (iniY <
	 * (element.getInitY()+canvas.getGap()/2))){ last.setColor(new
	 * Color(element.getColor().getRGB())); } } last.setArrow(true);
	 * canvas.repaint(); }
	 */

	public void mouseMoved(MouseEvent e) {
		if (canvas.getState() == InformationCanvas.SNAPSHOT) {
			canvas.setSnapshot(e.getX(), e.getY());
			canvas.repaint();
		} else if (canvas.getState() == InformationCanvas.CUT) {
			Line elemento = canvas.getCutLine().lastElement();
			elemento.setInitX(e.getX());
			elemento.setFinalX(e.getX());
			elemento.setInitY(canvas.getYLength() / 10);
			elemento.setFinalY(canvas.getYLength() - 10);
			canvas.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
