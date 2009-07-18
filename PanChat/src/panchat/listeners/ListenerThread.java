package panchat.listeners;

import java.io.*;
import java.net.Socket;

import panchat.Panchat;
import panchat.data.Usuario;
import panchat.messages.CausalMessage;
import panchat.messages.SimpleMessage;
import panchat.share.protocolo.Bloque;
import panchat.share.protocolo.Fichero;

public class ListenerThread extends Thread {

	public final static boolean DEBUG = false;

	private Panchat panchat;
	private ObjectInputStream ois;
	private Socket socket;
	private Usuario usuario;

	public ListenerThread(Panchat panchat, Usuario usuario, Socket socket,
			ObjectInputStream ois) {
		this.panchat = panchat;
		this.ois = ois;
		this.usuario = usuario;
		this.socket = socket;
	}

	public void run() {

		printDebug("Creado hilo para escuchar a : " + usuario.nickName);

		while (!socket.isClosed()) {
			try {
				// Leyendo objeto
				Object msg = ois.readObject();
				
				if (msg instanceof CausalMessage) {
					
					panchat.getCausalLinker().anyadirMensaje(usuario.uuid,
							(CausalMessage) msg);
					
				} else if (msg instanceof SimpleMessage) {
					
					panchat.getCausalLinker().anyadirMensaje(usuario.uuid,
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
				// El socket se ha cerrado
				try {

					printDebug("Se ha cerrado el socket");

					// Lo eliminamos del listado de usuarios

					printDebug("Borramos a " + usuario.nickName
							+ " de la lista de usuarios");
					panchat.getListaUsuarios().eliminarUsuario(usuario);

					// Lo eliminamos del listado de canales

					// printDebug("Borramos a " + usuario.nickName
					// + " de la lista de canales");
					panchat.getListaCanales().eliminarUsuario(usuario);

					socket.close();
				} catch (IOException e1) {
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		printDebug("Hilo terminado");
	}

	private void printDebug(String string) {
		String msgClase = "ListenerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
