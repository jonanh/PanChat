package panchat.linker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;

import panchat.data.User;
import panchat.messages.Message;

public class VirtualSocketLinker extends Linker {

	public VirtualSocketLinker(User myId, Linker[] pLinker) {
		super(myId, pLinker);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message handleMsg() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receive() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMsg(User destId, Object msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMsg(LinkedList<User> destIds, Object msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}
	
	

}
