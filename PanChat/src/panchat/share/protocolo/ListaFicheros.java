package panchat.share.protocolo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;


public class ListaFicheros implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LinkedList<Fichero> listaFicheros;

	/**
	 * 
	 * @return
	 */
	public LinkedList<Fichero> getListaFicheros() {
		return listaFicheros;
	}

	/**
	 * 
	 */
	public ListaFicheros() {
		listaFicheros = listFicheros(new File("."));
	}

	/*
	 * M�todos privados.
	 */

	/**
	 * Devuelve una lista que contiene todos los ficheros disponibles en el
	 * servidor.
	 */
	private static LinkedList<Fichero> listFicheros(File directory) {

		LinkedList<Fichero> files = new LinkedList<Fichero>();
		File[] contenidoDir = directory.listFiles();

		for (File entrada : contenidoDir) {
			try {

				if (entrada.isFile())
					files.add(new Fichero(entrada));
				
				else if (entrada.isDirectory())
					files.addAll(listFicheros(entrada));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return files;
	}
}