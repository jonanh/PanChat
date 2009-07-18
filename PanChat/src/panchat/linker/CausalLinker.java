package panchat.linker;

import java.util.*;
import java.io.*;

import panchat.Panchat;
import panchat.data.Usuario;
import panchat.messages.CausalMessage;

public class CausalLinker extends Linker {

	private CausalMatrix matrix;

	private Object mutex = new Object();

	/*
	 * Cola de mensajes de entrega.
	 */
	LinkedList<CausalMessage> deliveryQ = new LinkedList<CausalMessage>();

	/*
	 * Cola de mensajes pendientes.
	 */
	LinkedList<CausalMessage> pendingQ = new LinkedList<CausalMessage>();

	/**
	 * 
	 * @param panchat
	 * @throws Exception
	 */
	public CausalLinker(Panchat panchat) {
		super(panchat);

		matrix = new CausalMatrix(myId.uuid);
	}

	/**
	 * Envío de mensaje causal
	 * 
	 * @param destId
	 * @param msg
	 */
	public void sendMsg(Usuario destId, Object msg) {
		synchronized (mutex) {
			matrix.incrementarDestino(destId);

			/*
			 * Si nos estamos enviando un mensaje a nosotros mismos lo añadimos
			 * directamente a la lista de pendientes
			 */
			if (destId.equals(myId))
				pendingQ.add(new CausalMessage(msg, destId, matrix));
			else
				super.sendMsg(destId, new CausalMessage(msg, destId, matrix));
		}
	}

	/**
	 * Envío de mensaje multicast y causal
	 * 
	 * @param destIds
	 * @param msg
	 */
	public void sendMsg(LinkedList<Usuario> destIds, Object msg) {
		synchronized (mutex) {
			matrix.incrementarDestino(destIds);

			for (Usuario usuario : destIds)

				/*
				 * Si nos estamos enviando un mensaje a nosotros mismos lo
				 * añadimos directamente a la lista de pendientes
				 */
				if (usuario.equals(myId))
					pendingQ.add(new CausalMessage(msg, usuario, matrix));
				else
					super.sendMsg(usuario, new CausalMessage(msg, usuario,
							matrix));
		}
	}

	/**
	 * Condicción de espera
	 * 
	 * @param W
	 * @param srcId
	 * @return
	 */
	private boolean okayToRecv(CausalMatrix W, Usuario srcId) {
		// Algoritmo original
		//
		// if (W[srcId][myId] > M[srcId][myId] + 1)
		// ....return false;
		//
		// for (int k = 0; k < N; k++)
		// ........if ((k != srcId) && (W[k][myId] > M[k][myId]))
		// ........return false;
		//
		// return true;

		if (W.getValue(srcId.uuid, myId.uuid) > matrix.getValue(srcId.uuid,
				myId.uuid) + 1)
			return false;

		for (int k = 0; k < panchat.getListaUsuarios().getNumUsuarios(); k++) {

			UUID kIduui = panchat.getListaUsuarios().getUsuario(k).uuid;

			if ((kIduui != srcId.uuid)
					&& (W.getValue(kIduui, myId.uuid) > matrix.getValue(kIduui,
							myId.uuid)))
				return false;
		}

		return true;
	}

	/**
	 * Comprobamos la cola de pendientes, y en caso de que estén listos, los
	 * cambiamos a la cola de entrega.
	 */
	private synchronized void checkPendingQ() {

		Iterator<CausalMessage> iter = pendingQ.iterator();

		while (iter.hasNext()) {
			CausalMessage cm = iter.next();

			if (okayToRecv(cm.getMatrix(), cm.getUsuario())) {
				iter.remove();
				deliveryQ.add(cm);
			}
		}
	}

	/**
	 * Obtiene el primer paquete disponible.
	 * 
	 * Si no hay un paquete disponible, realiza un wait hasta que lo esté.
	 */
	// polls the channel given by fromId to add to the pendingQ
	public CausalMessage handleMsg() throws IOException {

		// tratamos la lista de pendientes
		synchronized (mutex) {
			checkPendingQ();
		}

		/*
		 * Esperamos mientras no hayamos recibido ningún elemento
		 */
		while (deliveryQ.isEmpty()) {

			// Mientras la cola de pendientes esté vacía dormimos
			if (pendingQ.isEmpty()) {
				try {
					pendingQ.wait();
				} catch (InterruptedException e) {
				}
			}

			// tratamos la lista de pendientes
			synchronized (mutex) {
				checkPendingQ();
			}
		}

		synchronized (mutex) {
			/*
			 * Obtenemos el primer elemento de pendientes.
			 */
			CausalMessage cm = deliveryQ.peek();

			/*
			 * Actualizamos la matrix
			 */
			matrix.maxMatrix(cm.getMatrix());

			return cm;
		}
	}

	/**
	 * Añadimos nuevo usuario en el causal linker
	 * 
	 * @param usuario
	 */
	public void anyadirUsuario(Usuario usuario) {
		matrix.anyadirUsuario(usuario);
	}

	/**
	 * Añadimos un nuevo mensaje a la cola de mensajes
	 */
	public synchronized void anyadirMensaje(UUID uuid, CausalMessage msg) {
		synchronized (mutex) {
			pendingQ.add(msg);
			pendingQ.notify();
		}
	}
}
