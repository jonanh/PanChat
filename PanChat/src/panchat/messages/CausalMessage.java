package panchat.messages;

import panchat.addressing.Address;
import panchat.clocks.MatrixClock;

/**
 * Mensaje genérico causal.
 * 
 * @author Jon Ander Hernández
 * 
 */
public class CausalMessage<T> extends SimpleMessage<T> {

	private static final long serialVersionUID = 1L;

	private MatrixClock matrixClock;

	/**
	 * @param pMessage
	 *            El contenido del mensaje
	 * @param pMatrixClock
	 *            La MatrixClock asociada al mensaje.
	 * @param pAddress
	 *            La Dirección del destinatario
	 */
	public CausalMessage(T pMessage, Address pAddress, MatrixClock pMatrixClock) {
		super(pMessage, pAddress);
		matrixClock = pMatrixClock;
	}

	/**
	 * @return La MatrixClock asociada al mensaje.
	 */
	public MatrixClock getMatrixClock() {
		return matrixClock;
	}
}
