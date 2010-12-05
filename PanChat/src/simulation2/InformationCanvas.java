package simulation2;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;

/* Esta clase es la encarga de responser a las acciones de usuario,
 * obteniendo la informaci�n del intercambio de mensajes y los eventos especiales
 * as� como de mostrar el resultado de la simulacion
 */
public class InformationCanvas extends Canvas {

	public static enum State {
		SNAPSHOT, CUT, EVENT, START, MOVE
	}

	private int TOL_X = 10;
	private int TOL_Y = 10;

	private int xLength, yLength;

	// indice el estado en el que nos encontramos para saber como debemos
	// dibujar
	private State state;

	private Vector<Line> flechasMensajes;
	private Vector<Line> processLine;
	private Vector<Line> cutLine;
	private Vector<Line> snapshotLine;

	private Iterator<Line> iterator;

	private DrawingListener handle;

	private int numProcess;
	private int timeUnit;

	private int snapshotX;
	private int snapshotY;
	private boolean isFixSnapshot;
	private boolean isCut;
	// para saber si al arrastrar hay que a�adir una nueva flecha
	private boolean first;

	// linea que se esta moviendo del snapshot
	private Line movingLine;
	private boolean isSelectedLine;
	private int forcedValues[][];

	private GridPosition hotSpot;

	// el gap es necesario conocerlo en la clase DrawingListener
	private int gap;
	private int processWidth;

	private int availableColor[][] = { { 255, 0, 0, 100 }, { 0, 255, 0, 100 },
			{ 0, 0, 255, 100 }, { 234, 184, 86, 100 }, { 0, 255, 255, 100 },
			{ 255, 0, 255, 100 }, { 255, 255, 0, 100 },// naranja
			{ 234, 19, 255, 100 },// rosa
			{ 30, 90, 200, 100 },// morado
	};

	// -------------------------------------------------------------------------------

	public InformationCanvas() {
		state = State.EVENT;

		timeUnit = 20;

		isCut = false;

		flechasMensajes = new Vector<Line>();
		processLine = new Vector<Line>();
		cutLine = new Vector<Line>();
		snapshotLine = new Vector<Line>();

		hotSpot = new GridPosition(this);
		handle = new DrawingListener(this);
		this.addMouseMotionListener((MouseMotionListener) handle);
		this.addMouseListener((MouseListener) handle);
	}

	public Vector<Line> getFlechasMensajes() {
		return flechasMensajes;
	}

	public Vector<Line> getProcessLine() {
		return processLine;
	}

	public Vector<Line> getCutLine() {
		return cutLine;
	}

	public State getState() {
		return state;
	}

	public int getGap() {
		return gap;
	}

	public int getYLength() {
		return yLength;
	}

	public int getXLength() {
		return xLength;
	}

	public int getTimeUnit() {
		return timeUnit;
	}

	public int getProcessWidth() {
		return processWidth;
	}

	public boolean getIsFixSnapshot() {
		return isFixSnapshot;
	}

	public boolean getIsCut() {
		return isCut;
	}

	public boolean isFirst() {
		return first;
	}

	public boolean isSelectedLine() {
		return isSelectedLine;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setNumProcess(int process) {
		numProcess = process;
		getLineProcess();
	}

	public void setTimeUnit(int unit) {
		timeUnit = unit;
	}

	public void setSnapshot(int x, int y) {
		snapshotX = x;
		snapshotY = y;
	}

	public void setIsFixSnapshot(boolean bool) {
		isFixSnapshot = bool;
	}

	public void setIsCut(boolean bool) {
		isCut = bool;
	}

	public void setFirst(boolean bool) {
		first = bool;
	}

	public void setFixSnapshot(boolean bool) {
		isFixSnapshot = bool;
	}

	public void setSnapshotEmpty() {
		snapshotLine = new Vector<Line>();
	}

	public Line getMovingLine() {
		return movingLine;
	}

	public void setIsSelectedLine(boolean bool) {
		isSelectedLine = bool;
	}

	public void setForcedValue(int x, int y, int value) {
		forcedValues[x][y] = value;
	}

	// metodo que permite el pintado del componente
	public void update(Graphics g) {
		paint(g);
	}

	public void newForcedValues(int dim) {
		forcedValues = new int[dim][dim];
	}

	public void paint(Graphics g) {
		float dash[] = { 10.0f };
		Rectangle bounds = g.getClipBounds();
		xLength = bounds.width;
		yLength = bounds.height;

		 BufferedImage imagen = new
		 BufferedImage(xLength,yLength,BufferedImage.TYPE_INT_ARGB);
		 

		dibujarFondo(g);

		dibujarFlechas(processLine, g);
		drawHotSpot(g);
		// dibujamos el borde de los procesos
		drawProcessBounds(g);
		dibujarFlechas(flechasMensajes, g);

		dashLine(g, 1.3f, dash);
		dibujarFlechas(snapshotLine, g);
		dibujarTimeLine(g);
		if (isCut == true)
			dibujarCorte(cutLine, g);
		dibujarSnapshot(g);

	}

	public void dibujarFondo(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, xLength, yLength);
		g.setColor(Color.BLACK);
	}

