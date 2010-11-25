package panchat.messages;

import panchat.data.User;

@SuppressWarnings("serial")
public class FifoMessage extends Message {

	private int num;

	/**
	 * @param pMessage
	 *            El contenido del mensaje
	 * @param pMessageNum
	 *            El número del mensaje al destinatario
	 */
	public FifoMessage(Object pMessage, User pUsuario, int pMessageNum) {

		super(pMessage, pUsuario);
		num = pMessageNum;
	}

	/**
	 * @return La MatrixClock asociada al mensaje.
	 */
	public int getMessageNum() {
		return num;
	}
}
