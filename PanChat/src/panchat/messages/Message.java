package panchat.messages;

import java.io.Serializable;

/**
 * Mensaje genérico
 * 
 * @param <T>
 */
public abstract class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object content;

	/**
	 * @param pMessage
	 *            Contenido del mensaje
	 */
	public Message(Object pMessage) {
		this.content = pMessage;
	}

	/**
	 * @return El contenido del mensaje
	 */
	public Object getContent() {
		return content;
	}
}
