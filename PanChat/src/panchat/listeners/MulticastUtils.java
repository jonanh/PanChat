package panchat.listeners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastUtils {
	/**
	 * Nos pasa el objeto a un buffer para enviarlo en socket Multicast.
	 * 
	 * @return
	 */
	public static byte[] objetoABytes(Object objeto) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;

		try {

			oos = new ObjectOutputStream(baos);
			oos.writeObject(objeto);
			oos.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * Lee un objeto de un socket multicast
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Object leerMultiCastSocket(MulticastSocket socket)
			throws IOException {

		// Creamos un buffer donde guardar lo leído
		byte[] buf = new byte[1000];

		DatagramPacket recv = new DatagramPacket(buf, buf.length);

		// Leemos del Socket
		socket.receive(recv);

		// Creamos un ByteArrayInputStream desde donde serializar el objeto
		ByteArrayInputStream bais = new ByteArrayInputStream(recv.getData(), 0,
				recv.getData().length);
		ObjectInputStream ois;

		Object objeto;

		// Intentamos leer el objeto
		try {

			ois = new ObjectInputStream(bais);
			objeto = ois.readObject();
			ois.close();

			return objeto;

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Si algo ha salido mal, devolvemos null
		return null;
	}
}
