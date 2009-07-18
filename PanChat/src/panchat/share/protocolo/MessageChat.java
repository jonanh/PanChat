package panchat.share.protocolo;

import java.io.Serializable;

import panchat.data.Canal;

public class MessageChat implements Serializable {

	private static final long serialVersionUID = 1L;

	public String mensaje;

	public Canal canal;

	public MessageChat(Canal pCanal, String pMensaje) {
		this.canal = pCanal;
		this.mensaje = pMensaje;
	}
}
