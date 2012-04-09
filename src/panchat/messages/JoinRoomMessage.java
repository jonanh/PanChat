package panchat.messages;

import java.io.Serializable;

import order.Message;

import panchat.data.ChatRoom;
import panchat.data.User;

public class JoinRoomMessage implements Serializable, Message.Unicast,
		Message.Causal, Message.Total {

	private static final long serialVersionUID = 1L;

	public ChatRoom room;
	public User user;
	public boolean joing;

	/**
	 * Actualiza el estado de pertenencia de un usuario dentro de un canal
	 */
	public JoinRoomMessage(ChatRoom pRoom, User pUser, boolean pJoin) {
		this.room = pRoom;
		this.user = pUser;
		this.joing = pJoin;
	}
}
