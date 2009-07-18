package panchat.data.models;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import panchat.data.Canal;
import panchat.data.Usuario;

public class CanalComboBoxModel implements ComboBoxModel {

	private Canal canal;

	private Usuario usuario;

	public CanalComboBoxModel(Canal canal) {
		this.canal = canal;
		this.usuario = canal.getUsuarioDesconectado(0);
	}

	@Override
	public Object getSelectedItem() {
		return usuario;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		usuario = (Usuario) anItem;
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
