package panchat.messages;

import panchat.addressing.Usuario;

/**
 * Mensaje simple (sin ordenación) p2p.
 * 
 * @author Jon Ander Hernández
 */
public class SimpleMessage extends Message {

	private static final long serialVersionUID = 1L;

	private Usuario address;

	/**
	 * 
	 * @param pMessage
	 *            El contenido del mensaje.
	 * @param pAddress
	 *            La Dirección del destinatario.
	 */
	public SimpleMessage(Object pMessage, Usuario pAddress) {
		super(pMessage);
		this.address = pAddress;
	}

	public Usuario getAddress() {
		return address;
	}

}
