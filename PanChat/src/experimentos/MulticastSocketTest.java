package experimentos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MulticastSocketTest {

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

	/**
	 * 
	 */
	public MulticastSocketTest() {

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

		final MulticastSocketTest zeroconf = new MulticastSocketTest();
		final MulticastSocketTest zeroconfArray[] = new MulticastSocketTest[4];
		Thread threadPool[] = new Thread[4];

		System.out.println(zeroconf.addr);
		System.out.println(zeroconf.name);

		/*
		 * Creamos un hilo que manda un mensaje.
		 */
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("enviando Hello!!!");
				String msg = "Hello";
				DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg
						.length(), zeroconf.group, MulticastSocketTest.MDNS_PORT);
				try {
					zeroconf.socket.send(hi);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("enviado!!");
			}

		});

		/*
		 * Creamos una serie de hilos que leen.
		 */
		for (int i = 0; i < 4; i++) {
			zeroconfArray[i] = new MulticastSocketTest();
			final int ii = i;
			threadPool[i] = new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println(ii + " recibiendo!!!");
					byte[] buf = new byte[1000];
					DatagramPacket recv = new DatagramPacket(buf, buf.length);
					try {
						zeroconfArray[ii].socket.receive(recv);
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(ii + " recibido!!! : -> ["
							+ new String(recv.getData(), 0, recv.getLength())
							+ "]");
				}
			});
			threadPool[i].start();
		}

		try {
			Thread.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		thread.start();

		try {
			thread.join();
			zeroconf.close();
			for (int i = 0; i < 4; i++) {
				threadPool[i].join();
				zeroconfArray[i].close();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
