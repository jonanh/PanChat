package panchat.linker;

import java.util.List;

import panchat.messages.Message;

public interface ReceiveLinker {

	/**
	 * Recibir mensajes
	 */
	public List<Message> receive();
}
