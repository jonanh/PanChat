package simulation2;
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
	
	private GridPosition gridPos;

	public DrawingListener(InformationCanvas canvas) {
		this.canvas = canvas;

		// el atributo first sirve para diferenciar el momento en el que se
		// hace click por primera vez y el de arrastrar
		canvas.setFirst(true);

		// para no estar pintando todo el rato. Nos indica cu�ndo pintar
		paint = 0;
		
		gridPos = new GridPosition(canvas);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (canvas.getState() == InformationCanvas.State.EVENT) {
			if (canvas.isFirst() == true) {
				// se indica que ya se ha pinchado y se crea una nueva linea
				// con puntos de origen el lugar pinchado
				canvas.setFirst(false);
				//canvas.getFlechasMensajes().add(new Line(e.getX(), e.getY()));
				gridPos.calculateMiddleGrid(e.getX(),e.getY());
				canvas.getFlechasMensajes().add(new Line(gridPos.getReverseXGrid()
													, gridPos.getReverseYGrid()));
				
			} else {
				last = canvas.getFlechasMensajes().lastElement();
				gridPos.calculateMiddleGrid(e.getX(),e.getY());
				finX = gridPos.getReverseXGrid();// e.getX();
				finY = gridPos.getReverseYGrid();//e.getY();
				
				last.setFinalX(finX);
				last.setFinalY(finY);

				iniX = last.getInitX();
				iniY = last.getInitY();

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

					canvas.repaint();// iniX,iniY,finX-iniX,finY-iniY);
					paint = 0;
				}
			}
		} else if (canvas.getState() == InformationCanvas.State.MOVE) {
			if (canvas.isSelectedLine() == false) {
				canvas.locateLine(e.getX(), e.getY());
				canvas.setIsSelectedLine(true);
			} else {
				gridPos.calculateMiddleGrid(e.getX(),e.getY());
				canvas.getMovingLine().setFinalX(gridPos.getReverseXGrid());
				canvas.getMovingLine().setFinalY(gridPos.getReverseYGrid());
				canvas.repaint();
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (canvas.getState() == InformationCanvas.State.EVENT)
			canvas.terminarFlecha(canvas.getFlechasMensajes().lastElement());
		else if (canvas.getState() == InformationCanvas.State.MOVE) {
			gridPos.calculateMiddleGrid(arg0.getX(),arg0.getY());
			canvas.getMovingLine().setFinalX(gridPos.getReverseXGrid());
			canvas.getMovingLine().setFinalY(gridPos.getReverseYGrid());
			canvas.terminarFlecha(canvas.getMovingLine());
			canvas.setIsSelectedLine(false);
			canvas.setForcedValue(canvas.getMoveLineOrigin(), canvas
					.getMoveLineDestiny(), arg0.getX());
			
			canvas.recalculateSnapshot();
			canvas.repaint();
		}

		else if (canvas.getState() == InformationCanvas.State.SNAPSHOT) {
			canvas.setIsFixSnapshot(true);
			//canvas.setState(InformationCanvas.State.EVENT);
			gridPos.setXPos(arg0.getX());
			gridPos.setYPos(arg0.getY());
			gridPos.calculateGrid();
			canvas.setSnapshot(gridPos.getReverseXGrid(), gridPos.getReverseYGrid());
			canvas.startSnapshot();
			canvas.setState(InformationCanvas.State.EVENT);
			canvas.repaint();
		} else if (canvas.getState() == InformationCanvas.State.CUT) {
			canvas.setState(InformationCanvas.State.EVENT);
		}
	}

	public void mouseMoved(MouseEvent e) {
		canvas.gethotSpot(e.getX(), e.getY());
		if (canvas.getState() == InformationCanvas.State.SNAPSHOT) {
			gridPos.setXPos(e.getX());
			gridPos.setYPos(e.getY());
			gridPos.calculateGrid();
			canvas.setSnapshot(gridPos.getReverseXGrid(), gridPos.getReverseYGrid());
		} else if (canvas.getState() == InformationCanvas.State.CUT) {
			Line elemento = canvas.getCutLine().lastElement();
			gridPos.setXPos(e.getX());
			gridPos.setYPos(e.getY());
			gridPos.calculateGrid();
			elemento.setInitX(gridPos.getReverseXGrid());
			elemento.setFinalX(gridPos.getReverseXGrid());
			elemento.setInitY(canvas.getYLength() / 10);
			elemento.setFinalY(canvas.getYLength() - 10);
		}
		canvas.repaint();
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
