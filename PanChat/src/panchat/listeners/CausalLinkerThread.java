package panchat.listeners;

import java.io.IOException;
import java.net.MulticastSocket;

import panchat.Panchat;
import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.User;
import panchat.messages.CausalMessage;
import panchat.messages.MessageChat;
import panchat.order.CausalMatrixLayer;
import panchat.protocol.JoinChannel;
import panchat.protocol.SaludoListaCanales;

public class CausalLinkerThread extends Thread {

	public final static boolean DEBUG = false;

	private MulticastSocket socket;

	private Panchat panchat;

	private CausalMatrixLayer causalLinker;

	public CausalLinkerThread(MulticastSocket pSocket, Panchat pPanchat) {
		this.socket = pSocket;
		this.panchat = pPanchat;
		this.causalLinker = panchat.getCausalLinker();
	}

	public void run() {

		while (!socket.isClosed()) {

			try {
				// Leyendo objeto

				CausalMessage cm = causalLinker.handleMsg();

			} catch (IOException e1) {
				// Se ha cerrado el socket
			}
		}

		printDebug("Terminado");
	}


	private void printDebug(String string) {
		String msgClase = "CausalLinkerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
