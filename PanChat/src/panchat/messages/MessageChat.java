package panchat.messages;

import java.io.Serializable;

import panchat.data.ChatRoom;

public class MessageChat implements Serializable {

	private static final long serialVersionUID = 1L;

	public String mensaje;

	public ChatRoom chatroom;

	public MessageChat(ChatRoom pChatRoom, String pMessage) {
		this.chatroom = pChatRoom;
		this.mensaje = pMessage;
	}
}
