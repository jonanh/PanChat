package simulation2;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//responde a las acciones del usuario sobre los botones

public class UserActionListener implements ActionListener,KeyListener{
	private OptionsMenu options;
	private InformationCanvas canvas;
	
	public UserActionListener (OptionsMenu options){
		this.options = options;
		canvas = options.getCanvas();
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource()==options.getFifoCheck()){
			
		}
		else if(arg0.getSource()==options.getCausalCheck()){
			System.out.println("causal");
		}
		else if(arg0.getSource()==options.getTotalCheck()){
			
		}
		else if(arg0.getSource()==options.getNoneCheck()){
	
		}
		else if(arg0.getSource()==options.getCutButton()){
			canvas.setState(InformationCanvas.State.CUT);
			canvas.setIsCut(true);
			canvas.getCutLine().add(new Line(0,0));
			canvas.getCutLine().lastElement().setColor(Color.RED);
		}
		else if(arg0.getSource()==options.getSnapshotButton()){
			if(canvas.getIsFixSnapshot() == true){
				canvas.setFixSnapshot(false);
				canvas.setSnapshotEmpty();
			}
				canvas.setState(InformationCanvas.State.SNAPSHOT);
		}
		else if(arg0.getSource()==options.getEventButton()){
			canvas.setState(InformationCanvas.State.EVENT);
		}
		else if(arg0.getSource()==options.getStartButton()){
	
		}
		else if(arg0.getSource()==options.getMoveSLButton()){
			canvas.setState(InformationCanvas.State.MOVE);
		}
		else if(arg0.getSource()==options.getStopMovingSLButton()){
			canvas.setState(InformationCanvas.State.EVENT);	
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		String texto;
		int numero;
		if(arg0.getSource()==options.getNumProcessText()){
			if(arg0.getKeyChar()=='\n'){
				texto = options.getNumProcessText().getText();
				numero = Integer.parseInt(texto);
				canvas.setNumProcess(numero);
				canvas.newForcedValues(numero);
				canvas.repaint();
			}
		}
		else if(arg0.getSource()==options.getTimeUnitText()){
			if(arg0.getKeyChar()=='\n'){
				texto = options.getTimeUnitText().getText();
				numero = Integer.parseInt(texto);
				canvas.setTimeUnit(numero);
				canvas.repaint();
			}
		}
	}
	
}
