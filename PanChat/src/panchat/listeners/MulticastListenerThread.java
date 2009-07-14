package panchat.listeners;

import java.net.MulticastSocket;

import panchat.Panchat;
import panchat.linker.Connector;
import panchat.share.protocolo.RegistrarCliente;

public class MulticastListenerThread extends Thread {

	public static boolean DEBUG = false;

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
		while (true) {
			if (DEBUG) {
				System.out.println("Esperando...");
			}
			RegistrarCliente msgCliente = RegistrarCliente
					.leerRegistrarCliente(socket);

			if (DEBUG) {
				System.out.println("MulticastListenerThread.java:"
						+ "Petición de cliente recibida");
				System.out.println("\tRecibido por " + panchat.getUsuario());
				System.out.println("\tRecibido Desde "
						+ msgCliente.getUsuario());
			}

			// Si no contenemos a este usuario lo añadimos
			if (!panchat.getListaUsuarios().contains(msgCliente.getUsuario()))

				/*
				 * Si es una acción de registrar lo registramos
				 */
				if (msgCliente.isRegistrar()) {

					/*
					 * Respondemos el saludo al usuario como buenos ciudadanos
					 * 0:-)
					 */
					if (DEBUG)
						System.out.println("MulticastListenerThread.java: "
								+ "Repondemos el saludo al usuario");

					connector.enviarSaludo();
					
					/*
					 * Crear el socket
					 */
					if (DEBUG)
						System.out.println("MulticastListenerThread.java: "
								+ "Creamos el socket");

					/*
					 * Unos aceptan desde el ServerSocket y otros crean sockets.
					 */
					if (msgCliente.getUsuario().uuid.compareTo(panchat
							.getUsuario().uuid) < 0)
						connector.connect(msgCliente.getUsuario());
					else
						connector.acceptConnect();

					// /*
					// * Añadir elementos en matrix del CausalLinker
					// */
					// if (DEBUG)
					// System.out.println("MulticastListenerThread.java: "
					// + "Añadimos elementos en la matrix del CausalLinker");
					//					
					// // TODO
					//					
					//

					/*
					 * Añadir a la ListaUsuarios el usuario
					 */
					if (DEBUG)
						System.out.println("MulticastListenerThread.java: "
								+ "Añadimos el usuario a ListaUsuarios");

					panchat.getListaUsuarios().añadirUsuario(
							msgCliente.getUsuario());

					// Creamos el ListenerThread para escuchar al socket

				} else {

					// TODO
				}
		}
	}
}
