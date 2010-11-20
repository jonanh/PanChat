package panchat.messages;

import panchat.data.User;

/**
 * Mensaje simple (sin ordenación) p2p.
 * 
 * @author Jon Ander Hernández
 */
public class SimpleMessage extends Message {

	private static final long serialVersionUID = 1L;

	private User address;

	/**
	 * 
	 * @param pMessage
	 *            El contenido del mensaje.
	 * @param pAddress
	 *            La Dirección del destinatario.
	 */
	public SimpleMessage(Object pMessage, User pAddress) {
		super(pMessage);
		this.address = pAddress;
	}

	public User getUsuario() {
		return address;
	}

}
