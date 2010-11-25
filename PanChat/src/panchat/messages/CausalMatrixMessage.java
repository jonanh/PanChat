package panchat.messages;

import panchat.data.User;
import panchat.messages.clocks.CausalMatrix;

@SuppressWarnings("serial")
public class CausalMatrixMessage extends Message {

	private CausalMatrix causalMatrix;

	/**
	 * @param pMessage
	 *            El contenido del mensaje
	 * @param pCausalMatrix
	 *            El reloj de Lamport del mensaje.
	 */
	public CausalMatrixMessage(Object pMessage, User pUsuario,
			CausalMatrix pCausalMatrix) {

		super(pMessage, pUsuario);
		causalMatrix = pCausalMatrix;
	}

	/**
	 * @return La MatrixClock asociada al mensaje.
	 */
	public CausalMatrix getCausalMatrix() {
		return causalMatrix;
	}
}