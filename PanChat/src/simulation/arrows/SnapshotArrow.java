package simulation.arrows;
import java.io.Serializable;


@SuppressWarnings("serial")
public class SnapshotArrow extends MessageArrow implements Serializable {

	int Process;

	// int[] time = new int[2];

	public SnapshotArrow(int time1, int process1, int time2, int process2) {
		// Construimos la linea sin saber aún las coordenadas
		super(0, 0, 0, 0);

		// Calculamos las coordenadas
		// Point2D p1 = InformationCanvas.time2Point2D(time1);
		// Point2D p2 = InformationCanvas.time2Point2D(time2);

		// Establecemos los valores
		// this.setLine(p1, p2);
	}

}