	public void dibujarFlechas(Vector<Line> flechasMensajes, Graphics g) {
		// se obtiene un iterador y se recorrer todos las flechas
		// entre procesos para dibujarlas

		iterator = flechasMensajes.iterator();
		Line next;
		int pid = 0;
		while (iterator.hasNext()) {
			next = iterator.next();

			if (next.isProcess()) {
				g.drawString("P" + pid++, next.getInitX() - 20,
						next.getInitY() + 10);
				g.setColor(next.getColor());
				g.fillRect(next.getInitX(), next.getInitY(), next.getFinalX(),
						next.getFinalY());
			} else {
				g.setColor(next.getColor());
				g.drawLine(next.getInitX(), next.getInitY(), next.getFinalX(),
						next.getFinalY());
				if (next.isArrow() == true) {
					g.drawLine(next.getFinalX(), next.getFinalY(), next.xFN,
							next.yFN);
					g.drawLine(next.getFinalX(), next.getFinalY(), next.xFS,
							next.yFS);
				}
			}
			g.setColor(Color.BLACK);
		}

	}
	
	public void dibujarCorte(Vector<Line> flechasMensajes, Graphics g) {
		iterator = flechasMensajes.iterator();
		Line next;
		while (iterator.hasNext()) {
			next = iterator.next();
			g.setColor(next.getColor());
			g.fillRect(next.getInitX(), next.getInitY(), timeUnit,yLength-yLength/5);
			g.setColor(Color.BLACK);
		}
	}

	// dibuja la linea de tiempo
	public void dibujarTimeLine(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(0.0f));
		int y = yLength - 10;
		int contadorX = xLength / 5;
		int contadorY = yLength / 10;
		int longX = xLength - xLength / 10;
		int indice = 0;

		g.drawLine(contadorX, y, longX, y);

		g.setColor(Color.GRAY);
		// dashLine(g,0.1f,(float)10.0f);

