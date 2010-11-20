package panchat.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

public class ChatRoomList extends Observable {

	private static final long serialVersionUID = 1L;

	private LinkedList<ChatRoom> channelList;

	private HashMap<String, ChatRoom> channelHashTable;

	private Object mutex = new Object();

	/**
	 * Nueva lista de canales
	 */
	public ChatRoomList() {
		channelList = new LinkedList<ChatRoom>();
		channelHashTable = new HashMap<String, ChatRoom>();
	}

	/**
	 * Registra un nuevo canal en la lista de canales
	 * 
	 * @param canal
	 */
	public void addChannel(ChatRoom canal) {
		synchronized (mutex) {
			if (!channelHashTable.containsKey(canal.getName())) {

				channelHashTable.put(canal.getName(), canal);

				channelList.add(canal);
				Collections.sort(channelList);

				super.setChanged();
				super.notifyObservers();
			}
		}
	}

	/**
	 * Registra un nuevo canal en la lista de canales
	 * 
	 * @param canal
	 */
	public void deleteChannel(ChatRoom canal) {
		synchronized (mutex) {
			if (channelHashTable.containsKey(canal.getName())) {

				channelHashTable.remove(canal.getName());

				channelList.remove(canal);

				super.setChanged();
				super.notifyObservers();
			}
		}
	}

	/**
	 * Añade el nuevo usuario a todos los canales como nuevo usuario
	 * desconectado
	 * 
	 * @param usuario
	 */
	public void addUser(User usuario) {
		synchronized (mutex) {
			for (ChatRoom channel : channelList)
				channel.addUser(usuario);

			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Elimina el usuario de todos los canales
	 * 
	 * @param usuario
	 */
	public void deleteUser(User usuario) {
		synchronized (mutex) {
			for (ChatRoom channel : channelList)
				channel.removeUser(usuario);

			super.setChanged();
			super.notifyObservers();
		}
	}

	/**
	 * Devuelve el canal según la posición index
	 * 
	 * @param index
	 * @return
	 */
	public ChatRoom getChannel(int index) {
		// Comprobar límites
		if (index >= 0 && index < length())
			return channelList.get(index);
		else
			return null;
	}

	/**
	 * Devuelve el número de canales que posee la lista de canales
	 * 
	 * @return
	 */
	public int length() {
		return channelList.size();
	}

	/**
	 * Devuelve el canal según la posición index
	 * 
	 * @param index
	 * @return
	 */
	public ChatRoom getChannel(String nombre) {
		return this.channelHashTable.get(nombre);
	}

	/**
	 * La información de uno de los canales ha sido modificada, actualizar la
	 * vista de canales
	 */
	public void setModified() {
		super.setChanged();
		super.notifyObservers();
	}

	/*
	 * Equals
	 */

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChatRoomList)
			return channelList.equals(((ChatRoomList) obj).channelList);
		else
			return false;
	}
}
