package simulation.view.listener;
import simulation.ToolbarPanel;
import simulation.model.*;
import simulation.view.SimulationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class OrderListener implements ActionListener {
	ToolbarPanel panel;
	SimulationModel model;
	
	public OrderListener(SimulationModel model,ToolbarPanel panel){
		this.panel = panel;
		this.model = model;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object action = arg0.getSource();
		JCheckBox sourceCheck = null;
		if(panel.getFifoCheck() == action){
			 sourceCheck = (JCheckBox)action;
			 setFifo(sourceCheck);
		}
		else if(panel.getCausalCheck() == action){
			sourceCheck = (JCheckBox)action;
			setCausal(sourceCheck);
		}
		else if(panel.getTotalCheck() == action){
			sourceCheck = (JCheckBox)action;
			setTotal(sourceCheck);
		}
		else if(panel.getShowFifoCheck() == action){
			sourceCheck = (JCheckBox) action;
			showFifoClocks(sourceCheck);
		}	
	}
	
	private void setFifo (JCheckBox fifo){
		//si esta activado hay que insertar fifo
		if(fifo.isSelected()){
			model.addFifoLayer();
		}
		else{
			model.removeFifoLayer();
		}
	}
	
	private void setCausal (JCheckBox causal){
		//si esta activado hay que insertar fifo
		if(causal.isSelected()){
			model.addCausalLayer();
		}
		else{
			model.removeCausalLayer();
		}
	}
	
	private void setTotal (JCheckBox total){
		//si esta activo se inserta,
		if(total.isSelected()){
			model.addTotalLayer();
		}
		else{
			model.removeTotalLayer();
		}
	}
	
	
	private void showFifoClocks(JCheckBox showFifo){
		if(showFifo.isSelected()){
			if(panel.getFifoCheck().isSelected())
				model.setShowFifo();
		}
		else{
			model.unsetShowFifo();
		}
	}

}
