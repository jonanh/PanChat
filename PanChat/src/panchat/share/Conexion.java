package panchat.share;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import panchat.share.protocolo.Protocolo;
import panchat.share.Conexiones;

public class Conexion extends Trabajo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Socket socket;
	private transient ObjectInputStream ois;
	private transient ObjectOutputStream oos;
	private transient boolean timeout = false;

	public Conexion(Socket pSocket) {
		socket = pSocket;
		try {

			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			System.out
					.println("Conexion.class : Error construyendo el objeto Conexion : "
							+ this);
		}
	}

	@Override
	public int hashCode() {
		return socket.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Conexion) {

			Conexion conexionObj = (Conexion) obj;
			return this.socket.equals(conexionObj.socket);
		}
		return false;
	}

	@Override
	public void cancelarTrabajo() {

		if (Configuracion.Conexion_DEBUG) {
			String mensaje = "Conexion.class : timeout";
			System.out.println(mensaje);
		}

		timeout = true;
		/*
		 * Indicamos a la conexion que debe terminar.
		 */
		Conexiones.getInstance().eliminarConexion(this, false);

		/*
		 * Mandamos la se�al de TimeOut de conexion.
		 */
		try {

			if (Configuracion.Conexion_DEBUG) {
				String mensaje = "Conexion.class : timeout, enviando Protocolo.ERROR_CONEXION_TIMEOUT_CANCELADO";
				System.out.println(mensaje);
			}

			ConexionEnvio<Protocolo> envio = new ConexionEnvio<Protocolo>(this,
					Protocolo.ERROR_CONEXION_TIMEOUT_CANCELADO);

			envio.enviar();

			oos.flush();

		} catch (Exception e) {
			System.out.println("Conexion.class : Error enviando terminacion : "
					+ this);
		}

		/*
		 * Cerramos el socket.
		 */
		try {
			if (Configuracion.Conexion_DEBUG) {
				String mensaje = "Conexion.class : timeout, cerrando socket";
				System.out.println(mensaje);
			}

			socket.close();

		} catch (IOException e) {
		}
	}

	@Override
	public long getTrabajoTimeOut() {
		return 7000;
	}

	@Override
	public String toString() {
		return socket.toString();
	}

	public ObjectInputStream getObjectInputStream() {
		return ois;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return oos;
	}

	public Socket getSocket() {
		return socket;
	}

	public boolean getTimeout() {
		return timeout;
	}
}
