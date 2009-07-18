package panchat.listeners;

import java.io.IOException;
import java.net.MulticastSocket;

import panchat.Panchat;
import panchat.connector.Connector;
import panchat.data.Usuario;
import panchat.messages.SaludoUsuario;

public class MulticastListenerThread extends Thread {

	public final static boolean DEBUG = false;

	private MulticastSocket socket;

	private Panchat panchat;

	private Connector connector;

	public MulticastListenerThread(MulticastSocket socket, Panchat panchat) {
		this.socket = socket;
		this.panchat = panchat;
	}

	public void run() {

		this.connector = panchat.getConnector();

		while (!socket.isClosed()) {

			try {
				// Leyendo objeto
				Object objeto = connector.leerMultiCastSocket();

				// Registrar nuevo usuario
				if (objeto instanceof SaludoUsuario) {
					registrarCliente((SaludoUsuario) objeto);

				}
			} catch (IOException e1) {
				// Se ha cerrado el socket
			}
		}

		printDebug("Terminado");
	}

	private void registrarCliente(SaludoUsuario saludoUsuario) {

		Usuario usuario = saludoUsuario.usuario;

		if (DEBUG) {
			System.out.println("MulticastListenerThread.java:"
					+ "Petición de cliente recibida");
			System.out.println("\tRecibido por " + panchat.getUsuario());
			System.out.println("\tRecibido Desde " + usuario);
		}

		// Si no contenemos a este usuario lo añadimos
		if (!panchat.getListaUsuarios().contains(usuario)) {

			/*
			 * Si es una acción de registrar lo registramos
			 */
			if (saludoUsuario.registrar) {

				/*
				 * Respondemos el saludo al usuario como buenos ciudadanos 0:-)
				 */
				printDebug("Repondemos el saludo al usuario");

				connector.enviarSaludo(true);

				/*
				 * Crear el socket
				 */
				printDebug("Creamos el socket");

				// Unos aceptan desde el ServerSocket y otros crean
				// sockets.

				if (usuario.uuid.compareTo(panchat.getUsuario().uuid) < 0)
					connector.connect(usuario);
				else
					connector.acceptConnect();

				/*
				 * Añadir elementos en el CausalLinker y el Linker
				 */
				printDebug("Añadimos elementos a los Linkers");

				panchat.getCausalLinker().anyadirUsuario(usuario);

				/*
				 * Añadir a la ListaUsuarios el usuario
				 */
				printDebug("Añadimos el usuario a ListaUsuarios");

				panchat.getListaUsuarios().añadirUsuario(usuario);

				/*
				 * Enviamos al usuario la información que tenemos sobre los
				 * canales.
				 */
				printDebug("Enviamos información sobre canales");

				// panchat.getCausalLinker().sendMsg(
				// usuario,
				// panchat.getListaConversaciones()
				// .getListaConversacionesCanal());
			}
		}
	}

	private void printDebug(String string) {
		String msgClase = "MulticastListenerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
