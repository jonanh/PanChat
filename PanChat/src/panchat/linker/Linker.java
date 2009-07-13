package panchat.linker;

import java.util.*;
import java.io.*;

public class Linker {
	private PrintWriter[] dataOut;
	private BufferedReader[] dataIn;
	private BufferedReader dIn;
	private int myId, N;
	private Connector connector;

	public LinkedList neighbors = new LinkedList();

	public Linker(String basename, int id, int numProc) throws Exception {
		myId = id;
		N = numProc;

		dataIn = new BufferedReader[numProc];
		dataOut = new PrintWriter[numProc];

		// Topology.readNeighbors(myId, N, neighbors);

		connector = new Connector();
		connector.Connect(basename, myId, numProc, dataIn, dataOut);
	}

	public void sendMsg(int destId, String tag, String msg) {
		dataOut[destId].println(myId + " " + destId + " " + tag + " " + msg
				+ "#");
		dataOut[destId].flush();
	}

	public void sendMsg(int destId, String tag) {
		sendMsg(destId, tag, " 0 ");
	}

	/**
	 * Envía un mensaje a multiples destinatarios
	 * 
	 * @param destIds
	 * @param tag
	 * @param msg
	 */
	public void multicast(LinkedList destIds, String tag, Message msg) {
		for (int i = 0; i < destIds.size(); i++) {
			sendMsg(destIds.getEntry(i), tag, msg);
		}
	}

	/**
	 * Recibe un mensaje de un cliente
	 * 
	 * @param fromId
	 *            La dirección del cliente del cúal recibir
	 * 
	 * @return El mensaje recibido
	 * 
	 * @throws IOException
	 */
	public Message receiveMsg(Address fromId) throws IOException {

		String getline = dataIn[fromId].readLine();
		Util.println(" received message " + getline);
		StringTokenizer st = new StringTokenizer(getline);
		int srcId = Integer.parseInt(st.nextToken());
		int destId = Integer.parseInt(st.nextToken());
		String tag = st.nextToken();
		String msg = st.nextToken("#");
		return new Msg(srcId, destId, tag, msg);

	}

	/**
	 * @return El identificador de nuestro cliente
	 */
	public int getMyId() {
		return myId;
	}

	/**
	 * @return El número de procesos
	 */
	public int getNumProc() {
		return N;
	}

	/**
	 * Cierra los sockets.
	 */
	public void close() {
		connector.closeSockets();
	}
}