		while (contadorX < longX) {
			g.drawLine(contadorX, y, contadorX, contadorY);
			g.drawString(String.valueOf(indice), contadorX + timeUnit / 4, y);
			indice++;
			contadorX += timeUnit;
		}

	}

	public void dibujarSnapshot(Graphics g) {
		if (state == State.SNAPSHOT || isFixSnapshot == true) {
			g.setColor(Color.BLACK);
			g.fillOval(snapshotX, snapshotY, timeUnit, processWidth);
		}
	}

	// calcula las lineas de los procesos de tal manera que queden
	// equiespaciadas
	public void getLineProcess() {
		int startX;
		int startY;
		int indice;
		int valores[];

		processLine = new Vector<Line>();
		startY = yLength / 10;
		startX = xLength / 5;
		gap = 8 * yLength / (10 * (5 * numProcess) / 4);
		processWidth = gap / 4;

		for (indice = 0; indice < numProcess; indice++) {
			Line p = new Line(startX, startY);
			p.setFinalX(xLength - startX - xLength / 10);
			p.setFinalY(processWidth);
			p.setProcess(true);
			if (indice < 9) {
				valores = availableColor[indice];
				p.setColor(new Color(valores[0], valores[1], valores[2],
						valores[3]));

			} else
				p.setColor(new Color(xLength % 256, yLength % 256,
						startY % 256, 100));
			processLine.add(p);
			startY += gap + processWidth;
		}

		// dibujamos el cuadro de los procesos
	}

	public void drawProcessBounds(Graphics g) {
		if (numProcess != 0) {
			float grosor = 2.5f;
			Line elem;

			dashLine(g, grosor, null);
			Iterator<Line> it = processLine.iterator();

			while (it.hasNext()) {
				elem = it.next();
				g.drawRect(elem.getInitX(), elem.getInitY(), elem.getFinalX(),
						elem.getFinalY());
			}
			drawInterLine(g);
			dashLine(g, 1.0f, null);
		}
	}

	public void drawInterLine(Graphics g) {
		int initX = xLength / 5;
		int initY = yLength / 10;
		int yIndex;
		int xIndex;
		int xFinal;
		int yFinal;
		int lastY = yLength - gap;

		// hasta donde hay que llegar en x
		xFinal = xLength - xLength / 10;
		yIndex = initY;
		while (yIndex < lastY) {
			xIndex = initX;
			yFinal = yIndex + processWidth;
			while (xIndex < xFinal) {
				g.drawLine(xIndex, yIndex, xIndex, yFinal);
				xIndex += timeUnit;
			}
			yIndex += gap + processWidth;
		}
	}

	/*
	 * obtiene un objeto flecha con origen en el proceso especificado por el
	 * numero de proceso origin y final en end. Las posiciones horizontales las
	 * marcan el resto de parametros
	 */
	Line arrowTo(int origin, int end, int iniX, int finalX) {
		int iniY, finalY;
		int color[];
		GridPosition pos;
		pos = new GridPosition(this);
		pos.setXPos(iniX);
		
		Line line = new Line(iniX, 0);
		//line.setFinalX(finalX);

		iniY = yLength / 10 + origin * (gap + processWidth) + processWidth / 2;
		finalY = iniY + (end - origin) * (gap + processWidth);
		
		//valores de inicio de la flecha
		pos.calculateMiddleGrid(iniX,iniY);
		line.setInitX(pos.getReverseXGrid());
		line.setInitY(pos.getReverseYGrid());
		//line.setInitY(iniY);
		//line.setFinalY(finalY);
		
		//valores finales de la flecha
		pos.calculateMiddleGrid(finalX,finalY);
		line.setFinalX(pos.getReverseXGrid());
		line.setFinalY(pos.getReverseYGrid());

		line.setArrow(true);
		if (origin < 9)
			color = availableColor[origin];
		else
			color = new int[] { 0, 0, 0 };
		line.setColor(new Color(color[0], color[1], color[2]));
		terminarFlecha(line);

		return line;
	}

	int processId(int y) {
		Iterator<Line> process = processLine.iterator();
		Line element;
		int indice = 0;

		while (process.hasNext()) {
			element = process.next();

			if ((y > (element.getInitY() - gap / 2))
					&& (y < (element.getInitY() + gap / 2))) {
				return indice;
			}
			indice++;
		}
		return 0;
	}

	public void terminarFlecha(Line obj) {
		int factor;
		int longX, longY, longFlechaX, longFlechaY;
		double alfa, beta;
		double sinBeta, cosBeta;
		int iniX;
		int iniY;
		int finX;
		int finY;
		int processId;
		Iterator<Line> process;
		Line element;
		first = true;

		// hay que calcular los puntos de la flecha
		iniX = obj.getInitX();
		iniY = obj.getInitY();
		finX = obj.getFinalX();
		finY = obj.getFinalY();

		factor = 1;

		// y_0 - y_1
		longY = iniY - finY;

		// x_1 - x_0
		longX = finX - iniX;

		alfa = Math.atan((double) longY / (double) longX);
		if (iniY < finY) {
			alfa = -alfa;
			factor = -1;
		}

		// 2pi rad = 360�, para pasar grados a radianes, multiplicar por 0.01745
		beta = alfa - (obj.getAngle() * 0.01745);

		sinBeta = Math.sin(beta);
		cosBeta = Math.cos(beta);

		longFlechaX = obj.getLengthX();
		longFlechaY = obj.getLengthY();

		obj.xFN = (int) (finX - cosBeta * longFlechaX);
		obj.yFN = (int) (finY + factor * sinBeta * longFlechaY);

		beta = Math.PI / 2 - alfa - (obj.getAngle() + 10) * 0.01745;
		sinBeta = Math.sin(beta);
		cosBeta = Math.cos(beta);

		obj.xFS = (int) (finX - sinBeta * longFlechaX);
		obj.yFS = (int) (finY + factor * cosBeta * longFlechaY);

		processId = processId(iniY);
		obj.setColor(new Color(processLine.get(processId).getColor().getRGB()));

		obj.setArrow(true);
		repaint();
	}

	public void recalculateSnapshot() {
		snapshotLine = new Vector<Line>();
		startSnapshot();
	}

	public void startSnapshot() {
		// proceso que va a iniciar el snapshot
		int starter;
		int nextStarter;
		int dest;
		int mySnapshotX;
		// necesario para el caso en el que el deadline sea cero
		int mySnapshotXZ;
		boolean finished;
		int lastMessage[][] = new int[numProcess][numProcess];

		// con quien tiene dependencia el proceso i
		int dependency[] = new int[numProcess];
		boolean snapshotSend[] = new boolean[numProcess];
		int deadLine;
		int min;
		int start;
		Iterator<Line> messages;
		Line element;

		finished = false;
		nextStarter = 0;
		starter = processId(snapshotY);
		mySnapshotX = snapshotX;
		mySnapshotXZ = snapshotX + timeUnit;
		start = Integer.MAX_VALUE;

		for (int i = 0; i < numProcess; i++) {
			dependency[i] = -1;
		}

		// mientras todo el mundo no haya enviado un snapshot
		while (!finished) {
			// el proceso con id starter es aquel que en este momento esta
			// haciendo el snapshot
			// en la primera iteracion sera el iniciador
			// se obtienen todos los mensajes enviados por todos los procesos
			messages = flechasMensajes.iterator();

			// el bucle se encarga de comprobar cuando tiene que llegar mi
			// snapshot a cada otro
			// proceso
			while (messages.hasNext()) {
				element = messages.next();

				// si el mensajes lo he enviado yo y es enviado antes del
				// snapshot
				if (processId(element.getInitY()) == starter
						&& element.getInitX() < mySnapshotX) {
					// el destino solo puede recibir mi snapshot despues del
					// mensaje
					// el snapshot va "hacia delante", por eso la funcion max,
					// por si llega
					// el mensaje antes de que comience a enviar el snapshot
					dest = processId(element.getFinalY());
					deadLine = Math.max(element.getFinalX(), mySnapshotX
							+ timeUnit);

					// indicamos cuan pronto les puede llegar nuestro snapshot a
					// los demas
					if (lastMessage[starter][dest] < deadLine) {
						lastMessage[starter][dest] = deadLine;
						// nextStarter = dest;
						dependency[dest] = starter;
					}
				}
			}
			// se indica que ya se ha enviado el snapshot y que el proceso
			// starter no volvera
			// a enviarlo
			snapshotSend[starter] = true;
			min = Integer.MAX_VALUE;

			// obtenemos el valor final de tiempo de recepcion del snapshot
			for (int i = 0; i < numProcess; i++) {
				if (forcedValues[starter][i] <= 0) {
					// se aniade un retraso dependiente del proceso destino para
					// que quede
					// mas legible
					if (lastMessage[starter][i] == 0)
						lastMessage[starter][i] += (i + starter + 1) * timeUnit
								+ mySnapshotX;
					else
						lastMessage[starter][i] += (i + starter + 1) * timeUnit;
					// +Math.pow(2,i)
				}
				deadLine = lastMessage[starter][i];
				// como minimo tiene que ser el deadline que habia antes
				deadLine = Math.max(deadLine, forcedValues[starter][i]);
				if (i != starter) {
					// si el proceso es distinto del starter
					System.out.println("origen: " + starter + " destino: " + i
							+ " deadline: " + deadLine);
					if (deadLine != 0) {
						snapshotLine.add(arrowTo(starter, i, mySnapshotX,
								deadLine));
					} else {
						// si no se han enviado mensajes entre los procesos y no
						// hay valores
						// forzados,mi snapshot les llegara una unidad de tiempo
						// despues de
						// cuando envio
						deadLine = Math.max(mySnapshotX + timeUnit, deadLine);
						snapshotLine.add(arrowTo(starter, i, mySnapshotX,
								deadLine));
						// lastMessage[starter][i]=mySnapshotX+timeUnit;
					}
					// actualizamos el valor de recepcion con el nuevo deadLine
					lastMessage[starter][i] = deadLine;
				}
			}

			// hay que comprobar quien sera el siguiente en enviar los snapshot
			// enviara snapshot el que antes lo reciba
			for (int i = 0; i < numProcess; i++) {
				for (int j = 0; j < numProcess; j++) {
					if (snapshotSend[j] == false && i != j) {
						// si j tiene que esperar para lanzar un snapshot
						if (dependency[j] != -1 && (lastMessage[i][j] == 0)) {
							// System.out.println("pasando: "+j);
							start = lastMessage[dependency[j]][j];

						} else if (lastMessage[i][j] > 0) {
							System.out.println("pasando con valor de j: " + j);
							start = lastMessage[i][j];
						} else if (snapshotSend[i] == true) {
							// System.out.println("pasando con valor de j: "+j);
							start = lastMessage[i][j];
						}
						if (start < min) {
							// System.out.println("valor de min antes de asignar start: "+min);
							min = start;
							System.out.println("para origen: " + i
									+ " y destino: " + j);
							System.out
									.println("valor de min despues de asignar start: "
											+ min);
							nextStarter = j;
						}
					}
				}
			}
			// System.out.println("valor de min antes del if: "+min);
			if (min == Integer.MAX_VALUE)
				finished = true;
			else {
				starter = nextStarter;
				if (min != 0) {
					mySnapshotX = min;
					// System.out.println("min distinto de cero");
					// System.out.println("Valor de mysnapshotX: "+mySnapshotX);
				} else {
					// System.out.println("min igual de cero");
					mySnapshotX = mySnapshotXZ;
				}
				// System.out.println("Valor de mysnapshotX: "+mySnapshotX);
			}
		}
	}

	// localiza la linea de snapshot sobre la que se ha hecho click
	// se usa una determinada tolerancia
	public void locateLine(int x, int y) {
		int leftLimit;
		int rightLimit;
		int upLimit;
		int downLimit;

		leftLimit = x - TOL_X;
		rightLimit = x + TOL_X;
		upLimit = y - TOL_Y;
		downLimit = y + TOL_Y;

		Iterator<Line> it = snapshotLine.iterator();

		while (it.hasNext()) {
			movingLine = it.next();
			if (movingLine.getFinalX() > leftLimit
					&& movingLine.getFinalX() < rightLimit
					&& movingLine.getFinalY() > upLimit
					&& movingLine.getFinalY() < downLimit) {
				break;
			}
		}

	}

	public int getMoveLineOrigin() {
		return processId(movingLine.getInitY());
	}

	public int getMoveLineDestiny() {
		return processId(movingLine.getFinalY());
	}

	public void dashLine(Graphics g, float grosor, float[] dashLength) {
		// como poner las lineas punteadas se ha extra�do de
		// http://www.cidse.itcr.ac.cr/revistamate/HERRAmInternet/Graficador-Swing-java2D/node3.html
		Graphics2D g2;
		Stroke s = new BasicStroke(grosor, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 1.0f, dashLength, 0.0f);
		g2 = (Graphics2D) g;
		g2.setStroke(s);
	}

	// obtiene la posicion de la cuadricula y el punto x, y del hotspot
	public void gethotSpot(int x, int y) {
		hotSpot.setXPos(x);
		hotSpot.setYPos(y);
		hotSpot.calculateGrid();
		// System.out.print("posX: "+hotSpot.getXPos()+", posY: "+hotSpot.getYPos());
		// System.out.println(" GridX: "+hotSpot.getXGrid()+" ,GridY: "+hotSpot.getYGrid());
	}

	public void drawHotSpot(Graphics g) {

		g.setColor(Color.YELLOW);
		g.fillRect(hotSpot.getReverseXGrid(), hotSpot.getReverseYGrid(),
				timeUnit, processWidth);
		g.setColor(Color.BLACK);
	}

	public static void main(String[] args) {

		JFrame ventana = new JFrame("Simulaci�n paso de mensajes");
		ventana.add(new InformationCanvas());

		ventana.setVisible(true);
		ventana.setSize(300, 300);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/*
	 * Graphics2D g2= (Graphics2D)g; //grosor de las lineas g2.setStroke(new
	 * BasicStroke(1.0f));
	 */
}
