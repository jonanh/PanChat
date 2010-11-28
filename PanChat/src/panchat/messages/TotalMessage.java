package panchat.messages;

import panchat.clocks.CausalMatrix;
import panchat.data.User;

@SuppressWarnings("serial")
public class TotalMessage extends Message {

	private CausalMatrix causalMatrix;

	/**
	 * @param pMessage
	 *            El contenido del mensaje
	 * @param pCausalMatrix
	 *            La MatrixClock asociada al mensaje.
	 */
	public TotalMessage(Object pMessage, User pUsuario,
			CausalMatrix pCausalMatrix) {

		super(pMessage, pUsuario);
		causalMatrix = pCausalMatrix;
	}

	/**
	 * @return La MatrixClock asociada al mensaje.
	 */
	public CausalMatrix getMatrix() {
		return causalMatrix;
	}
}
