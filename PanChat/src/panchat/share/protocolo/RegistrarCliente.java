package panchat.share.protocolo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import panchat.addressing.Usuario;

public class RegistrarCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private boolean registrar;

	/**
	 * Crear un nuevo mensaje para registrar clientes
	 * 
	 * @param address
	 * @param registrar
	 */
	public RegistrarCliente(Usuario address, boolean registrar) {
		this.usuario = address;
		this.registrar = registrar;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public boolean isRegistrar() {
		return registrar;
	}

	/**
	 * Nos pasa el objeto a un buffer para enviarlo en socket Multicast.
	 * 
	 * @return
	 */
	public byte[] bytes() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;

		try {

			oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			oos.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return baos.toByteArray();
	}
	
	/**
	 * Nos devuelve una clase RegistrarCliente desde un Socket Multicast
	 * 
	 * @param socket
	 * @return
	 */
	public static RegistrarCliente leerRegistrarCliente(MulticastSocket socket) {

		// Creamos un buffer donde guardar lo leído
		byte[] buf = new byte[1000];

		DatagramPacket recv = new DatagramPacket(buf, buf.length);

		// Leemos del Socket
		try {
			socket.receive(recv);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creamos un ByteArrayInputStream desde donde serializar el objeto
		ByteArrayInputStream bais = new ByteArrayInputStream(recv.getData(), 0,
				recv.getData().length);
		ObjectInputStream ois;

		RegistrarCliente paquete;

		// Intentamos leer el objeto
		try {

			ois = new ObjectInputStream(bais);
			paquete = (RegistrarCliente) ois.readObject();
			ois.close();

			return paquete;

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Si algo ha salido mal, devolvemos null
		return null;
	}
}
