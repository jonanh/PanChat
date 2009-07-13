package panchat.addressing;

import java.util.Iterator;
import java.util.LinkedList;

public class Conexiones {

	private static Conexiones conexiones = new Conexiones();

	private LinkedList<Address> listaConexiones;

	private Conexiones() {
		listaConexiones = new LinkedList<Address>();
	}

	public static Conexiones getInstanceOf() {
		return conexiones;
	}

	public Iterator<Address> getIterator() {
		return listaConexiones.iterator();
	}

	public void añadirElemento(Address address) {
		listaConexiones.add(address);
	}
}
