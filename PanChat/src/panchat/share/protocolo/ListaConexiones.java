package panchat.share.protocolo;

import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;

public class ListaConexiones implements Serializable {

	private static final long serialVersionUID = 1L;

	private LinkedList<Socket> listaSockets;

	public ListaConexiones(LinkedList<Socket> pListaSockets) {
		listaSockets = pListaSockets;
	}

	public LinkedList<Socket> getListaConexiones() {
		return listaSockets;
	}
}
