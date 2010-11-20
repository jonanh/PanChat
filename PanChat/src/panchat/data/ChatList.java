package panchat.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import panchat.Panchat;
import panchat.ui.chat.ChatWindow;
import panchat.ui.chat.ChatRoomWindow;

public class ChatList {

	private static final long serialVersionUID = 1L;

	// Ventanas de conversaciones
	private HashMap<User, ChatWindow> chatWindowHashMap;

	private HashMap<ChatRoom, ChatRoomWindow> ventanaConversacionesCanales;

	private Panchat panchat;

	private Object mutex = new Object();

	/**
	 * Creamos lista de conversaciones
	 * 
	 * Esta clase sirve para reconstruir toda la información de los canales a
	 * los nuevos usuarios. Y también para gestionar las ventanas de
	 * conversaciones existentes.
	 */
	public ChatList(Panchat pPanchat) {
		this.panchat = pPanchat;

		this.chatWindowHashMap = new HashMap<User, ChatWindow>();

		this.ventanaConversacionesCanales = new HashMap<ChatRoom, ChatRoomWindow>();
	}

	public void delete(User usuario) {
		chatWindowHashMap.remove(usuario);
	}

	public void delete(ChatRoom canal) {
		ventanaConversacionesCanales.remove(canal);
	}

	public ChatWindow getChatWindow(User user) {
		if (!chatWindowHashMap.containsKey(user)) {
			ChatWindow window = new ChatWindow(panchat,
					user);
			chatWindowHashMap.put(user, window);
			return window;
		} else {
			return chatWindowHashMap.get(user);
		}
	}
	
	public ChatRoomWindow getChatRoomWindow(ChatRoom chatroom) {
		if (!ventanaConversacionesCanales.containsKey(chatroom)) {
			ChatRoomWindow window = new ChatRoomWindow(
					panchat, chatroom);
			ventanaConversacionesCanales.put(chatroom, window);
			return window;
		} else {
			return ventanaConversacionesCanales.get(chatroom);
		}
	}

	public LinkedList<ChatRoom> getListaConversacionesCanal() {
		synchronized (mutex) {
			/*
			 * Creamos a partir de la tabla hash de conversaciones, todas las
			 * conversaciones en las que estamos
			 */
			LinkedList<ChatRoom> list = new LinkedList<ChatRoom>();

			Iterator<Entry<ChatRoom, ChatRoomWindow>> iter;
			iter = ventanaConversacionesCanales.entrySet().iterator();

			while (iter.hasNext()) {
				list.add(iter.next().getKey());
			}
			return list;
		}
	}
}
