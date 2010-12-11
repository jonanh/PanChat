package panchat.order;

import java.util.LinkedList;

import panchat.clocks.LamportClock;
import panchat.data.User;
import panchat.messages.Message;
import panchat.messages.Message.Type;

public class TotalOrderLinker extends OrderLayer {

	/*
	 * - To send a multicast message, a process sends a timestamped message to
	 * all the destination processes.
	 */
	private LinkedList<Message> undeliverableQueue = new LinkedList<Message>();

	private LinkedList<User> userList = new LinkedList<User>();

	private LamportClock clock = new LamportClock();

	/*
	 * Clase para las respuestas
	 */
	static enum TotalMessageType {
		proposal, ack
	}

	private class TotalMessage {
		TotalMessageType type;
		LamportClock clock;
	}

	public TotalOrderLinker(User user) {
		super(user);
	}

	@Override
	public synchronized void sendMsg(LinkedList<User> users, Message msg) {
		clock.tick();
		msg.setClock(orderCapability(), clock);
		super.sendMsg(users, msg);
	}

	@Override
	public synchronized void sendMsg(User user, Message msg) {
		clock.tick();
		msg.setClock(orderCapability(), clock);
		super.sendMsg(user, msg);
	}

	@Override
	protected boolean okayToRecv(Message msg) {
		/*
		 * - On receiving a message, a process marks it as undeliverable and
		 * sends the value of the logical clock as the proposed timestamp to the
		 * initiator.
		 * 
		 * - When the initiator has received all the proposed timestamps, it
		 * takes the maximum of all proposals and assigns that timestamp as the
		 * final timestamp to that message. This value is sent to all the
		 * destinations.
		 * 
		 * - On receiving the final timestamp of a message, it is marked as
		 * deliverable.
		 * 
		 * - A deliverable message is delivered to the site if it has the
		 * smallest timestamp in the message queue.
		 */
		return false;
	}

	@Override
	public Type orderCapability() {
		return Message.Type.TOTAL;
	}

	@Override
	public void addUser(User user) {
		userList.add(user);
	}

	@Override
	public void removeUser(User user) {
		userList.remove(user);
	}
}
