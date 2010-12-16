package panchat.connector;

import panchat.data.User;
import panchat.messages.Message;

public class SocketListenerThread extends Thread {

	public final static boolean DEBUG = false;

	private Connector connector;

	private User user = null;

	public SocketListenerThread(Connector connector) {
		this.connector = connector;
	}

	public SocketListenerThread(Connector connector, User user) {
		this.connector = connector;
		this.user = user;
	}

	public void run() {

		if (user != null)
			printDebug("Creado hilo para escuchar a : " + user.nickName);
		else
			printDebug("Creado hilo para escuchar el socket multicast");

		while (!connector.isClosed(user)) {

			// Leyendo objeto
			Object message = connector.read(user);

			// Registrar nuevo usuario
			if (message instanceof Message) {
				connector.receive((Message) message);
			} else {
				printDebug("Se ha recibido un paquete que no es un mensaje");
			}
		}

		printDebug("Hilo de escucha de " + user.nickName + " finalizado");
	}

	private void printDebug(String string) {

		String msgClase = "MulticastListenerThread: ";

		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
