package simulation.order_dinamic;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import simulation.view.CellPosition;
import simulation.view.CutPosition;
import simulation.view.IPositionObserver;
import simulation.view.Position;

public class UIDebugWindow extends JFrame implements IPositionObserver {

	private static final long serialVersionUID = 1L;

	private SimulationOrderModel simul;

	private JTextArea textArea = new JTextArea();

	public UIDebugWindow(SimulationOrderModel simul) {
		super("Simulation Layer Debug");

		this.simul = simul;

		textArea.setEditable(false);

		this.getContentPane().add(new JScrollPane(textArea));
		this.setSize(600, 500);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(false);
	}

	@Override
	public void setPosition(Position pos, Mode mode) {
		if (mode.equals(Mode.DoubleClick)) {
			String log = "";
			if (pos instanceof CellPosition) {

				CellPosition cell = (CellPosition) pos;

				String result = simul.getLog(cell);
				log += result == null ? "" : result;

			} else if (pos instanceof CutPosition) {
				int tick = ((CutPosition) pos).tick;
				CellPosition iter = new CellPosition(0, tick);
				for (; iter.process < simul.getNumProcesses(); iter.process++) {

					log += "\nprocess : " + String.valueOf(iter.process + 1);
					log += "\n----------\n";

					String result = simul.getLog(iter);
					log += result == null ? "" : result;
				}
			}
			textArea.setText(log);
			this.setVisible(true);
		}
	}
}
