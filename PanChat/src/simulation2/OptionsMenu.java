package simulation2;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OptionsMenu extends JPanel {
	private InformationCanvas canvas;
	private static final String imagePath="/simulation2/24x24/";
	private JLabel numProcessLabel;
	private JLabel timeUnitLabel;
	
	private JCheckBox fifoCheck;
	private JCheckBox causalCheck;
	private JCheckBox totalCheck;
	private JCheckBox noneCheck;

	
	private JTextField numProcessText;
	private JTextField timeUnitText;
	
	private JButton showSnapshotPanelButton;
	private JButton showEventPanelButton;
	private JButton showCutPanelButton;
	private JButton showSimulationPanelButton;
	
	private JButton createCutButton;
	private JButton moveCutButton;
	private JButton removeCutButton;
	
	private JButton createSnapshot;
	private JButton createMessageLine;
	private JButton removeMessageLine;
	private JButton moveMessageLine;
	private JButton playButton;
	private JButton pauseButton;
	
	private JButton moveSLButton;
	
	
	private JPanel toolPanel;
	private JPanel snapshotPanel;
	private JPanel eventPanel;
	private JPanel cutPanel;
	private JPanel simulationPanel;
	private JPanel generalOptionsPanel;
	

	
	private ImageIcon snapshotIcon;
	private ImageIcon cutIcon;
	private ImageIcon deleteIcon;
	private ImageIcon moveIcon;
	private ImageIcon pauseIcon;
	private ImageIcon startIcon;
	
	private ImageIcon createLineIcon;
	private ImageIcon showEventIcon;
	
	private ClassLoader loader;
	
	
	
	UserActionListener actionHandle;
	
	public OptionsMenu (InformationCanvas canvas){		
		this.canvas = canvas;
		loader = this.getClass().getClassLoader();
		
		toolPanel = new JPanel();
		snapshotPanel = new JPanel();
		eventPanel = new JPanel();
		cutPanel = new JPanel();
		simulationPanel = new JPanel();
		generalOptionsPanel = new JPanel();
		
		numProcessLabel = new JLabel("Nº process:");
		timeUnitLabel = new JLabel("Time unit: ");
		
		numProcessText = new JTextField();
		numProcessText.setColumns(4);
		timeUnitText = new JTextField();
		timeUnitText.setColumns(6);
		
		
		//botones del panel de eventos
		showEventPanelButton = new JButton();
		fifoCheck = new JCheckBox("FIFO");
		causalCheck = new JCheckBox("Causal");
		totalCheck = new JCheckBox("Total");
		noneCheck = new JCheckBox("none");
		createMessageLine = new JButton("Events");
		moveMessageLine = new JButton();
		removeMessageLine = new JButton();
		//-----------------------------------------
		
		//botones del panel de corte
		showCutPanelButton = new JButton();
		createCutButton = new JButton("Cut");
		moveCutButton = new JButton();
		removeCutButton = new JButton();
		
		//-----------------------------------------
		
		//botones del panel de snapshot
		showSnapshotPanelButton = new JButton();
		createSnapshot = new JButton("Snapshot");
		moveSLButton = new JButton("Move Snapshot");
		
		//-----------------------------------------
		
		//botones del panel de simulacion
		showSimulationPanelButton = new JButton();
		playButton = new JButton("Start");
		pauseButton = new JButton();
		
		
		this.setLayout(new BorderLayout());
		//panel de herramientas
		toolPanel.add(showSnapshotPanelButton);
		toolPanel.add(showEventPanelButton);
		toolPanel.add(showCutPanelButton);
		toolPanel.add(showSimulationPanelButton);
		
		//panel de opciones generales
		generalOptionsPanel.add(numProcessLabel);
		generalOptionsPanel.add(numProcessText);
		generalOptionsPanel.add(timeUnitLabel);
		generalOptionsPanel.add(timeUnitText);
		
		//opciones panel de eventos
		eventPanel.add(fifoCheck);
		eventPanel.add(causalCheck);
		eventPanel.add(totalCheck);
		eventPanel.add(noneCheck);
		eventPanel.add(createMessageLine);
		eventPanel.add(moveMessageLine);
		eventPanel.add(removeMessageLine);
		
		
		//opciones del panel de snapshot
		snapshotPanel.add(createSnapshot);
		snapshotPanel.add(moveSLButton);
		
		
		//opciones del panel de corte
		cutPanel.add(createCutButton);
		cutPanel.add(moveCutButton);
		cutPanel.add(removeCutButton);
		
		//opciones del panel de simulacion
		simulationPanel.add(playButton);
		simulationPanel.add(pauseButton);
		
		
		this.add(toolPanel,BorderLayout.WEST);
		this.add(generalOptionsPanel,BorderLayout.EAST);
		this.add(new JPanel(),BorderLayout.CENTER);
		//this.add(snapshotPanel,BorderLayout.CENTER);
		//eventPanel.setVisible(false);
		
		
		
		
		loadImages();
		setImages();
		
		actionHandle = new UserActionListener(this);
		subscribeEvents();
		
	}
	
	public void loadImages(){
		showEventIcon = createIcon(imagePath+"media-playlist-shuffle.png");
		
		createLineIcon = createIcon(imagePath+"object-rotate-right.png");
		moveIcon = createIcon(imagePath+"view-refresh.png");
		cutIcon = createIcon (imagePath+"edit-cut.png");
		deleteIcon = createIcon(imagePath+"edit-delete.png");
		snapshotIcon = createIcon(imagePath+"go-jump.png");
		startIcon = createIcon(imagePath+"media-playback-start.png");
		pauseIcon = createIcon(imagePath+"media-playback-pause.png");
		
	}
	
	public ImageIcon createIcon(String path){
		/*URL imageUrl = loader.getResource(path);
		System.out.println(imageUrl);*/
		System.out.println(this.getClass().getResource(path));
		return new ImageIcon(this.getClass().getResource(path));
	}
	
	
	public void setImages(){
		showSnapshotPanelButton.setIcon(snapshotIcon);
		showEventPanelButton.setIcon(showEventIcon);
		showCutPanelButton.setIcon(cutIcon);
		showSimulationPanelButton.setLabel("mostrar simulacion");
		
		createCutButton.setLabel("crear corte");
		moveCutButton.setIcon(moveIcon);
		removeCutButton.setIcon(deleteIcon);
		
		createSnapshot.setIcon(snapshotIcon);
		createMessageLine.setIcon(createLineIcon);
		removeMessageLine.setIcon(deleteIcon);
		moveMessageLine.setIcon(moveIcon);
		playButton.setIcon(startIcon);
		pauseButton.setIcon(pauseIcon);
		
		moveSLButton.setIcon(moveIcon);
		}
	
	public void subscribeEvents(){
		showSnapshotPanelButton.addActionListener(actionHandle);
		showCutPanelButton.addActionListener(actionHandle);
		showEventPanelButton.addActionListener(actionHandle);
		showSimulationPanelButton.addActionListener(actionHandle);
		
		fifoCheck.addActionListener(actionHandle);
		causalCheck.addActionListener(actionHandle);
		totalCheck.addActionListener(actionHandle);
		noneCheck.addActionListener(actionHandle);
		
		numProcessText.addKeyListener(actionHandle);
		timeUnitText.addKeyListener(actionHandle);
		
		createCutButton.addActionListener(actionHandle);
		createSnapshot.addActionListener(actionHandle);
		createMessageLine.addActionListener(actionHandle);
		playButton.addActionListener(actionHandle);
		moveSLButton.addActionListener(actionHandle);
		
	}
	
	public void setEventPanel(){
		BorderLayout layout =(BorderLayout) this.getLayout();
		this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		this.add(eventPanel,BorderLayout.CENTER);
		this.validate();
		this.repaint();
		
	}
	public void setSnapshotPanel(){
		BorderLayout layout =(BorderLayout) this.getLayout();
		this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		this.add(snapshotPanel,BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}
	public void setCutPanel(){
		BorderLayout layout =(BorderLayout) this.getLayout();
		this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		this.add(cutPanel,BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}
	public void setSimulationPanel(){
		BorderLayout layout =(BorderLayout) this.getLayout();
		this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		this.add(simulationPanel,BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}
	
	public JCheckBox getFifoCheck(){return fifoCheck;}
	public JCheckBox getCausalCheck(){return causalCheck;}
	public JCheckBox getTotalCheck(){return totalCheck;}
	public JCheckBox getNoneCheck(){return noneCheck;}
	
	public JTextField getNumProcessText(){return numProcessText;}
	public JTextField getTimeUnitText(){return timeUnitText;}
	
	public JButton getCutButton(){return createCutButton;}
	public JButton getSnapshotButton(){return createSnapshot;}
	public JButton getEventButton(){return createMessageLine;}
	public JButton getStartButton(){return playButton;}
	public JButton getMoveSLButton(){return moveSLButton;}
	public JButton getShowEventPanelButton(){return showEventPanelButton;}
	public JButton getShowCutPanelButton(){return showCutPanelButton;}
	public JButton getShowSimulationPanelButton(){return showSimulationPanelButton;}
	public JButton getShowSnapshotPanelButton(){return showSnapshotPanelButton;}
	
	
	public InformationCanvas getCanvas(){return canvas;}
	
	
	public static void main (String[] args){
		JFrame ventana = new JFrame ("prueba de los menus");
		ventana.add(new OptionsMenu(new InformationCanvas()));
		
		ventana.setVisible(true);
		ventana.setSize(300,300);
		ventana.setDefaultCloseOperation(2);
	}
}
