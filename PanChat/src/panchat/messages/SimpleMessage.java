package panchat.messages;

import panchat.addressing.Address;

/**
 * Mensaje simple (sin ordenación) p2p.
 * 
 * @author Jon Ander Hernández
 */
public class SimpleMessage<T> extends Message<T> {

	private static final long serialVersionUID = 1L;

	private Address address;

	/**
	 * 
	 * @param pMessage
	 *            El contenido del mensaje.
	 * @param pAddress
	 *            La Dirección del destinatario.
	 */
	public SimpleMessage(T pMessage, Address pAddress) {
		super(pMessage);
		this.address = pAddress;
	}

	public Address getAddress() {
		return address;
	}

}
