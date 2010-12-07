package simulation.view.order;

import java.awt.Graphics;

import simulation.model.SimulationModel;
import simulation.view.CellPosition;

/*
 * clase en la que se almacena el vector de una determinada posicion
 * origen: proceso origen del mensaje al que corresponde el vector
 * finalpos: proceso destino del mensaje al que corresponde el vector 
 * drawingPos: lugar donde se ha de dibujar el vector, corresponde al destino
 * los valores del vector corresponden a los mensajes que ha recibido destino
 */
public class VectorClock {
	CellPosition origin;
	CellPosition finalPos;
	CellPosition drawingPos;
	
	public static boolean print = false;
	
	//indica si este vector corresponde a origen o a destino
	boolean isOrigin;
	int vector[];
	
	public VectorClock(){
		vector = new int[SimulationModel.numProcesses];
	}
	
	public VectorClock(CellPosition origin, CellPosition destiny,boolean isOrigin){
		this();
		this.origin = origin;
		finalPos = destiny;
		this.isOrigin= isOrigin;
		
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
	
	public void draw(Graphics g){
		if(print){
		System.out.print("Origen: <"+origin.process+", "+origin.tick+">");
		System.out.print(" Destino: <"+finalPos.process+", "+finalPos.tick+">");
		System.out.print(" Dibuja en : <"+drawingPos.process+", "+drawingPos.tick+">");
		System.out.print(" Valor del vector : ");
		for(int i = 0;i<vector.length;i++)
			System.out.print(vector[i]+", ");
		System.out.println("\n");
		}
	}
	
	
}
