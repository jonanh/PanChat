package order;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;

import order.clocks.IClock;

import panchat.data.User;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Tipos de mensaje
	 */
	public static enum Type {
		TOTAL, CAUSAL, FIFO, UNICAST, MULTICAST
	}

	/*
	 * Interfaces para marcar el contenido de los mensajes. De modo que podamos
	 * determinar las propiedades del mensaje mediante el contenido.
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

	// Lista de propiedades
	private EnumMap<Type, Boolean> properties = new EnumMap<Type, Boolean>(
			Type.class);

	// Lista de relojes lógicos
	private EnumMap<Type, IClock<?>> clocks = new EnumMap<Type, IClock<?>>(
			Type.class);

	// Lista de usuarios para usarse en canales multicast
	private List<User> userList;

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
	public Message(Object pMessage, User pUser,
			EnumMap<Type, Boolean> properties) {
		this.user = pUser;
		this.content = pMessage;
		this.properties = properties.clone();
	}

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
		if (pMessage instanceof Total)
			this.properties.put(Type.TOTAL, true);
		if (pMessage instanceof Causal)
			this.properties.put(Type.CAUSAL, true);
		if (pMessage instanceof Fifo)
			this.properties.put(Type.FIFO, true);
		if (pMessage instanceof Multicast)
			this.properties.put(Type.MULTICAST, true);
		if (pMessage instanceof Unicast)
			this.properties.put(Type.UNICAST, true);
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
	 * Establecemos los destinatarios del mensajes. Fundamentalmente empleamos
	 * esta funcionalidad en canales multicast, o con mensajes que requieran
	 * "consenso", ya que necesitan conocer si el mensaje es para ellos.
	 * 
	 * @param list
	 */
	public void setUserList(List<User> list) {
		this.userList = list;
	}

	/**
	 * @return Lista de destinatarios
	 */
	public List<User> getUserList() {
		return this.userList;
	}

	/**
	 * Devuelve si el mensaje es del tipo property
	 * 
	 * @param property
	 * @return
	 */
	public Boolean isType(Type property) {
		return property == null || this.properties.containsKey(property)
				&& this.properties.get(property) == true;
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

		String type = "";
		if (isType(Type.FIFO))
			type += "FIFO";
		if (isType(Type.CAUSAL))
			type += "Causal";
		if (isType(Type.TOTAL))
			type += "Total";
		if (type == "")
			type = "Simple";

		return type + ":" + content.toString();
	}

	public EnumMap<Type, Boolean> getProperties() {
		return properties;
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
