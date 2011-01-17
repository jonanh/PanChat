package simulation3;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import order.Message.Type;


import simulation3.model.SimulationArrowModel;
import simulation3.view.SimulationView;

@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {

	// Constantes iconos
	private static final String path = "/simulation/icons/";
	private static final String create = "go-jump";
	private static final String delete = "edit-delete";
	private static final String move = "view-fullscreen";
	private static final String open = "document-open";
	private static final String save = "document-save";

	private Simulation simulation;
	private SimulationView simulationView;

	private JLabel numProcessLabel = new JLabel("Nº process:");
	private JLabel timeUnitLabel = new JLabel("Time unit: ");

	private JCheckBox fifoCheck = new JCheckBox("FIFO");
	private JCheckBox causalCheck = new JCheckBox("Causal");
	private JCheckBox totalCheck = new JCheckBox("Total");

	private JCheckBox deliveryCheck = new JCheckBox("Delivery");

	private EnumMap<Type, Boolean> properties;

	private JTextField numProcessText = new JTextField();
	private JTextField timeUnitText = new JTextField();

	private JButton stateButton[] = new JButton[6];

	public ToolbarPanel(Simulation simulation) {

		this.simulation = simulation;
		this.simulationView = simulation.getSimulationView();

		// Establecemos el tamaño de los cuadros de texto
		numProcessText.setColumns(4);
		timeUnitText.setColumns(6);

		// Añadimos los botones
		stateButton[0] = new JButton(loadIcon(create));
		stateButton[1] = new JButton(loadIcon(move));
		stateButton[2] = new JButton(loadIcon(delete));
		stateButton[4] = new JButton(loadIcon(open));
		stateButton[5] = new JButton(loadIcon(save));

		for (JButton button : stateButton)
			if (button != null)
				this.add(button);

		// Añadimos todos los componentes al layout
		this.setLayout(new FlowLayout());
		this.add(fifoCheck);
		this.add(causalCheck);
		this.add(totalCheck);

		this.add(deliveryCheck);

		this.add(numProcessLabel);
		this.add(numProcessText);
		this.add(timeUnitLabel);
		this.add(timeUnitText);

		deliveryCheck.setSelected(true);

		// Inicializamos el texto de los cuadros de simulacion
		int numeroProcesses = simulationView.getSimulationModel()
				.getNumProcesses();
		numProcessText.setText(String.valueOf(numeroProcesses));

		int numeroTicks = simulationView.getSimulationModel().getTimeTicks();
		timeUnitText.setText(String.valueOf(numeroTicks));

		// Obtenemos las propiedades del CreateListener
		properties = simulationView.getCreationProperties();

		subscribeEvents();
	}

	public ImageIcon loadIcon(String name) {
		return new ImageIcon(this.getClass().getResource(path + name + ".png"));
	}

	public void subscribeEvents() {

		final Component parent = this;

		// Create
		stateButton[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationView.setState(SimulationView.State.CREATE);
			}
		});

		// Move
		stateButton[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationView.setState(SimulationView.State.MOVE);
			}
		});

		// Delete
		stateButton[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationView.setState(SimulationView.State.DELETE);
			}
		});

		// Abrir
		stateButton[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimulationArrowModel simulationModel = FileChooser
						.getFile(parent);

				// Si hemos cargado un fichero
				if (simulationModel != null) {
					simulation.setSimulationModel(simulationModel);

					/*
					 * Establecemos los textbox del numero de procesos y ticks
					 * de acuerdo al nuevo modelo
					 */
					int numeroProcesses = simulationView.getSimulationModel()
							.getNumProcesses();
					numProcessText.setText(String.valueOf(numeroProcesses));

					int numeroTicks = simulationView.getSimulationModel()
							.getTimeTicks();
					timeUnitText.setText(String.valueOf(numeroTicks));
				}

			}
		});

		// Salvar
		stateButton[5].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimulationArrowModel simulationModel = simulationView
						.getSimulationModel();
				FileChooser.saveFile(parent, simulationModel);
			}
		});

		fifoCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Nuestro algoritmo requiere que el orden total sea tambien
				// FIFO o CAUSAL
				if (totalCheck.isSelected() && !fifoCheck.isSelected()
						&& !causalCheck.isSelected()) {
					totalCheck.setSelected(false);
					properties.put(Type.TOTAL, false);
				}

				properties.put(Type.FIFO, fifoCheck.isSelected());
			}
		});

		causalCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Nuestro algoritmo requiere que el orden total sea tambien
				// FIFO o CAUSAL
				if (totalCheck.isSelected() && !fifoCheck.isSelected()
						&& !causalCheck.isSelected()) {
					totalCheck.setSelected(false);
					properties.put(Type.TOTAL, false);
				}

				properties.put(Type.CAUSAL, causalCheck.isSelected());
			}
		});

		totalCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Nuestro algoritmo requiere que el orden total sea tambien
				// FIFO o CAUSAL
				if (totalCheck.isSelected() && !fifoCheck.isSelected()
						&& !causalCheck.isSelected()) {
					fifoCheck.setSelected(true);
					properties.put(Type.FIFO, true);
				}

				properties.put(Type.TOTAL, totalCheck.isSelected());
			}
		});

		deliveryCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// simulationView.setDrawSimulationArrows(deliveryCheck
				// .isSelected());

			}
		});

		numProcessText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String texto;
				int numero;
				// obtener el numero de procesos
				texto = numProcessText.getText();
				numero = Integer.parseInt(texto);

				// cambiar el numero de procesos
				numero = simulationView.getSimulationModel().setNumProcesses(
						numero);

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
				numero = simulationView.getSimulationModel().setTimeTicks(
						numero);

				// indicar en el textbox el numero de procesos establecidos
				texto = String.valueOf(numero);
				timeUnitText.setText(texto);
			}
		});
	}
}
