package panchat.addressing.channels;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class CanalComboBoxModel implements ComboBoxModel {

	private Canal canal;
	
	public CanalComboBoxModel(Canal canal) {
		this.canal = canal;
	}

	@Override
	public Object getSelectedItem() {
		return null;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		System.out.println(anItem);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
	}

	@Override
	public Object getElementAt(int index) {
		return canal.getUsuarioDesconectado(index);
	}

	@Override
	public int getSize() {
		return canal.getNumUsuariosDesconectados();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}
}
