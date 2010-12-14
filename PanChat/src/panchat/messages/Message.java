package panchat.messages;

import java.io.Serializable;
import java.util.EnumMap;

import panchat.clocks.IClock;
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
	 * Interfaces para marcar los mensajes
	 */
	public interface Total {
	}

	public interface Causal {
	}

	public interface Fifo {
	}

	public interface Unicast {
	}

	public interface Multicast {
	}

	/*
	 * Atributos del mensaje
	 */
	private User user;

	private Object content;

	private EnumMap<Type, Boolean> properties = new EnumMap<Type, Boolean>(
			Type.class);

	private EnumMap<Type, IClock<?>> clocks = new EnumMap<Type, IClock<?>>(
			Type.class);

	/**
	 * 
	 * @param pMessage
	 *            El contenido del mensaje. Y el mensaje está marcado con las
	 *            interfaces definidas en esta clase, no hará falta establecer
	 *            las propiedades.
	 * @param pUser
	 *            El usuario de origen.
	 * 
	 * @param properties
	 *            Propiedades del mensaje.
	 */
	public Message(Object pMessage, User pUser, Type... properties) {
		this.user = pUser;
		this.content = pMessage;
		for (Type type : properties) {
			this.properties.put(type, true);
		}

		/*
		 * Comprobamos si el mensaje ha sido marcado mediante las interfaces.
		 */
		if (pMessage instanceof Total) {
			this.properties.put(Type.TOTAL, true);
		} else if (pMessage instanceof Causal) {
			this.properties.put(Type.CAUSAL, true);
		} else if (pMessage instanceof Fifo) {
			this.properties.put(Type.FIFO, true);
		} else if (pMessage instanceof Multicast) {
			this.properties.put(Type.MULTICAST, true);
		} else if (pMessage instanceof Unicast) {
			this.properties.put(Type.UNICAST, true);
		}
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
	public IClock<?> getClock(Type property) {
		return clocks.get(property);
	}

	/**
	 * @return El contenido del mensaje
	 */
	public Object setClock(Type property, IClock<?> clock) {
		return clocks.put(property, clock);
	}

	/**
	 * Devuelve si el mensaje es del tipo property
	 * 
	 * @param property
	 * @return
	 */
	public Boolean isType(Type property) {
		return property == null || this.properties.containsKey(property);
	}

	/**
	 * 
	 * @param property
	 */
	public void removeType(Type property) {
		this.properties.remove(property);
	}

	@Override
	public String toString() {
		return "msg content :" + content.toString();
	}

	/**
	 * Construye una copia del mensaje
	 */
	public Message clone() {
		Message clone = new Message(content, user);
		clone.properties = this.properties.clone();
		for (Type type : Type.values()) {
			IClock<?> clock = this.getClock(type);
			if (clock != null)
				clone.setClock(type, clock.clone());
		}
		return clone;
	}
}
