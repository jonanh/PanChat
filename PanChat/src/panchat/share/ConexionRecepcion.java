package panchat.share;

import java.io.IOException;

import panchat.share.excepciones.*;
import panchat.share.protocolo.Protocolo;

public class ConexionRecepcion<T> extends Trabajo {

	private Conexion conexion;
	private Class<T> clazz;
	private boolean timeout = false;

	public ConexionRecepcion(Conexion pConexion, Class<T> pClazz) {

		TimeoutThread.getInstance().anyadirTrabajo(this);

		conexion = pConexion;
		clazz = pClazz;
	}

	public T recibir() throws IOException, ProtocolException, TimeoutException {
		try {

			Object objeto = conexion.getObjectInputStream().readObject();

			TimeoutThread.getInstance().eliminarTrabajo(this);

			return clazz.cast(objeto);

		} catch (IOException e) {
			if (timeout) {
				System.out.println("ConexionRecepcion.class : Timeout");
				throw new TimeoutException();
			} else
				throw e;
		} catch (Exception e) {
			throw new ProtocolException();
		}
	}

	@Override
	public void cancelarTrabajo() {
		try {
			timeout = true;

			try {

				if (Configuracion.ConexionRecepcion_DEBUG) {
					String mensaje = "ConexionRecepcion.class : Timeout, enviando Protocolo.ERROR_TIMEOUT_CANCELADO";
					System.out.println(mensaje);
				}

				ConexionEnvio<Protocolo> envio = new ConexionEnvio<Protocolo>(
						conexion, Protocolo.ERROR_TIMEOUT_CANCELADO);

				envio.enviar();

				conexion.getObjectOutputStream().flush();

			} catch (Exception e) {
				System.out
						.println("Conexion.class : Error enviando terminacion : "
								+ this);
			}

			conexion.getSocket().close();
		} catch (IOException e) {
			System.out.println("Error recibiendo trabajo : de " + conexion.getSocket());
			e.printStackTrace();
		}
	}

	@Override
	public long getTrabajoTimeOut() {
		return 3000;
	}
}