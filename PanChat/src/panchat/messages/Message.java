package panchat.messages;

import java.io.Serializable;
import java.util.EnumMap;

import panchat.data.User;

@SuppressWarnings("serial")
public class Message implements Serializable {

	/*
	 * Tipos de mensaje
	 */
	public static enum Type {
		TOTAL, CAUSAL, FIFO, UNICAST, MULTICAST
	}

	/*
	 * Atributos del mensaje
	 */
	private User user;

	private Object content;

	private EnumMap<Type, Boolean> properties = new EnumMap<Type, Boolean>(
			Type.class);

	private EnumMap<Type, Object> clocks = new EnumMap<Type, Object>(Type.class);

	/**
	 * 
	 * @param pMessage
	 *            El contenido del mensaje.
	 * @param pUser
	 *            El usuario de origen.
	 */
	public Message(Object pMessage, User pUser, Type... properties) {
		for (Type type : properties) {
			this.properties.put(type, true);
		}
		this.user = pUser;
	}

	/**
	 * 
	 * @return Devuelve el número del proceso
	 */
	public User getUsuario() {
		return user;
	}

	/**
	 * @return El contenido del mensaje
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * @return El contenido del mensaje
	 */
	public Object getClock(Type property) {
		return clocks.containsKey(property);
	}

	/**
	 * @return El contenido del mensaje
	 */
	public Object setClock(Type property, Object clock) {
		return clocks.put(property, clock);
	}

	/**
	 * Devuelve si el mensaje es del tipo property
	 * 
	 * @param property
	 * @return
	 */
	public Boolean isType(Type property) {
		return this.properties.containsKey(property);
	}

	/**
	 * 
	 * @param property
	 */
	public void removeType(Type property) {
		this.properties.remove(property);
	}
}
