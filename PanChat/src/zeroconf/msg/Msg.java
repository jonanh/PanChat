package zeroconf.msg;

import java.io.Serializable;
import java.util.*;

public class Msg<T extends Serializable> {
	private int sourceId, destionationId;
	private String messageType;
	private T msgBuf;

	/**
	 * 
	 * @param sourceId
	 * @param destinationId
	 * @param msgType
	 * @param buf
	 */
	public Msg(int sourceId, int destinationId, String messageType, T buf) {
		this.sourceId = sourceId;
		this.destionationId = destinationId;
		this.messageType = messageType;
		this.msgBuf = buf;
	}

	/**
	 * @return sourceId
	 */
	public int getSrcId() {
		return sourceId;
	}

	/**
	 * @return destinationId
	 */
	public int getDestId() {
		return destionationId;
	}

	/**
	 * @return messageType
	 */
	public String getTag() {
		return messageType;
	}

	/**
	 * @return message
	 */
	public T getMessage() {
		return msgBuf;
	}

	// TODO Un mensaje debería tener ¿un número asociado? ¿Porqué este número no
	// se pasa a traves de la constructora?

	// public int getMessageInt() {
	// StringTokenizer st = new StringTokenizer(msgBuf);
	// return Integer.parseInt(st.nextToken());
	// }

	// TODO Hay que poder leer un mensaje de un DataPacket y de ObjectBuffer, no
	// de un StringTokenizker.

	// public static Msg parseMsg(StringTokenizer st) {
	// int srcId = Integer.parseInt(st.nextToken());
	// int destId = Integer.parseInt(st.nextToken());
	// String tag = st.nextToken();
	// String buf = st.nextToken("#");
	// return new Msg(srcId, destId, tag, buf);
	// }

	/**
	 * 
	 */
	public String toString() {
		String s;
		s = "sourceId:" + sourceId + "\n";
		s += "destinationId" + destionationId + "\n";
		s += "messageType" + messageType + "\n";
		s += "message content" + msgBuf.toString() + "\n";
		return s;
	}
}
