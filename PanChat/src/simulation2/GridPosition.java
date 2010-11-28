package simulation2;
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
}
