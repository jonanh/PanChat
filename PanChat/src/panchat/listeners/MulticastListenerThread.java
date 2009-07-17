package panchat.listeners;

import java.io.IOException;
import java.net.MulticastSocket;

import panchat.Panchat;
import panchat.channels.Canal;
import panchat.connector.Connector;
import panchat.share.protocolo.SaludoUsuario;
import panchat.share.protocolo.UsuarioCanal;

public class MulticastListenerThread extends Thread {

	public final static boolean DEBUG = false;

	private MulticastSocket socket;

	private Panchat panchat;

	private Connector connector;

	public MulticastListenerThread(MulticastSocket socket, Panchat panchat,
			Connector connector) {
		this.socket = socket;
		this.panchat = panchat;
		this.connector = connector;
	}

	public void run() {
		while (!socket.isClosed()) {

			try {
				Object objeto = connector.leerMultiCastSocket();

				// Registrar nuevo usuario
				if (objeto instanceof SaludoUsuario) {

					registrarCliente((SaludoUsuario) objeto);

				}
				// Registrar nuevo canal
				else if (objeto instanceof Canal) {

					registrarCanal((Canal) objeto);

				}
				// Registrar usuario en canal
				else if (objeto instanceof UsuarioCanal) {

					tratarUsuarioCanal((UsuarioCanal) objeto);

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

	private void registrarCanal(Canal pCanal) {

		// Obtener nombre
		String nombreCanal = pCanal.getNombreCanal();

		// Crear objeto
		Canal canal = new Canal(nombreCanal, panchat.getListaUsuarios());

		// registrar
		panchat.getListaCanales().añadirCanal(canal);
	}

	private void tratarUsuarioCanal(UsuarioCanal objeto) {

	}

	private void printDebug(String string) {
		String msgClase = "MulticastListenerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
