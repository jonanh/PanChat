package panchat.share;

import java.io.IOException;

import panchat.share.excepciones.TimeoutException;

public class ConexionEnvio<T> extends Trabajo {

	private Conexion conexion;
	private T elementoAEnviar;
	private boolean timeout = false;

	public ConexionEnvio(Conexion pConexion, T ElementoAEnviar) {

		TimeoutThread.getInstance().anyadirTrabajo(this);

		conexion = pConexion;
		elementoAEnviar = ElementoAEnviar;
	}

	public void enviar() throws IOException, TimeoutException {
		try {
			conexion.getObjectOutputStream().writeObject(elementoAEnviar);
		} catch (IOException e) {
			if (timeout) {
				System.out.println("ConexionEnvio.class : Timeout");
				throw new TimeoutException();
			} else
				throw e;
		}
		TimeoutThread.getInstance().eliminarTrabajo(this);
	}

	@Override
	public void cancelarTrabajo() {
		try {
			timeout = true;
			conexion.getSocket().close();
		} catch (IOException e) {
			String mensaje = "Error cancelando trabajo : a "
					+ conexion.getSocket() + " el " + elementoAEnviar;
			System.out.println(mensaje);
		}
	}

	@Override
	public long getTrabajoTimeOut() {
		return 3000;
	}
}
