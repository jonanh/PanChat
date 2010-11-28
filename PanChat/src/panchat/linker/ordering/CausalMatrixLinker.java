package panchat.linker.ordering;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import panchat.Panchat;
import panchat.clocks.CausalMatrix;
import panchat.data.User;
import panchat.linker.Linker;
import panchat.messages.CausalMatrixMessage;
import panchat.messages.CausalMessage;

public class CausalMatrixLinker extends Linker {

	private static final boolean DEBUG = false;

	private CausalMatrix matrix;

	private Object mutex = new Object();

	/*
	 * Cola de mensajes de entrega.
	 */
	LinkedList<CausalMatrixMessage> deliveryQ = new LinkedList<CausalMatrixMessage>();

	/*
	 * Cola de mensajes pendientes.
	 */
	LinkedList<CausalMatrixMessage> pendingQ = new LinkedList<CausalMatrixMessage>();

	/**
	 * 
	 * @param panchat
	 * @throws Exception
	 */
	public CausalMatrixLinker(Linker linker, User myId) {
		super(linker, myId);
		this.matrix = new CausalMatrix(myId);
	}

	/**
	 * Envío de mensaje causal
	 * 
	 * @param destId
	 * @param msg
	 */
	@Override
	public void sendMsg(User destId, Object msg) {

		synchronized (mutex) {
			printDebug("sendMsg " + destId.nickName + " " + msg.getClass());

			matrix.incrementarDestino(destId);

			/*
			 * Si nos estamos enviando un mensaje a nosotros mismos lo añadimos
			 * directamente a la lista de pendientes
			 */
			if (destId.equals(myId)) {
				mutex.notifyAll();
				pendingQ.add(new CausalMatrixMessage(msg, myId, matrix));
			} else
				super.socketSendMsg(destId, new CausalMatrixMessage(msg, myId,
						matrix));
		}
	}

	/**
	 * Envío de mensaje multicast y causal
	 * 
	 * @param destIds
	 * @param msg
	 */
	@Override
	public void sendMsg(LinkedList<User> destIds, Object msg) {
		synchronized (mutex) {
			printDebug("sendMsg multicast " + destIds + " " + msg.getClass());

			matrix.incrementarDestino(destIds);

			for (User usuario : destIds)

				/*
				 * Si nos estamos enviando un mensaje a nosotros mismos lo
				 * añadimos directamente a la lista de pendientes
				 */
				if (usuario.equals(myId))
					pendingQ.add(new CausalMatrixMessage(msg, myId, matrix));
				else
					socketSendMsg(usuario, new CausalMatrixMessage(msg, myId,
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
	private boolean okayToRecv(CausalMatrix W, User srcId) {
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
				myId.uuid) + 1) {
			return false;
		}

		// Primero añadimos unas columnas que falten en las filas ya existentes.
		Iterator<Entry<UUID, Hashtable<UUID, Integer>>> iter = matrix.HashMatrix
				.entrySet().iterator();
		while (iter.hasNext()) {

			UUID kIduuid = iter.next().getKey();
			if ((!kIduuid.equals(srcId.uuid))
					&& (W.getValue(kIduuid, myId.uuid) > matrix.getValue(
							kIduuid, myId.uuid))) {

				return false;
			}
		}

		return true;
	}

	/**
	 * Comprobamos la cola de pendientes, y en caso de que estén listos, los
	 * cambiamos a la cola de entrega.
	 */
	private void checkPendingQ() {

		Iterator<CausalMatrixMessage> iter = pendingQ.iterator();

		while (iter.hasNext()) {
			CausalMatrixMessage cm = iter.next();

			printDebug("comprobando mensaje " + cm.getContent().getClass());

			if (okayToRecv(cm.getCausalMatrix(), cm.getUsuario())) {

				printDebug("mensaje añadido a la cola de delivery "
						+ cm.getContent().getClass());

				iter.remove();
				deliveryQ.add(cm);
			}
		}

		printDebug("despues de checkPendingQ : pendingQ : " + pendingQ.size());
		printDebug("despues de checkPendingQ : deliveryQ : " + deliveryQ.size());
	}

	/**
	 * Obtiene el primer paquete disponible.
	 * 
	 * Si no hay un paquete disponible, realiza un wait hasta que lo esté.
	 */
	@Override
	public CausalMatrixMessage handleMsg() throws IOException {

		// tratamos la lista de pendientes
		synchronized (mutex) {
			checkPendingQ();

			/*
			 * Esperamos mientras no hayamos recibido ningún elemento
			 */
			while (deliveryQ.isEmpty()) {

				printDebug("deliveryQ is empty, durmiendo");

				// Mientras la cola de pendientes esté vacía dormimos
				try {

					mutex.wait();

				} catch (InterruptedException e) {
				}

				printDebug("se ha recibido algo, comprobando");

				// tratamos la lista de pendientes
				checkPendingQ();
			}

			printDebug("objeto en deliveryQ");

			/*
			 * Obtenemos el primer elemento de pendientes.
			 */
			CausalMatrixMessage cm = deliveryQ.poll();

			/*
			 * Actualizamos la matrix
			 */
			matrix.maxMatrix(cm.getCausalMatrix());

			return cm;
		}
	}

	/**
	 * Añadimos nuevo usuario en el causal linker
	 * 
	 * @param usuario
	 */
	public void anyadirUsuario(User usuario) {
		synchronized (mutex) {
			matrix.anyadirUsuario(usuario);
		}
	}

	/**
	 * Añadimos un nuevo mensaje a la cola de mensajes
	 */
	public void anyadirMensaje(CausalMatrixMessage msg) {
		synchronized (mutex) {
			printDebug("Elemento añadido a la cola de pendientes");

			pendingQ.add(msg);
			mutex.notify();
		}
	}

	private void printDebug(String string) {
		String msgClase = "CausalLinker.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
