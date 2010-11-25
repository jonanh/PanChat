package panchat.protocol;

import java.io.Serializable;
import java.util.LinkedList;

import panchat.data.ChatRoom;

public class SaludoListaCanales implements Serializable {

	private static final long serialVersionUID = 1L;

	public LinkedList<ChatRoom> lista;

	public SaludoListaCanales(LinkedList<ChatRoom> pLista) {
		this.lista = pLista;
	}
}
