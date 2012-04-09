package simulation.order_static;

import java.io.Serializable;

import simulation.view.CellPosition;

public class Interval implements Serializable {

	private static final long serialVersionUID = 1L;

	CellPosition start;
	CellPosition end;

	public Interval(CellPosition start, CellPosition end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s;
		s = "[ " + start.toString() + " " + end.toString() + " ]";
		return s;
	}
}
