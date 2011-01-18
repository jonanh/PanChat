package simulation.chandy_lamport;
public class GridPosition {
	
	private int xGrid;
	private int yGrid;
	private int reverseXGrid;
	private int reverseYGrid;
	private float xPos;
	private float yPos;

	private InformationCanvas canvas;

	public GridPosition(float x, float y, InformationCanvas canvas) {
		xPos = x;
		yPos = y;
		this.canvas = canvas;
		calculateGrid();

	}

	public GridPosition(float x, float y) {
		xPos = x;
		yPos = y;
		calculateGrid();

	}

	public GridPosition(InformationCanvas canvas) {
		this.canvas = canvas;
	}

	public GridPosition() {
		xGrid = 0;
		yGrid = 0;
		xPos = 0.0f;
		yPos = 0.0f;
	}

	public int getXGrid() {
		return xGrid;
	}

	public int getYGrid() {
		return yGrid;
	}

	public int getReverseXGrid() {
		return reverseXGrid;
	}

	public int getReverseYGrid() {
		return reverseYGrid;
	}

	public float getXPos() {
		return xPos;
	}

	public float getYPos() {
		return yPos;
	}

	public void setXGrid(int x) {
		xGrid = x;
	}

	public void setYGrid(int y) {
		yGrid = y;
	}

	public void setXPos(float x) {
		xPos = x;
	}

	public void setYPos(float y) {
		yPos = y;
	}
	public void setReverseXGrid(int x){
		reverseXGrid = x;
	}
	public void setReverseYGrid(int y){
		reverseYGrid = y;
	}

	public void setCanvas(InformationCanvas canvas) {
		this.canvas = canvas;
	}

	public void calculateGrid() {
		if (canvas != null) {
			xGrid = (int) ((xPos - canvas.getXLength() / 5) / canvas
					.getTimeUnit());
			yGrid = (int) ((yPos - canvas.getYLength() / 10) / canvas
					.getProcessWidth());

			reverseXGrid = xGrid * canvas.getTimeUnit() + canvas.getXLength()
					/ 5;
			reverseYGrid = yGrid * canvas.getProcessWidth()
					+ canvas.getYLength() / 10;
		}
	}
	
	/*
	 * indica a partir del gridX si es un proceso
	 */
	public boolean isProcess(){
		System.out.println("yGrid: "+yGrid+" modulo: "+ (yGrid%5==0));
		return yGrid%5==0;
	}
	//calcula la posicion correspondiente a la mitad de la cuadricula donde
	//se encuentran x e y
	public void calculateMiddleGrid(int x,int y){
		setXPos(x);
		setYPos(y);
		calculateGrid();
		setReverseXGrid(getReverseXGrid()+canvas.getTimeUnit()/2);
		setReverseYGrid(getReverseYGrid()+canvas.getProcessWidth()/2);
		System.out.println("S"+canvas.getProcessWidth());
		
	}
}
