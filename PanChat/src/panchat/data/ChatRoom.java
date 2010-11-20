package panchat.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Observable;

public class ChatRoom extends Observable implements Comparable<ChatRoom>,
		Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private transient LinkedList<User> listadoUsuariosConectados;
	private transient LinkedList<User> listadoUsuariosDesconectados;
	private transient UserList userList;

	private transient Object mutex = new Object();

	/**
	 * Crea un nuevo canal
	 * 
	 * Esta constructora solo debe usarse cuando se desea avisar a traves de un
	 * socket de la existencia de un nuevo canal.
	 * 
	 * @param name
	 */
	public ChatRoom(String name) {
		this.name = name;
	}

	/**
	 * Crea un nuevo canal
	 * 
	 * @param name
	 * @param userList
	 */
	public ChatRoom(String name, UserList userList) {
		this.name = name;
		this.listadoUsuariosConectados = new LinkedList<User>();
		this.listadoUsuariosDesconectados = userList
				.getClonedUserList();
		this.userList = userList;
	}

	/**
	 * Devuelve el nombre del canal
	 */
	public String getName() {
		return name;
	}

	/**
	 * Método para eleminiar un nuevo usuario a la conversación.
	 * 
	 * @param usuario
	 */
	public void addUser(User usuario) {
		synchronized (mutex) {
			listadoUsuariosDesconectados.add(usuario);

			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Método para eleminiar un nuevo usuario a la conversación.
	 * 
	 * @param usuario
	 */
	public void removeUser(User usuario) {
		synchronized (mutex) {
			listadoUsuariosDesconectados.remove(usuario);
			listadoUsuariosConectados.remove(usuario);

			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Método para añadir un nuevo usuario a la conversación.
	 * 
	 * @param usuario
	 */
	public void joinUser(User usuario) {
		synchronized (mutex) {
			listadoUsuariosConectados.add(usuario);
			listadoUsuariosDesconectados.remove(usuario);

			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Método para añadir un nuevo usuario a la conversación.
	 * 
	 * @param usuario
	 */
	public void leaveUser(User usuario) {
		synchronized (mutex) {
			listadoUsuariosConectados.remove(usuario);
			listadoUsuariosDesconectados.add(usuario);

			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Devuelve el elemento conectados cuyo indice es index
	 * 
	 * @param index
	 * @return
	 */
	public User getUsuarioConectado(int index) {
		return listadoUsuariosConectados.get(index);
	}

	/**
	 * Devuelve el elemento desconectados cuyo indice es index
	 * 
	 * @param index
	 * @return
	 */
	public User getUsuarioDesconectado(int index) {
		return listadoUsuariosDesconectados.get(index);
	}

	/**
	 * Devuelve el número de usuarios conectados
	 * 
	 * @return
	 */
	public int getNumUsuariosConectados() {
		return listadoUsuariosConectados.size();
	}

	/**
	 * Devuelve el número de usuarios sin conectar
	 * 
	 * @return
	 */
	public int getNumUsuariosDesconectados() {
		return userList.length()
				- listadoUsuariosConectados.size();
	}

	/**
	 * Compueba si el usuario está conectado.
	 * 
	 * @param usuario
	 * @return
	 */
	public boolean contains(User usuario) {
		synchronized (mutex) {
			return listadoUsuariosConectados.contains(usuario);
		}
	}

	/**
	 * Devuelve el listado de usuario del canal
	 * 
	 * @return
	 */
	public LinkedList<User> getUserList() {
		synchronized (mutex) {
			return this.listadoUsuariosConectados;
		}
	}

	/*
	 * compareTo, equals y toString
	 */

	@Override
	public int compareTo(ChatRoom o) {
		return name.compareTo(o.name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChatRoom) {
			ChatRoom canal = (ChatRoom) obj;
			return name.equals(canal.name);
		} else
			return false;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
