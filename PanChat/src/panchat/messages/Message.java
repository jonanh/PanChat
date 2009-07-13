package panchat.messages;

import java.io.Serializable;

/**
 * Mensaje genérico
 * 
 * @param <T>
 */
public abstract class Message<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T content;

	/**
	 * @param pMessage
	 *            Contenido del mensaje
	 */
	public Message(T pMessage) {
		this.content = pMessage;
	}

	/**
	 * @return El contenido del mensaje
	 */
	public T getContent() {
		return content;
	}
}
