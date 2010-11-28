package panchat.linker.ordering;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Observable;

import panchat.data.User;
import panchat.linker.Linker;
import panchat.messages.Message;

public class FifoOrderLinker extends Linker {

	Hashtable<User, Integer> sendVector = new Hashtable<User, Integer>();
	Hashtable<User, Integer> receivedVector = new Hashtable<User, Integer>();

	public FifoOrderLinker(User myId, Linker[] pLinker) {
		super(myId, pLinker);
	}

	@Override
	public void addUser(User user) {
		sendVector.put(user, 0);
		receivedVector.put(user, 0);
	}

	@Override
	public void removeUser(User user) {
		sendVector.remove(user);
		receivedVector.remove(user);
	}

	@Override
	public void receive() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMsg(User user, Object msg) {
		sendVector.put(user, sendVector.get(user) + 1);

	}

	@Override
	public void sendMsg(LinkedList<User> users, Object msg) {
		for (User user : users)
			sendVector.put(user, sendVector.get(user) + 1);

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}
}
