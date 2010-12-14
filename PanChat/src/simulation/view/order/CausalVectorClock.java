	package simulation.view.order;

	import java.awt.Color;
	import java.awt.FontMetrics;
	import java.awt.Graphics2D;
	import java.io.Serializable;
	import java.util.Arrays;
	import java.util.Vector;

	import simulation.view.CellPosition;
	import simulation.view.SimulationView;
	@SuppressWarnings("serial")
public class CausalVectorClock implements VectorI,Serializable {
		public static final int X_PAINT = 0;
		public static final int Y_PAINT = -2;
		public static final int CHARATER_WIDTH = 4;
		public static final int SPECIAL_CHARATER_WIDTH = 2;
		public static final int EVEN_ELEVATION = 14;

		CellPosition origin;
		Vector<CellPosition> finalPos;
		CellPosition drawingPos;

		// public static boolean print = false;

		// indica si este vector corresponde a origen o a destino
		boolean isOrigin;
		int vector[];

		public CausalVectorClock(int size) {
			vector = new int[size];
			finalPos = new Vector<CellPosition>();
		}

		public CausalVectorClock(CellPosition origin, CellPosition destiny,
				boolean isOrigin, int size) {
			this(size);
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
		
		public void setUniqueFinalPos(CellPosition finalPos){
			this.finalPos.clear();
			this.finalPos.add(finalPos);
		}

		public void incrPos(int i) {
			vector[i]++;
		}

		public void setVector(CausalVectorClock vector) {
			/*
			 * nos quedamos con el maximo de los dos vectores
			 */
			int[] vector2 = vector.vector;
			int limite = Math.min(this.vector.length, vector2.length);

			for (int i = 0; i < limite; i++)
				this.vector[i] = Math.max( this.vector[i],vector2[i]);
		}

		public void initialize() {
			/*
			 * se inicializa el vector 
			 */
			for (int i = 0; i < vector.length; i++)
				this.vector[i] = 0;
		}

		public boolean isCorrect(CausalVectorClock vectorClock) {
			// se comprueba si el vector es correcto
			boolean correctness = false;
			int[] vector = vectorClock.vector;

			/*
			 * un vector es correcto si no es estrictamente menor en todas las componentes
			 * con respecto al anterior
			 */
			for (int i = 0; i<vector.length;i++)
				if(this.vector[i]!= 0 || vector[i] != 0)
					correctness = correctness || this.vector[i] <= vector[i];
			return correctness;
		}

		// reduce en 1 el escalar de tiempos de dicho proceso
		public void decrease(int process) {
			vector[process]--;
		}

		public void draw(Graphics2D g) {
			int process = drawingPos.process;
			int tick = drawingPos.tick;

			int xDraw;
			int yDraw;

			int changedValue;

			String vectorS;
			String firstPart = null;
			String specialPart = null;
			String finalPart = null;

			/*
			 * if(print){
			 * System.out.print("Origen: <"+origin.process+", "+origin.tick+">");
			 * System
			 * .out.print(" Destino: <"+finalPos.process+", "+finalPos.tick+">");
			 * System
			 * .out.print(" Dibuja en : <"+drawingPos.process+", "+drawingPos.tick
			 * +">"); System.out.print(" Valor del vector : "); for(int i =
			 * 0;i<vector.length;i++) System.out.print(vector[i]+", ");
			 * System.out.println("\n"); }
			 */

			// el valor que cambia se pinta en rojo
			if (isOrigin)
				changedValue = finalPos.lastElement().process;
			else
				changedValue = origin.process;

			changedValue += 2 + changedValue;

			vectorS = Arrays.toString(vector).replace(" ", "");
			// if(changedValue >1)

			firstPart = vectorS.substring(0, changedValue - 1);
			specialPart = vectorS.substring(changedValue - 1, changedValue + 1);
			finalPart = vectorS.substring(changedValue + 1);

			// firstPart = vectorS.substring(1,3);
			// System.out.println(firstPart + specialPart + finalPart);

			xDraw = tick * SimulationView.cellWidth + SimulationView.paddingX
					+ X_PAINT;
			yDraw = process * (SimulationView.cellHeight + SimulationView.paddingY)
					+ SimulationView.paddingY + Y_PAINT;

			// se elevan las columnas pares para que no se solape con la anterior
			if (drawingPos.tick % 2 == 0)
				yDraw -= EVEN_ELEVATION;

			FontServer.boldFont(g);
			g.setColor(Color.BLUE);
			g.drawString(firstPart, xDraw, yDraw);

			FontMetrics font = g.getFontMetrics();
			xDraw += font.stringWidth(firstPart);

			g.setColor(Color.RED);
			g.drawString(specialPart, xDraw, yDraw);
			// FontServer.restoreFont(g);
			xDraw += font.stringWidth(specialPart);

			g.setColor(Color.BLUE);
			g.drawString(finalPart, xDraw, yDraw);
			g.setColor(Color.BLACK);
		}
		
		public void newDimension ( int dim ){
			int vectorAux[];
			int limite;
			vectorAux = vector;
			vector = new int[dim];
			limite = Math.min(dim,vectorAux.length);
			for (int i = 0;i < limite;i++)
				vector[i] = vectorAux[i];
		}
		public boolean isMultiple(){
			return finalPos.size()>1;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			String s = new String();
			s = "[";
			for(int i=0;i<vector.length;i++){
				s = s+vector[i]+", ";
			}
			s = s+"]";
			return s;
		}
}
