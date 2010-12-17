package simulation.view.order;


import java.util.Vector;

public class Intersection {
	Vector<Interval> causalTotal;
	Vector<Interval> causal;
	Vector<Interval> total;
	
	public Intersection(){
		causalTotal = new Vector<Interval>();
		causal = new Vector<Interval>();
		total = new Vector<Interval>();
	}
}
