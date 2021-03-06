package order.layer;

import java.util.*;
import java.util.Map.Entry;

import order.Message;
import order.Message.Type;
import order.clocks.CausalMatrix;

import panchat.data.User;

public class CausalMatrixLayer extends OrderLayer {

	private CausalMatrix matrix;

	public CausalMatrixLayer(User myId) {
		super(myId);
		this.matrix = new CausalMatrix(myId);
	}

	/**
	 * Envío de mensaje causal
	 * 
	 * @param destId
	 * @param msg
	 */
	@Override
	public synchronized void sendMsg(User user, Message msg, boolean answer) {

		matrix.send(user);

		msg.setClock(orderCapability(), matrix.clone());

		super.sendMsg(user, msg, answer);
	}

	/**
	 * Envío de mensaje multicast y causal
	 * 
	 * @param destIds
	 * @param msg
	 */
	@Override
	public synchronized void sendMsg(List<User> users, Message msg,
			boolean answer) {

		matrix.send(users);

		msg.setClock(orderCapability(), matrix.clone());

		super.sendMsg(users, msg, answer);
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

		if (W.getValue(srcId, user) > (matrix.getValue(srcId, user) + 1)) {
			return false;
		}

		// Primero añadimos unas columnas que falten en las filas ya existentes.
		Iterator<Entry<User, Hashtable<User, Integer>>> iter = matrix.HashMatrix
				.entrySet().iterator();
		while (iter.hasNext()) {

			User kIduuid = iter.next().getKey();
			if ((!kIduuid.equals(srcId))
					&& (W.getValue(kIduuid, user) > matrix.getValue(kIduuid,
							user))) {

				return false;
			}
		}

		return true;
	}

	@Override
	protected receiveStatus okayToRecv(Message msg) {

		CausalMatrix cm = (CausalMatrix) msg.getClock(orderCapability());

		if (okayToRecv(cm, msg.getUsuario())) {

			/*
			 * Actualizamos la matrix
			 */
			matrix.receiveAction(cm);

			return receiveStatus.Receive;
		}

		return receiveStatus.Nothing;
	}

	@Override
	public Type orderCapability() {
		return Message.Type.CAUSAL;
	}

	@Override
	public String layerName() {
		return "CausalLayer";
	}

	/**
	 * Añadimos nuevo usuario en el causal linker
	 * 
	 * @param usuario
	 */
	@Override
	public void addUser(User usuario) {
		matrix.addUser(usuario);
	}

	/**
	 * Añadimos nuevo usuario en el causal linker
	 * 
	 * @param usuario
	 */
	@Override
	public void removeUser(User usuario) {
		// FIXME
	}

	public CausalMatrix getCausalMatrix() {
		return this.matrix;
	}
}
