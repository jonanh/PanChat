package panchat.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.UUID;

public class UserList extends Observable {

	private static final long serialVersionUID = 1L;

	private ChatRoomList listaCanales;

	private LinkedList<User> userList;

	private HashMap<UUID, User> hashtableUsuarios;

	private Object mutex = new Object();

	/**
	 * Crea una nueva lista de usuarios
	 * 
	 * @param listaCanales
	 */
	public UserList(ChatRoomList listaCanales) {
		this.listaCanales = listaCanales;
		this.userList = new LinkedList<User>();
		this.hashtableUsuarios = new HashMap<UUID, User>();
	}

	/**
	 * Añade un usuario a la lista de usuarios
	 * 
	 * @param usuario
	 */
	public void add(User usuario) {
		synchronized (mutex) {
			if (!contains(usuario)) {
				hashtableUsuarios.put(usuario.uuid, usuario);

				userList.add(usuario);
				Collections.sort(userList);
				listaCanales.addUser(usuario);
				super.setChanged();
				super.notifyObservers();
			}
		}
	}

	/**
	 * Elimina un usuario de la lista de usuarios
	 * 
	 * @param usuario
	 */
	public void remove(User usuario) {
		synchronized (mutex) {
			hashtableUsuarios.remove(usuario.uuid);

			userList.remove(usuario);
			listaCanales.deleteUser(usuario);
			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Devuelve una copia de la lista de usuarios
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<User> getClonedUserList() {
		synchronized (mutex) {
			return (LinkedList<User>) userList.clone();
		}
	}

	/**
	 * Devuelve el usuario pedido
	 * 
	 * @param Index
	 * @return
	 */
	public User getUser(int index) {
		return userList.get(index);
	}

	/**
	 * Devuelve el usuario pedido
	 * 
	 * @param Index
	 * @return
	 */
	public User getUser(UUID nombre) {
		return hashtableUsuarios.get(nombre);
	}

	/**
	 * Contiene el usuario en la lista de usuarios
	 * 
	 * @param user
	 * @return
	 */
	public boolean contains(User user) {
		return hashtableUsuarios.containsKey(user.uuid);
	}

	/**
	 * Devuelve el número de usuarios
	 * 
	 * @return
	 */
	public int length() {
		return userList.size();
	}

	/**
	 * Devuelve la lista de usuarios
	 * 
	 * @return
	 */
	public LinkedList<User> getUserList() {
		return this.userList;
	}

	/*
	 * compareTo, equals y toString
	 */

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserList)
			return userList.equals(((UserList) obj).userList);
		else
			return false;
	}

	@Override
	public String toString() {
		return userList.toString();
	}
}
