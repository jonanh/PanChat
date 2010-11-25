package panchat.protocol;

import java.io.Serializable;

import panchat.data.ChatRoom;
import panchat.data.User;

public class JoinChannel implements Serializable {

	private static final long serialVersionUID = 1L;

	public ChatRoom channel;
	public User user;
	public boolean joing;

	/**
	 * Actualiza el estado de pertenencia de un usuario dentro de un canal
	 */
	public JoinChannel(ChatRoom pChannel, User pUser, boolean pJoin) {
		this.channel = pChannel;
		this.user = pUser;
		this.joing = pJoin;
	}
}
