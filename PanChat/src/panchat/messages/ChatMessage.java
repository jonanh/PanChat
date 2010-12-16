package panchat.messages;

import java.io.Serializable;

import panchat.data.ChatRoom;

public class ChatMessage implements Serializable, Message.Unicast,
		Message.Causal, Message.Total {

	private static final long serialVersionUID = 1L;

	public String mensaje;

	public ChatRoom chatroom;

	public ChatMessage(ChatRoom pChatRoom, String pMessage) {
		this.chatroom = pChatRoom;
		this.mensaje = pMessage;
	}
}
