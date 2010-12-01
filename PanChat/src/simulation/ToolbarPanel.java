package simulation;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import simulation.model.SimulationModel;
import simulation.view.SimulationView;

@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {

	// Constantes iconos
	private static final String path = "/simulation/icons/";
	private static final String create = "go-jump";
	private static final String delete = "edit-delete";
	private static final String move = "view-fullscreen";
	private static final String cut = "edit-cut";
	private static final String open = "document-open";
	private static final String save = "document-save";

	private SimulationView simulationView;

	private JLabel numProcessLabel = new JLabel("Nº process:");
	private JLabel timeUnitLabel = new JLabel("Time unit: ");

	private JCheckBox fifoCheck = new JCheckBox("FIFO");
	private JCheckBox causalCheck = new JCheckBox("Causal");
	private JCheckBox totalCheck = new JCheckBox("Total");
	private JCheckBox multicastCheck = new JCheckBox("Multicast");

	private JTextField numProcessText = new JTextField();
	private JTextField timeUnitText = new JTextField();

	private JButton stateButton[] = new JButton[6];

	public ToolbarPanel(SimulationView simulation) {
		
		this.simulationView = simulation;

		numProcessText.setColumns(4);
		timeUnitText.setColumns(6);

		stateButton[0] = new JButton(loadIcon(create));
		stateButton[1] = new JButton(loadIcon(move));
		stateButton[2] = new JButton(loadIcon(delete));
		stateButton[3] = new JButton(loadIcon(cut));
		stateButton[4] = new JButton(loadIcon(open));
		stateButton[5] = new JButton(loadIcon(save));

		for (JButton button : stateButton)
			this.add(button);
		
		this.setLayout(new FlowLayout());
		this.add(fifoCheck);
		this.add(causalCheck);
		this.add(totalCheck);
		this.add(multicastCheck);

		this.add(numProcessLabel);
		this.add(numProcessText);
		this.add(timeUnitLabel);
		this.add(timeUnitText);

		subscribeEvents();

	}

	public ImageIcon loadIcon(String name) {
		return new ImageIcon(this.getClass().getResource(path + name + ".png"));
	}

	public void subscribeEvents() {
		stateButton[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationView.setState(SimulationView.State.CREATE);
			}
		});
		stateButton[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationView.setState(SimulationView.State.MOVE);
			}
		});
		stateButton[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationView.setState(SimulationView.State.DELETE);
			}
		});
		
		fifoCheck.addActionListener(null);
		causalCheck.addActionListener(null);
		totalCheck.addActionListener(null);
		multicastCheck.addActionListener(null);

		numProcessText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String texto;
				int numero;
				// obtener el numero de procesos
				texto = numProcessText.getText();
				numero = Integer.parseInt(texto);

				// cambiar el numero de procesos
				numero = simulationView.getSimulationModel().setNumProcesses(numero);

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
				numero = simulationView.getSimulationModel().setTimeTicks(numero);

				// indicar en el textbox el numero de procesos establecidos
				texto = String.valueOf(numero);
				timeUnitText.setText(texto);
			}
		});
	}

	public static void main(String[] args) {
		JFrame ventana = new JFrame("prueba de los menus");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.add(new ToolbarPanel(new SimulationView(new SimulationModel())));

		ventana.setVisible(true);
		ventana.setSize(850, 60);
	}
}
