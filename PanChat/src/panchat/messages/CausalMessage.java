package panchat.messages;

import panchat.data.User;
import panchat.messages.clocks.LamportClock;

@SuppressWarnings("serial")
public class CausalMessage extends Message {

	private LamportClock lamportClock;

	/**
	 * @param pMessage
	 *            El contenido del mensaje
	 * @param pCausalMatrix
	 *            El reloj de Lamport del mensaje.
	 */
	public CausalMessage(Object pMessage, User pUsuario,
			LamportClock pLamportClock) {

		super(pMessage, pUsuario);
		lamportClock = pLamportClock;
	}

	/**
	 * @return La MatrixClock asociada al mensaje.
	 */
	public LamportClock getLamportClock() {
		return lamportClock;
	}
}