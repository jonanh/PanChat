package panchat.messages;

import java.io.Serializable;

import panchat.data.User;

@SuppressWarnings("serial")
public class Message implements Serializable {

	private User address;

	private Object content;

	/**
	 * 
	 * @param pMessage
	 *            El contenido del mensaje.
	 * @param pAddress
	 *            La Dirección del destinatario.
	 */
	public Message(Object pMessage, User pAddress) {
		this.address = pAddress;
	}

	public User getUsuario() {
		return address;
	}

	/**
	 * @return El contenido del mensaje
	 */
	public Object getContent() {
		return content;
	}
}
