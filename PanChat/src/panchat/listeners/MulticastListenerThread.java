package panchat.listeners;

import java.io.IOException;
import java.net.MulticastSocket;

import panchat.Panchat;
import panchat.connector.Connector;
import panchat.data.Usuario;
import panchat.messages.SaludoListaCanales;
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

				if (usuario.uuid.compareTo(panchat.getUsuario().uuid) < 0) {

					connector.connect(usuario);

					accionesRegistro(usuario);

				} else {

					Usuario usuarioLeido = connector.acceptConnect();

					accionesRegistro(usuarioLeido);
				}

				/*
				 * Añadir a la ListaUsuarios el usuario
				 */
				printDebug("Añadimos el usuario a ListaUsuarios");

				panchat.getListaUsuarios().añadirUsuario(usuario);
			}
		}
	}

	/**
	 * Realizamos la accionRegistro teniendo en cuenta que el usuario aceptado
	 * no tiene porque darse en orden, y por tanto puede ocasionarse una
	 * potencial condición de carrera que puede ser evitada tratando el elemento
	 * registrado o aceptado
	 * 
	 * @param pUsuario
	 */
	private void accionesRegistro(Usuario pUsuario) {
		/*
		 * Añadir elementos en el CausalLinker y el Linker
		 */
		printDebug("Añadimos elementos a los Linkers");

		panchat.getCausalLinker().anyadirUsuario(pUsuario);

		/*
		 * Enviamos al usuario la información que tenemos sobre los canales.
		 */
		printDebug("Enviamos información sobre canales");

		SaludoListaCanales saludo = new SaludoListaCanales(panchat
				.getListaConversaciones().getListaConversacionesCanal());

		panchat.getCausalLinker().sendMsg(pUsuario, saludo);

	}

	private void printDebug(String string) {
		String msgClase = "MulticastListenerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
