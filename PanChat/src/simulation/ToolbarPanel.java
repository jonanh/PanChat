package simulation;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import simulation.model.SimulationModel;

@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {

	private SimulationModel simulationData;

	private JLabel numProcessLabel = new JLabel("Nº process:");
	private JLabel timeUnitLabel = new JLabel("Time unit: ");

	private JCheckBox fifoCheck = new JCheckBox("FIFO");
	private JCheckBox causalCheck = new JCheckBox("Causal");
	private JCheckBox totalCheck = new JCheckBox("Total");
	private JCheckBox noneCheck = new JCheckBox("none");

	private JTextField numProcessText = new JTextField();
	private JTextField timeUnitText = new JTextField();

	private JButton cutButton = new JButton("Cut");
	private JButton snapshotButton = new JButton("Snapshot");
	private JButton eventButton = new JButton("Events");
	private JButton startButton = new JButton("Start");
	private JButton moveSLButton = new JButton("Move Snapshot");
	private JButton stopMovingSLButton = new JButton("Stop Moving Snapshot");

	public ToolbarPanel(SimulationModel simulationData) {
		this.simulationData = simulationData;

		numProcessText.setColumns(4);
		timeUnitText.setColumns(6);

		this.setLayout(new FlowLayout());
		this.add(fifoCheck);
		this.add(causalCheck);
		this.add(totalCheck);
		this.add(noneCheck);
		this.add(cutButton);
		this.add(numProcessLabel);
		this.add(numProcessText);
		this.add(timeUnitLabel);
		this.add(timeUnitText);
		this.add(snapshotButton);
		this.add(eventButton);
		this.add(startButton);
		this.add(moveSLButton);
		this.add(stopMovingSLButton);

		subscribeEvents();

	}

	public void subscribeEvents() {
		fifoCheck.addActionListener(null);
		causalCheck.addActionListener(null);
		totalCheck.addActionListener(null);
		noneCheck.addActionListener(null);

		numProcessText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String texto;
				int numero;
				// obtener el numero de procesos
				texto = numProcessText.getText();
				numero = Integer.parseInt(texto);

				// cambiar el numero de procesos
				numero = simulationData.setNumProcesses(numero);

				// indicar en el textbox el numero de procesos establecidos
				texto = String.valueOf(numero);
				numProcessText.setText(texto);
			}

		});
		timeUnitText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String texto;
				int numero;
				// obtener el numero de procesos
				texto = timeUnitText.getText();
				numero = Integer.parseInt(texto);

				// cambiar el numero de procesos
				numero = simulationData.setTimeTicks(numero);

				// indicar en el textbox el numero de procesos establecidos
				texto = String.valueOf(numero);
				timeUnitText.setText(texto);
			}
		});

		cutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// simulationData.setState(SimulationData.CUT);
				// simulationData.setIsCut(true);
				// simulationData.getCutLine().add(new Line(0, 0));
				// simulationData.getCutLine().lastElement().setColor(Color.RED);
			}

		});
		snapshotButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// if (simulationData.getIsFixSnapshot() == true) {
				// simulationData.setFixSnapshot(false);
				// simulationData.setSnapshotEmpty();
				// }
				// simulationData.setState(SimulationData.SNAPSHOT);
			}
		});
		eventButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// simulationData.setState(SimulationData.EVENT);
			}

		});

		startButton.addActionListener(null);

		moveSLButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// simulationData.setState(SimulationData.MOVE);
			}

		});
		stopMovingSLButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// simulationData.setState(SimulationData.EVENT);
			}

		});
	}

	public static void main(String[] args) {
		JFrame ventana = new JFrame("prueba de los menus");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.add(new ToolbarPanel(new SimulationModel()));

		ventana.setVisible(true);
		ventana.setSize(300, 300);
	}
}
