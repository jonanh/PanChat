package panchat.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import panchat.Panchat;
import panchat.ui.chat.VentanaConversacion;
import panchat.ui.chat.VentanaConversacionCanal;

public class ListaConversaciones {

	private static final long serialVersionUID = 1L;

	// Ventanas de conversaciones
	private HashMap<Usuario, VentanaConversacion> ventanaConversaciones;

	private HashMap<Canal, VentanaConversacionCanal> ventanaConversacionesCanales;

	private Panchat panchat;

	private Object mutex = new Object();

	/**
	 * Creamos lista de conversaciones
	 * 
	 * Esta clase sirve para reconstruir toda la información de los canales a
	 * los nuevos usuarios. Y también para gestionar las ventanas de
	 * conversaciones existentes.
	 */
	public ListaConversaciones(Panchat pPanchat) {
		this.panchat = pPanchat;

		this.ventanaConversaciones = new HashMap<Usuario, VentanaConversacion>();

		this.ventanaConversacionesCanales = new HashMap<Canal, VentanaConversacionCanal>();
	}

	public void eliminarConversacion(Usuario usuario) {
		ventanaConversaciones.remove(usuario);
	}

	public void eliminarConversacion(Canal canal) {
		ventanaConversacionesCanales.remove(canal);
	}

	public VentanaConversacion getVentanaConversacion(Usuario usuario) {
		if (!ventanaConversaciones.containsKey(usuario)) {
			VentanaConversacion ventana = new VentanaConversacion(panchat,
					usuario);
			ventanaConversaciones.put(usuario, ventana);
			return ventana;
		} else {
			return ventanaConversaciones.get(usuario);
		}
	}

	public VentanaConversacionCanal getVentanaConversacion(Canal canal) {
		if (!ventanaConversacionesCanales.containsKey(canal)) {
			VentanaConversacionCanal ventana = new VentanaConversacionCanal(
					panchat, canal);
			ventanaConversacionesCanales.put(canal, ventana);
			return ventana;
		} else {
			return ventanaConversacionesCanales.get(canal);
		}
	}

	public LinkedList<Canal> getListaConversacionesCanal() {
		synchronized (mutex) {
			/*
			 * Creamos a partir de la tabla hash de conversaciones, todas las
			 * conversaciones en las que estamos
			 */
			LinkedList<Canal> lista = new LinkedList<Canal>();

			Iterator<Entry<Canal, VentanaConversacionCanal>> iter;
			iter = ventanaConversacionesCanales.entrySet().iterator();

			while (iter.hasNext()) {
				lista.add(iter.next().getKey());
			}
			return lista;
		}
	}
}
