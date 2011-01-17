package order.layer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import order.clocks.LamportClock;

import panchat.data.User;
import panchat.messages.Message;
import panchat.messages.Message.Type;

public class TotalOrderLinker extends OrderLayer {

	private LamportClock clock = new LamportClock();

	/*
	 * Estructura de datos que almacenamos por cada mensaje total enviado
	 */
	private class SendMessagePending {

		private List<User> userList;
		private List<User> responseUserList;
		LamportClock clock;

		SendMessagePending(LamportClock clock, List<User> userList) {
			this.clock = clock;
			for (User user : this.userList) {
				this.userList.add(user);
				this.responseUserList.add(user);
			}
		}

		/**
		 * @return Si hemos recibido una respuesta de todos
		 */
		boolean okayToRecv() {
			return responseUserList.size() == 0;
		}

	}

	/*
	 * Tramas para cada una de las fases de la ordenación total.
	 */
	public class TotalSendMsg implements Message.Fifo, Message.Total {
		int msgReference;
		LamportClock clock;
		Object content;
	}

	public class TotalProposalMsg implements Message.Fifo, Message.Total {
		int msgReference;
		LamportClock clock;
	}

	public class TotalFinalMsg implements Message.Fifo, Message.Total {
		LamportClock clock;
	}

	/*
	 * Usamos esta tabla hash para ir almacenando los diferentes relojes que nos
	 * van respondiendo.
	 */
	HashMap<Integer, Integer[]> values = new HashMap<Integer, Integer[]>();

	public TotalOrderLinker(User user) {
		super(user);
	}

	/**
	 * Enviar mensaje a multiples usuarios
	 * 
	 * @param users
	 * 
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
	protected receiveStatus okayToRecv(Message msg) {
		
		this.clock.tick();
		
		/*
		 * - On receiving a message, a process marks it as undeliverable and
		 * sends the value of the logical clock as the proposed timestamp to the
		 * initiator.
		 */
		if (msg.getContent() instanceof TotalSendMsg) {
//			this.undeliverable.add(msg);
			
			TotalProposalMsg proposal = new TotalProposalMsg();
			proposal.clock = this.clock.clone();
			
//			super.sendMsg(msg.getUsuario());
			
			return receiveStatus.Delete;
		}
		/*
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
//		if (msg instanceof )
		
		return receiveStatus.Nothing;
	}

	@Override
	public Type orderCapability() {
		return Message.Type.TOTAL;
	}
}
