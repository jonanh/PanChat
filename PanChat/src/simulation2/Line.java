package simulation2;
import java.awt.Color;



public class Line {
	static int DEFAULT_ANGLE = 25;
	static int DEFAULT_LENGTH_X = 20;
	static int DEFAULT_LENGTH_Y = 15;
	
	private int initX,initY;
	private int finalX,finalY;
	private Color color;
	private boolean flecha;
	private boolean isProcess;
	private int angle;
	private int lengthX;
	private int lengthY;
	
	int xFS,yFS,xFN,yFN;
	
	public Line(int x, int y){
		initX = x;
		initY = y;
		
		angle = DEFAULT_ANGLE;
		lengthX = DEFAULT_LENGTH_X;
		lengthY = DEFAULT_LENGTH_Y;
		
		xFS = 0;
		yFS = 0;
		xFN = 0;
		yFN = 0;
		flecha = false;
		isProcess = false;
	}
	
	
	public int getInitX(){return initX;}
	public int getInitY(){return initY;}
	public int getFinalX(){return finalX;}
	public int getFinalY(){return finalY;}
	public int getAngle(){return angle;}
	public int getLengthX(){return lengthX;}
	public int getLengthY(){return lengthY;}
	public boolean isArrow(){return flecha;}
	public boolean isProcess(){return isProcess;}
	public Color getColor(){return color;}
	
	public void setInitX(int x){initX = x;}
	public void setInitY(int y){initY = y;}
	public void setFinalX(int x){finalX = x;}
	public void setFinalY(int y){finalY = y;}
	public void setAngle(int angle){this.angle = angle;}
	public void setLengthX(int length){this.lengthX = length;}
	public void setLengthY(int length){this.lengthY = length;}
	public void setArrow(boolean isArrow){flecha = isArrow;}
	public void setProcess(boolean isProcess){this.isProcess = isProcess;}
	public void setColor(Color color){this.color = color;}
}
