package panchat.order;

import java.util.List;

import panchat.clocks.LamportClock;
import panchat.data.User;
import panchat.messages.Message;
import panchat.messages.Message.Type;

public class TotalOrderLinker extends OrderLayer {

	private LamportClock clock = new LamportClock();

	private class TotalMessage implements Message.Fifo {
		LamportClock clock;
	}

	public TotalOrderLinker(User user) {
		super(user);
	}

	public class TotalSendMsg implements Message.Fifo, Message.Total {
		int msgReference;
		LamportClock clock;
	}

	public class TotalProposalMsg implements Message.Fifo, Message.Total {
		int msgReference;
		LamportClock clock;
	}

	public class TotalFinalMsg implements Message.Fifo, Message.Total {
		LamportClock clock;
	}

	/**
	 * Enviar mensaje a multiples usuarios
	 * 
	 * @param users
	 * @param msg
	 */
	@Override
	public synchronized void sendMsg(List<User> users, Message msg) {
		// Incrementamos el reloj, y añadimos el reloj al mensaje
		clock.tick();
		msg.setClock(orderCapability(), clock);

		// Añadimos el mensaje a la cola de pendientes
		this.pendingQueue.add(msg.clone());

		// Enviamos el mensaje
		super.sendMsg(users, msg);
	}

	/**
	 * - To send a multicast message, a process sends a timestamped message to
	 * all the destination processes.
	 */
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
}
