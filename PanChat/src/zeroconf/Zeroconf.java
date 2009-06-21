package zeroconf;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Zeroconf {

	/**
	 * This is the multicast group, we are listening to for multicast DNS
	 * messages.
	 */
	private InetAddress group;

	/**
	 * This is our multicast socket.
	 */
	private MulticastSocket socket;

	private InetAddress addr;
	private String name;

	/*
	 * constantes
	 */
	public final static String MDNS_GROUP = "224.0.0.251";
	public final static String MDNS_GROUP_IPV6 = "FF02::FB";
	// public final static int MDNS_PORT = 5353;
	// Modificado para que no haya troubles con el mDNS 'real' 0:-)
	public final static int MDNS_PORT = 5454;
	public final static int DNS_PORT = 53;

	public Zeroconf() {

		/*
		 * Obtenemos la info de la red "local"
		 */
		try {
			addr = InetAddress.getLocalHost();
			name = addr.getHostAddress();
			if (addr.isLoopbackAddress())
				addr = null;
		} catch (final IOException e) {
			addr = null;
			name = "local";
		}

		/*
		 * Creamos el socket Multicast
		 */
		try {
			group = InetAddress.getByName(MDNS_GROUP);

			socket = new MulticastSocket(MDNS_PORT);

			socket.joinGroup(group);

		} catch (UnknownHostException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void close() {
		try {
			socket.leaveGroup(group);

			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Zeroconf zeroconf = new Zeroconf();
		Zeroconf zeroconf2 = new Zeroconf();

		System.out.println(zeroconf.addr);
		System.out.print(zeroconf.name);

		/*
		 * Ahora hay que aprender a crear un paquete, mandarlo y recibirlo :-P
		 */

		// DatagramPacket packet = new DatagramPacket();
		// packet.setData("hola");
		//		
		// zeroconf.socket.send(new DatagramPacket());

	}

}
