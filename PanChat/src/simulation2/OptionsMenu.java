package simulation2;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OptionsMenu extends JPanel {
	private InformationCanvas canvas;
	
	private JLabel numProcessLabel;
	private JLabel timeUnitLabel;
	
	private JCheckBox fifoCheck;
	private JCheckBox causalCheck;
	private JCheckBox totalCheck;
	private JCheckBox noneCheck;

	
	private JTextField numProcessText;
	private JTextField timeUnitText;
	
	private JButton cutButton;
	private JButton snapshotButton;
	private JButton eventButton;
	private JButton startButton;
	private JButton moveSLButton;
	private JButton stopMovingSLButton;
	
	
	UserActionListener actionHandle;
	
	public OptionsMenu (InformationCanvas canvas){		
		this.canvas = canvas;
		
		numProcessLabel = new JLabel("N� process:");
		timeUnitLabel = new JLabel("Time unit: ");
		
		fifoCheck = new JCheckBox("FIFO");
		causalCheck = new JCheckBox("Causal");
		totalCheck = new JCheckBox("Total");
		noneCheck = new JCheckBox("none");
		
		numProcessText = new JTextField();
		numProcessText.setColumns(4);
		timeUnitText = new JTextField();
		timeUnitText.setColumns(6);
		
		cutButton = new JButton("Cut");
		snapshotButton = new JButton("Snapshot");
		eventButton = new JButton("Events");
		startButton = new JButton("Start");
		moveSLButton = new JButton("Move Snapshot");
		stopMovingSLButton = new JButton ("Stop Moving Snapshot");
		
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
		
		actionHandle = new UserActionListener(this);
		subscribeEvents();
		
	}
	
	public void subscribeEvents(){
		fifoCheck.addActionListener(actionHandle);
		causalCheck.addActionListener(actionHandle);
		totalCheck.addActionListener(actionHandle);
		noneCheck.addActionListener(actionHandle);
		
		numProcessText.addKeyListener(actionHandle);
		timeUnitText.addKeyListener(actionHandle);
		
		cutButton.addActionListener(actionHandle);
		snapshotButton.addActionListener(actionHandle);
		eventButton.addActionListener(actionHandle);
		startButton.addActionListener(actionHandle);
		moveSLButton.addActionListener(actionHandle);
		stopMovingSLButton.addActionListener(actionHandle);
	}
	
	
	public JCheckBox getFifoCheck(){return fifoCheck;}
	public JCheckBox getCausalCheck(){return causalCheck;}
	public JCheckBox getTotalCheck(){return totalCheck;}
	public JCheckBox getNoneCheck(){return noneCheck;}
	
	public JTextField getNumProcessText(){return numProcessText;}
	public JTextField getTimeUnitText(){return timeUnitText;}
	
	public JButton getCutButton(){return cutButton;}
	public JButton getSnapshotButton(){return snapshotButton;}
	public JButton getEventButton(){return eventButton;}
	public JButton getStartButton(){return startButton;}
	public JButton getMoveSLButton(){return moveSLButton;}
	public JButton getStopMovingSLButton(){return stopMovingSLButton;}
	
	public InformationCanvas getCanvas(){return canvas;}
	
	
	public static void main (String[] args){
		JFrame ventana = new JFrame ("prueba de los menus");
		ventana.add(new OptionsMenu(new InformationCanvas()));
		
		ventana.setVisible(true);
		ventana.setSize(300,300);
		ventana.setDefaultCloseOperation(2);
	}
}
