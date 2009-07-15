package panchat.listeners;

import java.io.*;
import java.util.UUID;

import panchat.Panchat;
import panchat.messages.CausalMessage;
import panchat.messages.SimpleMessage;
import panchat.share.protocolo.Bloque;
import panchat.share.protocolo.Fichero;

public class ListenerThread extends Thread {
	private boolean terminar;
	private Panchat panchat;
	private ObjectInputStream ois;
	private UUID uuid;

	public ListenerThread(Panchat panchat, UUID uuid, ObjectInputStream ois) {
		this.panchat = panchat;
		this.ois = ois;
		this.uuid = uuid;
	}

	public void run() {

		while (!terminar) {
			try {
				Object msg = ois.readObject();
				if (msg instanceof CausalMessage) {
					panchat.getCausalLinker().anyadirMensaje(uuid,
							(CausalMessage) msg);
				} else if (msg instanceof SimpleMessage) {
					panchat.getCausalLinker().anyadirMensaje(uuid,
							(CausalMessage) msg);
				} else if (msg instanceof Fichero) {

					/*
					 * Preguntamos donde queremos guardar el fichero
					 */

					// TODO
					// int returnVal = fc.showSaveDialog(FileChooserDemo.this);
					// if (returnVal == JFileChooser.APPROVE_OPTION) {
					// File file = fc.getSelectedFile();
					// }
					/*
					 * Salvamos la información en una tabla hash
					 */

					// TODO
				} else if (msg instanceof Bloque) {
					/*
					 * Buscamos donde queríamos guardar el bloque en la tabla
					 * hash
					 */

					// TODO
					/*
					 * Escribimos el fichero
					 */

					// TODO
				}
			} catch (IOException e) {
				terminar = true;
				System.err.println(e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
}
