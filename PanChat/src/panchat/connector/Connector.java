package panchat.connector;

import panchat.data.User;
import panchat.messages.Message;

public interface Connector {

	/**
	 * @return Devuelve un objeto leido a traves del connector
	 */
	Object read(User user);

	/**
	 * Le pasamos al connector el objeto que hemos leido
	 * 
	 * @param message
	 */
	public void receive(Message message);

	/**
	 * @return Devuelve si el connector se ha cerrado.
	 */
	boolean isClosed(User user);

	/**
	 * Cerramos el connector, el hilo y el socket
	 */
	public void close();
}
