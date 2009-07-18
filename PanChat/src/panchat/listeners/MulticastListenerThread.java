package panchat.listeners;

import java.io.IOException;
import java.net.MulticastSocket;

import panchat.Panchat;
import panchat.connector.Connector;
import panchat.share.protocolo.SaludoUsuario;

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

	private void registrarCliente(SaludoUsuario msgCliente) {

		if (DEBUG) {
			System.out.println("MulticastListenerThread.java:"
					+ "Petición de cliente recibida");
			System.out.println("\tRecibido por " + panchat.getUsuario());
			System.out.println("\tRecibido Desde " + msgCliente.getUsuario());
		}

		// Si no contenemos a este usuario lo añadimos
		if (!panchat.getListaUsuarios().contains(msgCliente.getUsuario())) {

			/*
			 * Si es una acción de registrar lo registramos
			 */
			if (msgCliente.isRegistrar()) {

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

				if (msgCliente.getUsuario().uuid
						.compareTo(panchat.getUsuario().uuid) < 0)
					connector.connect(msgCliente.getUsuario());
				else
					connector.acceptConnect();

				/*
				 * Añadir elementos en el CausalLinker y el Linker
				 */
				printDebug("Añadimos elementos a los Linkers");

				panchat.getCausalLinker().anyadirUsuario(
						msgCliente.getUsuario());

				panchat.getLinker().anyadirUsuario(msgCliente.getUsuario());

				/*
				 * Añadir a la ListaUsuarios el usuario
				 */
				printDebug("Añadimos el usuario a ListaUsuarios");

				panchat.getListaUsuarios().añadirUsuario(
						msgCliente.getUsuario());
			}
		}
	}

	private void printDebug(String string) {
		String msgClase = "MulticastListenerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
