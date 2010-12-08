package simulation.view.order;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

import simulation.model.SimulationModel;
import simulation.view.CellPosition;
import simulation.view.SimulationView;

/*
 * clase en la que se almacena el vector de una determinada posicion
 * origen: proceso origen del mensaje al que corresponde el vector
 * finalpos: proceso destino del mensaje al que corresponde el vector 
 * drawingPos: lugar donde se ha de dibujar el vector, corresponde al destino
 * los valores del vector corresponden a los mensajes que ha recibido destino
 */
public class VectorClock {
	public static final int X_PAINT = 0;
	public static final int Y_PAINT = -2;
	public static final int CHARATER_WIDTH = 4;
	public static final int SPECIAL_CHARATER_WIDTH = 0;
	
	CellPosition origin;
	CellPosition finalPos;
	CellPosition drawingPos;
	
	boolean isMultiple;
	
	
	
	//public static boolean print = false;
	
	//indica si este vector corresponde a origen o a destino
	boolean isOrigin;
	int vector[];
	
	public VectorClock(){
		vector = new int[SimulationModel.numProcesses];
	}
	
	public VectorClock(CellPosition origin, CellPosition destiny,boolean isOrigin,boolean isMultiple){
		this();
		this.origin = origin;
		finalPos = destiny;
		this.isOrigin= isOrigin;
		this.isMultiple = isMultiple;
		
		/*
		 * si es origen la posicion de dibujo sera la posicion de origen
		 * si es destino la posicion e dibujo sera la posicion de destino
		 */
		if(this.isOrigin == true)
			drawingPos = origin;
		else
			drawingPos = finalPos;
	}
	
	public void incrPos(int i){
		vector[i]++;
	}
	
	public void setVector (VectorClock vector){
		/*
		 * se copia el vector y se incrementa la posicion correspondiente
		 * a position
		 */
		int [] vector2 = vector.vector;
		
		for (int i=0;i<vector2.length;i++)
			this.vector[i] = vector2[i];
		
		/*
		 * si es origen, hay que incrementar a quien se manda
		 * si es destino, hay que incrementar de quie espero
		 */
		if(isOrigin == true)
			this.vector[finalPos.process]++;
		else
			this.vector[origin.process]++;
	}
	
	public void initialize(){
		/*
		 * se inicializa el vector y se incrementa la posicion correspondiente
		 */
		for (int i=0;i<vector.length;i++)
			this.vector[i] = 0;
		
		/*
		 * si es origen, hay que incrementar a quien se manda
		 * si es destino, hay que incrementar de quie espero
		 */
		if(isOrigin == true)
			this.vector[finalPos.process]++;
		else
			this.vector[origin.process]++;
	}
	
	public boolean isCorrect (VectorClock vectorClock){
		//se comprueba si el vector es correcto
		boolean correctness = true;
		int []vector = vectorClock.vector;
		
		/*for(int i = 0;i<vector.length;i++){
			correct = vector[i]== vector[i];
			if(correct == false)break;
		}*/
		correctness = this.vector[origin.process] == vector[finalPos.process];
		return correctness;
	}
	
	public void draw(Graphics2D g){
		int process = drawingPos.process;
		int tick = drawingPos.tick;
		
		int xDraw;
		int yDraw;
		
		int changedValue;
		
		String vectorS;
		String firstPart= null;
		String specialPart= null;
		String finalPart= null;
		
		/*if(print){
		System.out.print("Origen: <"+origin.process+", "+origin.tick+">");
		System.out.print(" Destino: <"+finalPos.process+", "+finalPos.tick+">");
		System.out.print(" Dibuja en : <"+drawingPos.process+", "+drawingPos.tick+">");
		System.out.print(" Valor del vector : ");
		for(int i = 0;i<vector.length;i++)
			System.out.print(vector[i]+", ");
		System.out.println("\n");
		}*/
		
		//el valor que cambia se pinta en rojo
		if(isOrigin)
			changedValue = finalPos.process;
		else
			changedValue = origin.process;
		
		changedValue+= 2+ changedValue;
		
		vectorS = Arrays.toString(vector).replace(" ","");
		//if(changedValue >1)
		
		firstPart = vectorS.substring(0,changedValue-1);
		specialPart = vectorS.substring(changedValue-1,changedValue+1);
		finalPart = vectorS.substring(changedValue+1);
		
		//firstPart = vectorS.substring(1,3);
		//System.out.println(firstPart + specialPart + finalPart);
		xDraw = tick*SimulationView.cellWidth + SimulationView.paddingX + X_PAINT;
		yDraw = process*(SimulationView.cellHeight+SimulationView.paddingY) 
								+ SimulationView.paddingY+ Y_PAINT;
		
		g.drawString(firstPart, xDraw, yDraw);
		xDraw += firstPart.length()*CHARATER_WIDTH+SPECIAL_CHARATER_WIDTH;
		
		g.setColor(Color.RED);
		FontServer.boldFont(g);
		g.drawString(specialPart,xDraw,yDraw);
		FontServer.restoreFont(g);
		xDraw += specialPart.length()*CHARATER_WIDTH+SPECIAL_CHARATER_WIDTH;
		
		g.setColor(Color.BLACK);
		g.drawString(finalPart,xDraw,yDraw);
		
	}
	
	
}
