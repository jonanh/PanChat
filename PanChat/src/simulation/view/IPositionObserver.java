package simulation.view;

public interface IPositionObserver {

	enum Mode {
		Over, Click, DoubleClick
	}

	public void setPosition(IPosition pos, Mode mode);

}
