package panchat.messages.register;

import java.io.Serializable;
import java.util.List;

import order.Message;

import panchat.data.ChatRoom;

public class RoomListMsg implements Serializable, Message.Unicast {

	private static final long serialVersionUID = 1L;

	public List<ChatRoom> roomList;

	public RoomListMsg(List<ChatRoom> pRoomList) {
		this.roomList = pRoomList;
	}
}
