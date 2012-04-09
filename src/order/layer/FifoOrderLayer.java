package order.layer;

import java.util.List;

import order.Message;
import order.Message.Type;
import order.clocks.VectorClock;

import panchat.data.User;

public class FifoOrderLayer extends OrderLayer {

	private VectorClock sendClock;
	private VectorClock receiveClock;

	int i = 0;

	public FifoOrderLayer(User user) {
		super(user);
		sendClock = new VectorClock(user, true);
		receiveClock = new VectorClock(user, false);
	}

	@Override
	public synchronized void sendMsg(List<User> users, Message msg,
			boolean answer) {
		// Añadir un tick en cada usuario
		for (User user : users)
			sendClock.send(user);

		msg.setClock(orderCapability(), sendClock.clone());
		super.sendMsg(users, msg, answer);
	}

	@Override
	public synchronized void sendMsg(User user, Message msg, boolean answer) {
		sendClock.send(user);
		msg.setClock(orderCapability(), sendClock.clone());
		super.sendMsg(user, msg, answer);
	}

	@Override
	protected receiveStatus okayToRecv(Message msg) {

		VectorClock vc = (VectorClock) msg.getClock(orderCapability());

		User sendUser = msg.getUsuario();

		if (vc.getValue(this.user) == receiveClock.getValue(sendUser) + 1) {
			receiveClock.send(sendUser);

			return receiveStatus.Receive;
		}

		return receiveStatus.Nothing;
	}

	@Override
	public Type orderCapability() {
		return Message.Type.FIFO;
	}

	@Override
	public String layerName() {
		return "FIFOLayer";
	}

	@Override
	public void addUser(User user) {
		sendClock.addUser(user);
		receiveClock.addUser(user);
	}

	@Override
	public void removeUser(User user) {
		sendClock.removeUser(user);
		receiveClock.removeUser(user);
	}

	public VectorClock getSendClock() {
		return this.sendClock;
	}

	public VectorClock getReceiveClock() {
		return this.receiveClock;
	}
}
