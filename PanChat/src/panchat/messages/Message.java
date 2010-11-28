package panchat.messages;

import java.io.Serializable;

import panchat.data.User;

@SuppressWarnings("serial")
public class Message implements Serializable {

	/*
	 * Tipos de mensaje
	 */
	public enum type {
		FIFO, CAUSAL, TOTAL, CAUSALTOTAL
	}

	/**
	 * 
	 * El mensaje es enviado usando un socket unicast o un socket multicast.
	 */
	public enum channel {
		UNICAST, MULTICAST
	}

	/*
	 * Atributos del mensaje
	 */
	private User user;

	private Object content;

	/**
	 * 
	 * @param pMessage
	 *            El contenido del mensaje.
	 * @param pUser
	 *            El usuario de origen.
	 */
	public Message(Object pMessage, User pUser) {
		this.user = pUser;
	}

	public User getUsuario() {
		return user;
	}

	/**
	 * @return El contenido del mensaje
	 */
	public Object getContent() {
		return content;
	}
}
