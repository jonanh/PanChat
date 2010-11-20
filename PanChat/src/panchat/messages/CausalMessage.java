package panchat.messages;

import panchat.data.User;
import panchat.linker.CausalMatrix;

/**
 * Mensaje genérico causal.
 * 
 * @author Jon Ander Hernández
 * 
 */
public class CausalMessage extends SimpleMessage {

	private static final long serialVersionUID = 1L;

	private CausalMatrix causalMatrix;

	/**
	 * @param pMessage
	 *            El contenido del mensaje
	 * @param pCausalMatrix
	 *            La MatrixClock asociada al mensaje.
	 */
	public CausalMessage(Object pMessage, User pUsuario, CausalMatrix pCausalMatrix) {
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
